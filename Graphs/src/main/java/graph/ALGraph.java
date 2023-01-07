package graph;

import java.util.*;

public class ALGraph<V extends Vertex, E extends Edge<V>> implements MGraph<V, E> {
    private Map<V,List<E>> aLGraph;
    // Rep Invariant:
    //           A map of vertices in the graph with all of their specific edges
    // Abstract Function:
    //          something

    /**
     * Constructor method for ALGraph
     */
    public ALGraph(){
        aLGraph = new HashMap<>();
    }

    /**
     * Add a vertex to the graph
     *
     * @param v vertex to add
     * @return true if the vertex was added successfully and false otherwise
     */
    public boolean addVertex(V v){
        List<E> vertexEdges = new ArrayList<>();
        if (!vertex(v)){
            aLGraph.put(v, vertexEdges);
            return true;
        }
        return false;
    }

    /**
     * Check if a vertex is part of the graph
     *
     * @param v vertex to check in the graph
     * @return true of v is part of the graph and false otherwise
     */
    public boolean vertex(V v){
        return aLGraph.containsKey(v);
    }

    /**
     * Add an edge of the graph
     *
     * @param e the edge to add to the graph
     * @return true if the edge was successfully added and false otherwise
     */
    public boolean addEdge(E e){
        if (!edge(e)){
            if (vertex(e.v1()) && vertex(e.v2())){
                List<E> vEdges = aLGraph.get(e.v1());
                vEdges.add(e);
                aLGraph.replace(e.v1(), vEdges);

                vEdges = aLGraph.get(e.v2());
                vEdges.add(e);
                aLGraph.replace(e.v2(), vEdges);

                return true;
            }
        }
        return false;
    }

    /**
     * Check if an edge is part of the graph
     *
     * @param e the edge to check in the graph
     * @return true if e is an edge in the graph and false otherwise
     */
    public boolean edge(E e){
        for (List<E> edgeLists : aLGraph.values()){
            for (E edge : edgeLists){
                if (edge.equals(e)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if v1-v2 is an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return true of the v1-v2 edge is part of the graph and false otherwise
     */
    public boolean edge(V v1, V v2){
        for (List<E> edgeLists : aLGraph.values()){
            for (E edge : edgeLists){
                if (edge.v1().equals(v1) || edge.v2().equals(v1)) {
                    if (edge.v1().equals(v2) || edge.v2().equals(v2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determine the length on an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return the length of the v1-v2 edge if this edge is part of the graph or -1 otherwise
     */
    public int edgeLength(V v1, V v2){
        int length = -1;
        if (edge(v1, v2)){
            List<E> edges = aLGraph.get(v1);
            for (E edge : edges){
                if (edge.v1().equals(v1) || edge.v2().equals(v1)) {
                    if (edge.v1().equals(v2) || edge.v2().equals(v2)) {
                        length = edge.length();
                    }
                }
            }
        }
        return length;
    }

    /**
     * Obtain the sum of the lengths of all edges in the graph
     *
     * @return the sum of the lengths of all edges in the graph
     */
    public int edgeLengthSum(){
        int sum = 0;
        Set<E> allEdges = new HashSet<>();
        for (List<E> edgeLists : aLGraph.values()){
            for (E edge : edgeLists){
                allEdges.add(edge);
            }
        }

        for (E edge : allEdges){
            sum += edge.length();
        }

        return sum;
    }

    /**
     * Remove an edge from the graph
     *
     * @param e the edge to remove
     * @return true if e was successfully removed and false otherwise
     */
    public boolean remove(E e){
        V v1 = e.v1();
        V v2 = e.v2();

        if (edge(v1, v2)){
            aLGraph.get(v1).remove(e);
            aLGraph.get(v2).remove(e);
            return true;
        }

        return false;
    }

    /**
     * Remove a vertex from the graph
     *
     * @param v the vertex to remove
     * @return true if v was successfully removed and false otherwise
     */
    public boolean remove(V v){
        if (aLGraph.containsKey(v)){
            Set<E> containingEdges = new HashSet<>();

            for (List<E> edgeList : aLGraph.values()){
                for (E edge : edgeList){
                    if (edge.v1().equals(v) || edge.v2().equals(v)){
                        containingEdges.add(edge);
                    }
                }
            }

            for (E edge : containingEdges){
                remove(edge);
            }

            aLGraph.remove(v);
            return true;
        }
        return false;
    }


    /**
     * Obtain a set of all vertices in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return a set of all vertices in the graph
     */
    public Set<V> allVertices(){
        Set<V> allVertices = new HashSet<>();

        for (V vertices : aLGraph.keySet()){
            Vertex v = vertices.clone();
            allVertices.add((V) v);
        }

        return allVertices;
    }

    /**
     * Obtain a set of all edges incident on v.
     * Access to this set **should not** permit graph mutations.
     *
     * @param v the vertex of interest
     * @return all edges incident on v
     */
    public Set<E> allEdges(V v){
        Set<E> allEdgesV = new HashSet<>();
        List<E> edges = aLGraph.get(v);

            for (E edge : edges){
                Edge e = edge.clone();
                allEdgesV.add((E) e);
            }

        return allEdgesV;
    }

    /**
     * Obtain a set of all edges in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return all edges in the graph
     */
    public Set<E> allEdges(){
        Set<E> allEdgesV = new HashSet<>();

        for (List<E> edgeList : aLGraph.values()){
            for (E edge : edgeList){
                Edge e = edge.clone();
                allEdgesV.add((E) e);
            }
        }
        return allEdgesV;
    }

    /**
     * Obtain all the neighbours of vertex v.
     * Access to this map **should not** permit graph mutations.
     *
     * @param v is the vertex whose neighbourhood we want.
     * @return a map containing each vertex w that neighbors v and the edge between v and w.
     */
    public Map<V, E> getNeighbours(V v){
        Map<V,E> neighbours = new HashMap<>();
        List<E> edgeList = new ArrayList<>(aLGraph.get(v));

        edgeList.forEach((edge) -> {
            Edge e = edge.clone();
            if (edge.v1().equals(v)){
                Vertex v2 = edge.v2().clone();
                neighbours.put((V) v2, (E) e);
            } else {
                Vertex v1 = edge.v1().clone();
                neighbours.put((V) v1, (E) e);
            }
        });

        return neighbours;
    }


}
