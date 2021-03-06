/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.path_search;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import bean.pwr.imskamieskiego.data.map.entity.MapPointEntity;
import bean.pwr.imskamieskiego.model.map.Edge;
import bean.pwr.imskamieskiego.model.map.MapPoint;
import bean.pwr.imskamieskiego.repository.IMapGraphRepository;

/**
 * Implementation of Dijkstra algorithm.
 * @see PathSearcher
 */
public class DijkstraSearch implements PathSearchAlgorithm {

    public final static int DEFAULT_PENALIZATION = 1;
    private int penalizationFactor = DEFAULT_PENALIZATION;

    private int startPointID;
    private int[] endPointIDs;
    private IMapGraphRepository graphRepository;
    private List<MapPoint> path;

    /**
     * Create instance of Dijkstra algorithm to search path between given points. Points can't have
     * the same ID (be the same node on graph)
     * @see PathSearcher
     * @param graphRepository repository with graph data
     * @param startPoint point from which the algorithm should start searching for the path
     * @param endPoint target point
     * @throws IllegalArgumentException throws when start and end point are the same node
     */
    public DijkstraSearch(IMapGraphRepository graphRepository, MapPoint startPoint, MapPoint endPoint) throws IllegalArgumentException {
        if (startPoint.getId()==endPoint.getId()){
            throw new IllegalArgumentException("End point and start point are the same points!");
        }
        this.startPointID = startPoint.getId();
        this.endPointIDs = new int[1];
        this.endPointIDs[0] = endPoint.getId();
        this.graphRepository = graphRepository;
        this.path = new ArrayList<>();
    }

    /**
     * Create instance of Dijkstra algorithm to search path between given points. Any point on
     * targets list can't have the same ID (be the same node on graph) as startPoint.
     * @see PathSearcher
     * @param graphRepository repository with graph data
     * @param startPoint point from which the algorithm should start searching for the path
     * @param endPoints list of target points
     * @throws IllegalArgumentException throws when start point and any of end points are the same
     * node. Throws this exception also, when targets list is empty.
     */
    public DijkstraSearch(IMapGraphRepository graphRepository, MapPoint startPoint, List<MapPoint> endPoints) throws IllegalArgumentException {
        for (MapPoint point:endPoints) {
            if (startPoint.getId()==point.getId()){
                throw new IllegalArgumentException("End point and start point are the same points!");
            }
        }
        if (endPoints.isEmpty()) throw new IllegalArgumentException("End points list is empty!");


        this.startPointID = startPoint.getId();

        this.endPointIDs = new int[endPoints.size()];
        for (int i = 0; i < endPointIDs.length; i++) {
            endPointIDs[i] = endPoints.get(i).getId();
        }

        this.graphRepository = graphRepository;
        this.path = new ArrayList<>();
    }

    @Override
    public void setPenalizationFactor(int penalizationFactor) {
        this.penalizationFactor = penalizationFactor;
    }

