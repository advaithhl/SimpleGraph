package graph.weightedgraph;

import java.io.Serializable;

import java.util.*;

public class WeightedGraph<T> implements Serializable {
    //key : nodeIdentifier, value : WeightedNode
    private HashMap<T, WeightedNode<T>> nodeMap;

    public WeightedGraph() {
        nodeMap = new HashMap<>();
    }

    public WeightedGraph(T data) {
        addNode(data);
        nodeMap = new HashMap<>();
    }

    public WeightedGraph(WeightedNode<T> node) {
        addNode(node);
        nodeMap = new HashMap<>();
    }

    //add isolated node
    public WeightedNode<T> addNode(T data) {
        WeightedNode<T> node = new WeightedNode<>(data);
        addNode(node);
        return node;
    }

    //add isolated node
    public void addNode(WeightedNode<T> node) {
        nodeMap.putIfAbsent(node.getData(), node);
    }

    //add isolated node and add neighbour
    public void addNode(T data, T neighbour, Integer distanceToNeighbour)
            throws IllegalArgumentException {
        if (distanceToNeighbour < 1)
            throw new IllegalArgumentException("Distance cannot be less than 1");
        if (!nodeMap.containsKey(neighbour))
            throw new IllegalArgumentException("Referenced node not found");
        WeightedNode<T> node = addNode(data);
        nodeMap.get(data).addNeighbour(neighbour, distanceToNeighbour);
        nodeMap.get(neighbour).addNeighbour(data, distanceToNeighbour);
    }

    public void addNode(T data, WeightedNode<T> neighbour, Integer distanceToNeighbour)
            throws IllegalArgumentException {
        if (distanceToNeighbour < 1)
            throw new IllegalArgumentException("Distance cannot be less than 1");
        if (!nodeMap.containsKey(neighbour.getData()))
            throw new IllegalArgumentException("Referenced node not found");
        WeightedNode<T> node = addNode(data);
        nodeMap.get(data).addNeighbour(neighbour.getData(), distanceToNeighbour);
        nodeMap.get(neighbour.getData()).addNeighbour(data, distanceToNeighbour);
    }

    public void addNode(WeightedNode<T> node, WeightedNode<T> neighbour, Integer distanceToNeighbour)
            throws IllegalArgumentException {
        if (distanceToNeighbour < 1)
            throw new IllegalArgumentException("Distance cannot be less than 1");
        if (!nodeMap.containsKey(neighbour.getData()))
            throw new IllegalArgumentException("Referenced node not found");
        nodeMap.get(node.getData()).addNeighbour(neighbour.getData(), distanceToNeighbour);
        nodeMap.get(neighbour.getData()).addNeighbour(node.getData(), distanceToNeighbour);
    }

    public WeightedNode<T> getNode(T nodeIdentifier) {
        return nodeMap.get(nodeIdentifier);
    }

    //TODO : Change node distance to another value

    public void removeNode(T nodeIdentifier) {
        for (HashMap.Entry<T, Integer> it :
                nodeMap.get(nodeIdentifier).getNeighbours().entrySet())
            nodeMap.get(it.getKey()).removeNeighbour(nodeIdentifier);

        nodeMap.get(nodeIdentifier).getNeighbours().clear();
        nodeMap.remove(nodeIdentifier);
    }

    public Path<T> findPath(T source, T target)
            throws NoSuchElementException {
        HashMap<T, Integer> distances = new HashMap<>();
        //current node, prev node
        HashMap<T, T> crudePath = new HashMap<>();
        PriorityQueue<T> Q = new PriorityQueue<>((o1, o2) -> {
            if (distances.get(o1) > distances.get(o2))
                return 1;
            else if (distances.get(o1) < distances.get(o2))
                return -1;
            else
                return 0;
        });

        distances.put(source, 0);

        //initialisation
        for (HashMap.Entry<T, WeightedNode<T>> graphIterator :
                nodeMap.entrySet()) {
            //TODO: maybe change v = graphIterator.getKey()
            if (!graphIterator.getKey().equals(source)) {
                distances.put(graphIterator.getKey(), Integer.MAX_VALUE);
                crudePath.put(graphIterator.getKey(), null);
            }

            Q.add(graphIterator.getKey());
        }

        while (!Q.isEmpty()) {
            T u = Q.poll();
            Integer alt;
            //TODO: maybe change v = it.getKey()
            for (HashMap.Entry<T, Integer> it :
                    nodeMap.get(u).getNeighbours().entrySet()) {
                if (Q.contains(it.getKey())) {
                    alt = distances.get(u) + nodeMap.get(u).getDistanceToNeighbour(it.getKey());
                    if (alt < distances.get(it.getKey())) {
                        distances.put(it.getKey(), alt);
                        Q.remove(it.getKey());
                        Q.add(it.getKey());
                        crudePath.put(it.getKey(), u);
                    }
                }
            }
        }

        //making a path instance with the crudePath
        return new Path<>(crudePath, distances, source, target);
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("WeightedGraph with ");
        info.append(nodeMap.size());
        info.append(" elements\n");
        for (HashMap.Entry<T, WeightedNode<T>> it :
                nodeMap.entrySet()) {
            info.append(it.getValue().toString());
            info.append('\n');
        }
        return info.toString();
    }

    public static void main(String[] args) {
        WeightedGraph<String> g = new WeightedGraph<>();
        g.addNode("a");
        g.addNode("b");
        g.addNode("c");
        g.addNode("d");
        g.addNode("e");
        g.addNode("f");
        g.addNode("g");

        g.addNode("a","b", 2);
        g.addNode("a","c",3);
        g.addNode("b","d",1);
        g.addNode("c","d",4);
        g.addNode("b","e",5);
        g.addNode("c","f",7);
        g.addNode("d","e",3);
        g.addNode("e","f",4);
        g.addNode("e","g",6);
        g.addNode("f","g",2);

        /*System.out.println(g.getNode("b"));
        System.out.println(g.getNode("d"));
        System.out.println(g.getNode("c"));*/

        /*System.out.println(g.getShortestPath(null,null));
        System.out.println(g.getShortestPathLength(null,null));*/

        Path<String> path = g.findPath("a","g");
        System.out.println(path.getShortestPath());
        System.out.println(path.getShortestPathLength());
        System.out.println(path.noOfNodesInShortestPath());
    }
}