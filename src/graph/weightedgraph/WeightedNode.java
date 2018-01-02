package graph.weightedgraph;

import java.io.Serializable;
import java.util.HashMap;

public class WeightedNode<T> implements Serializable {
    private T data;
    //Key = data, Value = distance
    private HashMap<T, Integer> neighbours;

    public WeightedNode(T data) {
        this.data = data;
        neighbours = new HashMap<>();
    }

    public void addNeighbour(T nodeIdentifierData, Integer distance)
            throws IllegalArgumentException {

        neighbours.put(nodeIdentifierData, distance);
    }

    public void removeNeighbour(T nodeIdentifierData) {
        neighbours.remove(nodeIdentifierData);
    }

    //can only be used by Graph, because "this" cannot modify the neighbour's entry of "this"
    void moveNeighbour(T nodeIdentifierData, Integer newDistance)
            throws IllegalArgumentException {
        neighbours.put(nodeIdentifierData, newDistance);
    }

    public HashMap<T, Integer> getNeighbours() {
        return neighbours;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isIsolated() {
        return neighbours.size() == 0;
    }

    public boolean isNeighbour(T data) {
        return neighbours.containsKey(data);
    }

    public Integer getDistanceToNeighbour(T nodeIdentifier) {
        return neighbours.get(nodeIdentifier);
    }

    public T getClosestNeighbour() {
        Integer minDistance = Integer.MAX_VALUE;
        T closestNode = null;
        for (HashMap.Entry<T, Integer> it :
                neighbours.entrySet()) {
            if (it.getValue() < minDistance) {
                minDistance = it.getValue();
                closestNode = it.getKey();
            }
        }
        return closestNode;
    }

    public boolean anyNodeAtDistance(Integer distance) {
        return neighbours.containsValue(distance);
    }

    public boolean anyNodeWithinRadius(Integer radius) {
        for (HashMap.Entry<T, Integer> it :
                neighbours.entrySet())
            if (it.getValue() <= radius)
                return true;
        return false;
    }

    public int noOfNodesWithinRadius(Integer radius) {
        int nodeCount = 0;
        for (HashMap.Entry<T, Integer> it :
                neighbours.entrySet())
            if (it.getValue() <= radius)
                ++nodeCount;
        return nodeCount;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("\n");
        info.append(data.toString());
        if (isIsolated()) {
            info.append("\n\tNo neighbours");
            return info.toString();
        }
        for (HashMap.Entry<T, Integer> it :
                neighbours.entrySet()) {
            info.append("\n\t\"");
            info.append(it.getKey().toString());
            info.append("\" at a distance of ");
            info.append(it.getValue().toString());
        }
        return info.toString();
    }
}