    /**
     * Start path search between points given in constructor. Searching is a blocking operation
     * that can take a long time. Therefore, do not call it directly in the main thread.
     * @see PathSearcher
     */
    @Override
    public void startSearch() {
        //TODO depth fetch parameters should be chosen experimentally
        int initDepthFetch = 10;
        int depthFetch = 10;


        path.clear();

        Map<Integer, Integer> nodeVisitHistory = new HashMap<>();
        Map<Integer, Integer> distancesFromStart = new HashMap<>();
        Set<Integer> hardToReachPoints = new HashSet<>();

        PriorityQueue<NodePriorityWrapper> pathToTravel = new PriorityQueue<>(100,
                (dist1, dist2) -> dist1.distance-dist2.distance);

        Map<Integer, List<Edge>> outgoingEdges = new HashMap<>(fetchEdges(startPointID, initDepthFetch));
        if (penalizationFactor != DEFAULT_PENALIZATION){
            hardToReachPoints.addAll(fetchHardToReachNodes(new ArrayList<>(outgoingEdges.keySet())));
        }

        //We start from a point without outgoing edges. There is nowhere to go.
        if (outgoingEdges.isEmpty()){
            return;
        }

        nodeVisitHistory.put(startPointID, -1);
        distancesFromStart.put(startPointID, 0);
        pathToTravel.add(new NodePriorityWrapper(startPointID, distancesFromStart.get(startPointID)));

        while (!pathToTravel.isEmpty()){

            int from_id = pathToTravel.remove().id;

            if (isEndPoint(from_id)){
                //The target has been achieved.
                generatePath(nodeVisitHistory, from_id);
                return;
            }

            if (!outgoingEdges.containsKey(from_id)){
                Map<Integer, List<Edge>> fetchedEdges = fetchEdges(from_id, depthFetch);
                outgoingEdges.putAll(fetchedEdges);
                if (penalizationFactor != DEFAULT_PENALIZATION) {
                    hardToReachPoints.addAll(fetchHardToReachNodes(new ArrayList<>(fetchedEdges.keySet())));
                }
            }

            for (Edge edge:outgoingEdges.get(from_id)) {

                int edgeLength = edge.getLength() * (hardToReachPoints.contains(edge.getTo()) ? penalizationFactor : 1);
                int newBestDistance = edgeLength + distancesFromStart.get(from_id);
                int oldDistance = Integer.MAX_VALUE;
                if (distancesFromStart.containsKey(edge.getTo())){
                    oldDistance = distancesFromStart.get(edge.getTo());
                }

                if (newBestDistance < oldDistance){
                    distancesFromStart.put(edge.getTo(), newBestDistance);
                    nodeVisitHistory.put(edge.getTo(), from_id);
                    pathToTravel.add(new NodePriorityWrapper(edge.getTo(), newBestDistance));
                }
            }
        }
    }

    /**
     * Return the shortest path between points given in constructor. If searching didn't end or
     * can not find path, it returns empty;
     * @return found path
     */
    @Override
    @NonNull
    public List<MapPoint> getPath() {
        return path;
    }


    private static class NodePriorityWrapper {
        int id;
        int distance;

        NodePriorityWrapper(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }

    private boolean isEndPoint(int pointID){
        for (int id:endPointIDs) {
            if (pointID == id) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> fetchHardToReachNodes(List<Integer> nodeIds){
        List<MapPoint> nodes = graphRepository.getPointByID(nodeIds);
        Set<Integer> hardToReachNodes = new HashSet<>();
        for (MapPoint node:nodes) {
            if (node.isHardToReach()){
                hardToReachNodes.add(node.getId());
            }
        }
        return hardToReachNodes;
    }

    private Map<Integer, List<Edge>> fetchEdges(int nodeID, int depth) {
        if (depth < 0) depth = 0;
        List<Integer> toFetchList = new ArrayList<>();
        Map<Integer, List<Edge>> tmpFetchedEdges;
        Map<Integer, List<Edge>> fetchedEdges;

        toFetchList.add(nodeID);
        tmpFetchedEdges = graphRepository.getOutgoingEdges(toFetchList);
        fetchedEdges = tmpFetchedEdges;

        for (int i = 0; i < depth; i++) {
            toFetchList.clear();
            for (Integer id:tmpFetchedEdges.keySet()) {
                List<Edge> edges = tmpFetchedEdges.get(id);
                for (Edge edge : edges){
                    if (!fetchedEdges.containsKey(edge.getTo())) {
                        toFetchList.add(edge.getTo());
                    }
                }
            }
            tmpFetchedEdges = graphRepository.getOutgoingEdges(toFetchList);
            fetchedEdges.putAll(tmpFetchedEdges);

            if(tmpFetchedEdges.isEmpty()) break;
        }

        return fetchedEdges;
    }

    private void generatePath(Map<Integer, Integer> nodeVisitHistory, int endPointID){
        int pathSize = nodeVisitHistory.size();
        if (pathSize == 0) return;
        if (!nodeVisitHistory.containsKey(endPointID)) return;


        List<Integer> listOfVisits = new ArrayList<>();
        int previousID = endPointID;

        while (previousID >= 0){
            listOfVisits.add(previousID);
            previousID = nodeVisitHistory.get(previousID);
        }

        List<MapPoint> unsortedMapPointList = graphRepository.getPointByID(listOfVisits);

        for (int i = listOfVisits.size()-1; i >= 0; i--) {
            int id = listOfVisits.get(i);
            for (MapPoint point:unsortedMapPointList) {
                if (point.getId() == id){
                    path.add(point);
                    break;
                }
            }
        }
    }
}
