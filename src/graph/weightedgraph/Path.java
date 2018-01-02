package graph.weightedgraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Path<T> implements Comparable<Path<T>> {
    private final LinkedList<T> path;
    private final HashMap<T, Integer> distances;

    Path(HashMap<T, T> nodePath, HashMap<T, Integer> distances, T source, T target) {
        this.distances = distances;
        path = new LinkedList<>();
        T nodeIdentifier = target;

        do {
            path.addFirst(nodeIdentifier);
            nodeIdentifier = nodePath.get(nodeIdentifier);
        } while (nodeIdentifier != source);

        path.addFirst(source);
    }

    public LinkedList<T> getShortestPath() {
        return path;
    }

    public HashMap<T, Integer> getDistances() {
        return distances;
    }

    public Integer noOfNodesInShortestPath() {
        return path.size();
    }

    public Integer getShortestPathLength() {
        try {
            return getPathLengthBetween(path.getFirst(), path.getLast());
        } catch (NoSuchElementException nsee) {
            return null;
        }
    }

    public Integer getPathLengthBetween(T node1, T node2)
            throws NoSuchElementException {
        if (!distances.containsKey(node1) || !distances.containsKey(node2))
            throw new NoSuchElementException("Node not part of the shortest path");
        return distances.get(node2) - distances.get(node1);
    }

    @Override
    public int compareTo(Path<T> o) {
        if (this.getShortestPathLength() < o.getShortestPathLength())
            return 1;
        else if (this.getShortestPathLength() > o.getShortestPathLength())
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return path.toString();
    }
}