package bean.pwr.imskamieskiego.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import bean.pwr.imskamieskiego.data.map.dao.EdgeDao;
import bean.pwr.imskamieskiego.data.map.entity.EdgeEntity;
import bean.pwr.imskamieskiego.model.map.Edge;


public class MapRepositoryImplTest {


    @Mock MapRepositoryImpl mapRepository;
    @Mock LocalDB localDB;
    @Mock EdgeDao edgeDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnMapOfListsOfEdgesForIDsList() {
        List<EdgeEntity> edgesFrom1 = new ArrayList<>();
        edgesFrom1.add(new EdgeEntity(1, 1, 2, 1));
        edgesFrom1.add(new EdgeEntity(2, 1, 3, 1));

        List<EdgeEntity> edgesFrom2 = new ArrayList<>();
        edgesFrom2.add(new EdgeEntity(3, 2, 1, 1));
        edgesFrom2.add(new EdgeEntity(4, 2, 3, 1));

        List<EdgeEntity> edges = new ArrayList<>();
        edges.addAll(edgesFrom1);
        edges.addAll(edgesFrom2);

        Map<Integer, List<Edge>> expected = new Hashtable<>();
        expected.put(1, new ArrayList<Edge>(edgesFrom1));
        expected.put(2, new ArrayList<Edge>(edgesFrom2));

        when(edgeDao.getOngoingEdges(anyListOf(Integer.class))).thenReturn(edges);
        when(localDB.getEdgeDao()).thenReturn(edgeDao);

        mapRepository = new MapRepositoryImpl(localDB);
        Map<Integer, List<Edge>> result = mapRepository.getOutgoingEdges(anyListOf(Integer.class));

        assertEquals(expected, result);
    }
}