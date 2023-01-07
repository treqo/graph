package graph;

import java.util.*;
import java.util.stream.Collectors;

public class AMGraph<V extends Vertex, E extends Edge<V>> implements MGraph<V, E> {

    private List<V> vertices = new ArrayList<>();
    private int[][] graphMatrix;
    private int maxVertices;

    /**
     * Create an empty graph with an upper-bound on the number of vertices
     * @param maxVertices is greater than 1
     */

    public AMGraph(int maxVertices) {
        this.maxVertices = maxVertices;
        this.graphMatrix = new int[maxVertices][maxVertices]; //created an n by n matrix represented as 2d array
        Arrays.stream(graphMatrix).forEach(row -> Arrays.fill(row, -1));
    }

    /**
     * Add a vertex to the graph
     *
     * @param v vertex to add
     * @return true if the vertex was added successfully and false otherwise
     */
    public boolean addVertex(V v) {
        if(!vertices.contains(v) && vertices.size() < maxVertices
                && vertices.stream().noneMatch(vertex -> vertex.equals(v))) {
            return vertices.add(v);
        } else {
            return false;
        }
    }

    /**
     * Check if a vertex is part of the graph
     *
     * @param v vertex to check in the graph
     * @return true of v is part of the graph and false otherwise
     */
    public boolean vertex(V v) {
        return vertices.contains(v);
    }

    public boolean vertex(List<V> vList) {
        for(V v : vList) {
            if(!vertex(v)) {
                return false;
            }
        }
        return true;
    }

    private List<V> edgeToVertex(E e) {
        List<V> vList = new ArrayList<>();
        V v1 = e.v1();
        V v2 = e.v2();
        vList.add(v1);
        vList.add(v2);
        return vList;
    }

    private boolean checkEdge(int index1, int index2) {
        return graphMatrix[index1][index2] != -1
                && graphMatrix[index2][index1] != -1;
    }

    private boolean checkEdge(int index1, int index2, int edgeLength) {
        return graphMatrix[index1][index2] == edgeLength
                && graphMatrix[index2][index1] == edgeLength;
    }

    private boolean addEdge(int index1, int index2, int edgeLength) {
        graphMatrix[index1][index2] = edgeLength;
        graphMatrix[index2][index1] = edgeLength;
        return true;
    }

    /**
     * Add an edge of the graph
     *
     * @param e the edge to add to the graph
     * @return true if the edge was successfully added and false otherwise
     */
    public boolean addEdge(E e) {
        List<V> vList = edgeToVertex(e);
        if(!vertex(vList)) {
            return false;
        }
        int[] indices = {vertices.indexOf(vList.get(0)), vertices.indexOf(vList.get(1))};
        if(checkEdge(indices[0],indices[1],-1)) {
            return addEdge(indices[0],indices[1],e.length());
        }
        return false;
    }

    /**
     * Check if an edge is part of the graph
     *
     * @param e the edge to check in the graph
     * @return true if e is an edge in the graph and false otherwise
     */
    public boolean edge(E e) {
        List<V> vList = edgeToVertex(e);
        if(!vertex(vList)) {
            return false;
        }
        int[] indices = {vertices.indexOf(vList.get(0)), vertices.indexOf(vList.get(1))};
        return checkEdge(indices[0],indices[1],e.length());
    }

    /**
     * Check if v1-v2 is an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return true of the v1-v2 edge is part of the graph and false otherwise
     */

    public boolean edge(V v1, V v2) {
        if(!vertex(v1) || !vertex(v2)) {
            return false;
        }
        int[] indices = {vertices.indexOf(v1), vertices.indexOf(v2)};
        return checkEdge(indices[0], indices[1]);
    }

    /**
     * Determine the length on an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return the length of the v1-v2 edge if this edge is part of the graph
     */
    public int edgeLength(V v1, V v2) {
        if(!vertex(v1) || !vertex(v2)) {
            return -1;
        }
        return graphMatrix[vertices.indexOf(v1)][vertices.indexOf(v2)];
    }

