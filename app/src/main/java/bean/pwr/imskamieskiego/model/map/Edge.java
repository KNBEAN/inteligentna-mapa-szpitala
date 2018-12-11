/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package bean.pwr.imskamieskiego.model.map;

/**
 * This interface represents directional edge on the graph. Edge is a object that make connection
 * between nodes.
 */
public interface Edge {

    /**
     * ID of start point of edge
     * @return
     */
    int getFrom();

    /**
     * ID of end point of edge
     * @return
     */
    int getTo();

    /**
     * Length of edge.
     * @return
     */
    int getLength();

}