    /**
     * Obtain the sum of the lengths of all edges in the graph
     *
     * @return the sum of the lengths of all edges in the graph
     */
    public int edgeLengthSum() {
        int sum = 0;
        for(int i = 0; i < maxVertices; i++) {
            for(int j = i; j < maxVertices; j++) {
                if(graphMatrix[i][j] != -1) {
                    sum += graphMatrix[i][j];
                }
            }
        }
        return sum;
    }

    /**
     * Remove an edge from the graph
     *
     * @param e the edge to remove
     * @return true if e was successfully removed and false otherwise
     */
    public boolean remove(E e) {
        if(edge(e)) {
            int[] indices = {vertices.indexOf(e.v1()), vertices.indexOf(e.v2())};
            return addEdge(indices[0],indices[1],-1);
        }
        return false;
    }

    /**
     * Remove a vertex from the graph
     *
     * @param v the vertex to remove
     * @return true if v was successfully removed and false otherwise
     */
    public boolean remove(V v) {
        if(vertex(v)) {
            int index = vertices.indexOf(v);
            Set<V> neighbors = getNeighbours(v).keySet();
            neighbors.forEach((neighbor) -> {
                int indexNeighbor = vertices.indexOf(neighbor);
                addEdge(index, indexNeighbor, -1);
            });
            update(index);
            return vertices.remove(v);
        }
        return false;
    }

    private void update(int index) {
        for(int i = index; i < maxVertices - 1; i++) {
            for(int j = 0; i < index ; j++) {
                graphMatrix[i][j] = graphMatrix[i+1][j];
                graphMatrix[j][i] = graphMatrix[j][i+1];
            }
        }
        //not sure if this will work
        Arrays.fill(graphMatrix, maxVertices-1, maxVertices, -1);
    }


    /**
     * Obtain a set of all vertices in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return a set of all vertices in the graph
     */
    public Set<V> allVertices() {
        Set<V> verticesList = new HashSet<>();
        for (V vertex : vertices){
            Vertex v = vertex.clone();
            verticesList.add((V) v);
        }
        return verticesList;
    }

    /**
     * Obtain a set of all vertices incident on v.
     * Access to this set **should not** permit graph mutations.
     *
     * @param v the vertex of interest
     * @return all edges incident on v
     */
    public Set<E> allEdges(V v) {
        Set<E> edges = new HashSet<>();
        if(vertex(v)) {
            int index = vertices.indexOf(v);
            for(int i=0; i<maxVertices; i++) {
                if(checkEdge(index, i)) {
                    Edge edge = new Edge(vertices.get(i), vertices.get(index), graphMatrix[i][index]);
                    edges.add((E) edge);
                }
            }
        }
        return edges;
    }

    /**
     * Obtain a set of all edges in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return all edges in the graph
     */
    public Set<E> allEdges() {
        Set<E> edges = new HashSet<>();
        for(int i = 0; i < maxVertices; i++) {
            for(int j = i; j < maxVertices; j++) {
                if(graphMatrix[i][j] != -1) {
                    Edge edge = new Edge(vertices.get(i), vertices.get(j), graphMatrix[i][j]);
                    edges.add((E) edge);
                }
            }
        }
        return edges;
    }

    /**
     * Obtain all the neighbours of vertex v.
     * Access to this map **should not** permit graph mutations.
     *
     * @param v is the vertex whose neighbourhood we want.
     * @return a map containing each vertex w that neighbors v and the edge between v and w.
     */
    public Map<V, E> getNeighbours(V v) {
        Map<V, E> neighbours = new HashMap<>();
        Set<E> incidentEdges = allEdges(v);
        for(E edge : incidentEdges) {
            if(!edge.v1().equals(v)) {
                neighbours.put((V) edge.v1().clone(), (E) edge.clone());
            }
            else {
                neighbours.put((V) edge.v2().clone(), (E) edge.clone());
            }
        }
        return neighbours;
    }
}