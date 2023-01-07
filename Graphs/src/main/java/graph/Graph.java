package graph;

import java.util.*;

/**
 * Represents a graph with vertices of type V.
 *
 * @param <V> represents a vertex type
 */
public class Graph<V extends Vertex, E extends Edge<V>> implements ImGraph<V, E>, MGraph<V, E> {
    final private ALGraph<V, E> graph;
    // Rep Invariant:
    //          An augmented list graph made up of weighted edges and vertices.
    // Abstract Function:
    //          Represents an undirected Graph.

    public Graph(){
        graph = new ALGraph<>();
    }
    public Graph(ALGraph<V,E> alGraph){
        graph = alGraph;
    }

    /**
     * Find the edge that connects two vertices. (An edge between v1 and v2 has to exist)
     * This method should not permit graph mutations.
     *
     * @param v1 one end of the edge (edge(v1,v2) == true)
     * @param v2 the other end of the edge (edge(v1,v2) == true)
     * @return the edge connecting v1 and v2
     */
    public E getEdge(V v1, V v2){
        E edge = null;
        Set<E> edges = allEdges(v1);
        for (E e : edges){
            if (e.v1().equals(v1) || e.v2().equals(v1)){
                if (e.v2().equals(v2) || e.v1().equals(v2)){
                    edge = e;
                }
            }
        }
        return edge;//if spec is followed edge will never return null
    }

    /**
     * Compute the shortest path in the graph from source to sink
     *
     * Code inspired from: https://www.youtube.com/watch?v=BuvKtCh0SKk&ab_channel=Geekific
     *
     * @param source the start vertex
     * @param sink   the end vertex
     * @return the vertices, in order, on the shortest path from source to sink (both end points are part of the list),
     *          if no such path exists it returns an empty list. If source is the same as the sink returns a list
     *          with only one Vertex.
     */
    public List<V> shortestPath(V source, V sink){
        List<V> shortest = new ArrayList<>();

        if (allEdges(source).isEmpty() || allEdges(sink).isEmpty() || !vertex(source) || !vertex(sink)){
            return shortest;
        }

        Map<V, V> shortestPath = new HashMap<>();
        Map<V, Integer> shortestLengths = new HashMap<>();
        Set<V> allVertices = allVertices();
        for (V v : allVertices){
            shortestPath.put(v, v);
            shortestLengths.put(v, Integer.MAX_VALUE);
        }
        shortestLengths.replace(source,0);

        List<V> unsettledVertices = new LinkedList<>(Collections.singleton(source));
        Set<V> settledVertices = new HashSet<>();

        while (!unsettledVertices.isEmpty()){
            int unsettledShortest = Integer.MAX_VALUE;
            int unsettledShortestIndex = 0;
            for (int i = 0; i < unsettledVertices.size(); i++){
                if (shortestLengths.get(unsettledVertices.get(i)) < unsettledShortest){
                    unsettledShortest = shortestLengths.get(unsettledVertices.get(i));
                    unsettledShortestIndex = i;
                }
            }//makes sure it checks the shortest length first
            V currentVertex = unsettledVertices.remove(unsettledShortestIndex);

            getNeighbours(currentVertex).entrySet()
                    .stream()
                    .filter(v -> !settledVertices.contains(v.getKey()))
                    .forEach(v -> {
                        V lilCurrent = v.getKey();
                        int distance = shortestLengths.get(currentVertex) + edgeLength(lilCurrent, currentVertex);

                        if (distance < shortestLengths.get(lilCurrent)){
                            shortestLengths.replace(lilCurrent, distance);
                            shortestPath.replace(lilCurrent, currentVertex);
                        }
                        unsettledVertices.add(lilCurrent);
                    });
            settledVertices.add(currentVertex);
        }//uses dijkstra's algorithm to create a map of shortests paths.

        V currentVertex = sink;
        do{
            if (!shortestPath.get(currentVertex).equals(currentVertex)){
                shortest.add(currentVertex);
                currentVertex = shortestPath.get(currentVertex);
            } else {
                break;
            }
        } while (!currentVertex.equals(source));
        shortest.add(source);

        Collections.reverse(shortest);

        return shortest;
    }

    /**
     * Compute the length of a given path
     *
     * @param path indicates the vertices on the given path
     * @return the length of path
     *          returns Integer.MAX_VALUE if list is empty
     */
    public int pathLength(List<V> path){
        int pathSum = 0;

        for (int i = 0; i < path.size() - 1; i++){
            pathSum += edgeLength(path.get(i), path.get(i + 1));
        }

        if (path.isEmpty()){
            return Integer.MAX_VALUE;
        } else {
            return pathSum;
        }
    }

    /**
     * Obtain all vertices w that are no more than a <em>path distance</em> of range from v.
     *
     * @param v     the vertex to start the search from.
     * @param range the radius of the search.
     * @return a map where the keys are the vertices in the neighbourhood of v,
     *          and the value for key w is the last edge on the shortest path
     *          from v to w.
     */
    public Map<V, E> getNeighbours(V v, int range){
        Map<V,E> neighbours = getNeighbours(v);

        neighbours.entrySet().removeIf(vertex ->
                pathLength(shortestPath(v, vertex.getKey())) > range
        );

        return neighbours;
    }

    /**
     * Return a set with k connected components of the graph.
     *
     * inspired from: https://www.youtube.com/watch?v=jsmMtJpPnhU&ab_channel=WilliamFiset
     *
     * <ul>
     * <li>When k = 1, the method returns one graph in the set, and that graph
     * represents the minimum spanning tree of the graph.
     * See: https://en.wikipedia.org/wiki/Minimum_spanning_tree</li>
     *
     * <li>When k = n, where n is the number of vertices in the graph, then
     * the method returns a set of n graphs, and each graph contains a
     * unique vertex and no edge.</li>
     *
     * <li>When k is in [2, n-1], the method partitions the graph into k connected sub-graphs
     * such that for any two vertices V_i and V_j, if vertex V_i is in subgraph
     * G_a and vertex V_j is in subgraph G_b (a != b), and there is an edge
     * between V_i and V_j, and |G_a| > 1, then there must exist some vertex V_k in G_a such
     * that the length of the edge between V_i and V_k is at most the length
     * of the edge between V_i and V_j.</li>
     * </ul>
     *
     * @return a set of graph partitions such that a vertex in one partition
     * is no closer to a vertex in a different partition than it is to a vertex
     * in its own partition.
     * However, if components of original graph is greater than k, returns null.
     */

    public Set<ImGraph<V, E>> minimumSpanningComponents(int k) {
        Set<ImGraph<V, E>> msc = new HashSet<>();
        List<ALGraph<V,E>> components = new ArrayList<>();
        components = splitIntoComponents(graph);
        Comparator<E> descending_edge_comparator = new Comparator<E>() { //descending (large to small)
            @Override
            public int compare(E e1, E e2) {
                return e2.length() - e1.length();
            }
        };

        if (components.size() <= k) {
            List<ALGraph<V, E>> mst = new ArrayList<>();
            Set<E> all_MSC_Edges = new HashSet<>();
            components.forEach(g -> {
                if(g.allEdges().size() > 0) {
                    ALGraph<V,E> mst_component = minimumSpanningTree(g);
                    all_MSC_Edges.addAll(mst_component.allEdges());
                    mst.add(mst_component);
                }
                else{mst.add(g);}
            });

            //creates a priority queue of all edges in descending order
            PriorityQueue<E> edges_pq = new PriorityQueue<>(all_MSC_Edges.size(), descending_edge_comparator);
            all_MSC_Edges.forEach(e -> edges_pq.add(e));

            for (int i = mst.size(); i < k; i++) {
                E head = edges_pq.poll();
                mst.forEach(g -> {
                    g.remove(head);
                });
            }

            mst.forEach(g -> {
                splitIntoComponents(g).forEach(c -> {
                    ImGraph<V,E> mst_component = new Graph(c);
                    msc.add(mst_component);
                });
            });
            return msc;
        }
        return null;
    }

    private List<ALGraph<V,E>> splitIntoComponents(ALGraph<V,E> undivided_graph) {
        List<ALGraph<V,E>> components = new ArrayList<>();
        List<V> graph_vertices = new ArrayList<V>(undivided_graph.allVertices());
        while(graph_vertices.size() != 0) {
            //reseting edges and vertex set (fields)
            edgesSet = new HashSet<E>();
            vertexSet = new HashSet<V>();
            ALGraph<V,E> component_graph = new ALGraph<>();

            //calls a recursive depth first search method with the first index of vertices list
            dFSRecursion(graph_vertices.get(0), undivided_graph);

            //defining component_graph using vertexset and edgesset
            //and adding to components (list of MGraphs – components of original graph)
            vertexSet.forEach(v -> {
                component_graph.addVertex(v);
            });
            edgesSet.forEach(e -> {
                component_graph.addEdge(e);
            });
            components.add(component_graph);

            //remove used vertices from original vertices set
            graph_vertices.removeAll(vertexSet);
        }
        return components;
    }

    private Set<E> edgesSet;
    private Set<V> vertexSet;

    private void dFSRecursion(V current, MGraph<V,E> instance) {
        vertexSet.add(current);
        Set<E> current_edges = instance.allEdges(current);

        current_edges.forEach(e -> {
            if(!edgesSet.contains(e)) {
                edgesSet.add(e);
                if(!e.v1().equals(current)) {
                    dFSRecursion(e.v1(), instance);
                }
                else {
                    dFSRecursion(e.v2(), instance);
                }
            }
        });
    }

    private ALGraph<V,E> minimumSpanningTree(ALGraph<V,E> component) {

        Comparator<E> ascending_edge_comparator = new Comparator<E>() { //ascending (small to large)
            @Override
            public int compare(E e1, E e2) {
                return e1.length() - e2.length();
            }
        };

        int n = component.allVertices().size();
        int m = n-1;

        List<V> vertices = new ArrayList<>(component.allVertices());

        boolean[] visited = new boolean[n];
        Arrays.fill(visited, Boolean.FALSE);

        PriorityQueue<E> edges_pq = new PriorityQueue<E>(component.allEdges().size(), ascending_edge_comparator);
        int edgeCount = 0;
        Set<E> mstEdges = new HashSet<>();

        int current_node = 0;

        addEdge(current_node, visited, vertices, edges_pq);

        while(edges_pq.size() > 0 && edgeCount != m) {
            int next_node;
            E edge = edges_pq.poll();
            if(!edge.v1().equals(vertices.get(current_node))) {
                next_node = vertices.indexOf(edge.v1());
            }
            else {
                next_node = vertices.indexOf(edge.v2());
            }

            if(visited[next_node]) {continue;}

            mstEdges.add(edge);
            current_node = next_node;
            addEdge(current_node, visited, vertices, edges_pq);
            edgeCount++;
        }

        ALGraph<V,E> msc = new ALGraph<>();

        vertices.forEach(v -> msc.addVertex(v));
        mstEdges.forEach(e -> msc.addEdge(e));

        return msc;
    }

    private void addEdge(int index, boolean[] visited, List<V> vertices, PriorityQueue<E> edges_pq) {
        V node = vertices.get(index);
        visited[index] = true;
        Map<V,E> neighbours = graph.getNeighbours(node);
        neighbours.forEach((v,e) -> {
            if(!visited[vertices.indexOf(v)]) {
                edges_pq.add(e);
            }
        });
    }

    /**
     * Compute the diameter of the graph.
     * <ul>
     * <li>The diameter of a graph is the length of the longest shortest path in the graph.</li>
     * <li>If a graph has multiple components then we will define the diameter
     * as the diameter of the largest component.</li>
     * </ul>
     *
     * @return the diameter of the graph.
     */
    public int diameter(){
        int diameter = 0;
        int maxLength = 0;

        for (V vertex1 : allVertices()){
            for (V vertex2 : allVertices()){
                if (!vertex1.equals(vertex2)){
                    List<V> current = shortestPath(vertex1, vertex2);
                    int currentSize = current.size();
                    if (currentSize > maxLength){
                        diameter = pathLength(current);
                        maxLength = currentSize;
                    }
                }
            }
        }

        return diameter;
    }

    /**
     * Compute the center of the graph.
     *
     * <ul>
     * <li>For a vertex s, the eccentricity of s is defined as the maximum distance
     * between s and any other vertex t in the graph.</li>
     *
     * <li>The center of a graph is the vertex with minimum eccentricity.</li>
     *
     * <li>If a graph is not connected, we will define the graph's center to be the
     * center of the largest connected component.</li>
     * </ul>
     *
     * @return the center of the graph.
     */
    public V getCenter(){
        V vertex = null;
        int minEccentricity = Integer.MAX_VALUE;

        for (V vertex1 : allVertices()){
            int currentMax = 0;

            for (V vertex2 : allVertices()){
                if (!vertex1.equals(vertex2)){
                    int current;
                    List<V> currentPath = shortestPath(vertex1, vertex2);
                    current = pathLength(currentPath);
                    if (current > currentMax){
                        currentMax = current;
                    }
                }
            }

            if (currentMax < minEccentricity){
                minEccentricity = currentMax;
                vertex = vertex1;
            }
        }

        return vertex;
    }

    /**
     * Add a vertex to the graph
     *
     * @param v vertex to add
     * @return true if the vertex was added successfully and false otherwise
     */
    public boolean addVertex(V v){
        return graph.addVertex(v);
    }

    /**
     * Check if a vertex is part of the graph
     *
     * @param v vertex to check in the graph
     * @return true of v is part of the graph and false otherwise
     */
    public boolean vertex(V v){
        return graph.vertex(v);
    }

    /**
     * Add an edge of the graph
     *
     * @param e the edge to add to the graph
     * @return true if the edge was successfully added and false otherwise
     */
    public boolean addEdge(E e) {
        return graph.addEdge(e);
    }

    /**
     * Check if an edge is part of the graph
     *
     * @param e the edge to check in the graph
     * @return true if e is an edge in the graph and false otherwise
     */
    public boolean edge(E e){
        return graph.edge(e);
    }

    /**
     * Check if v1-v2 is an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return true of the v1-v2 edge is part of the graph and false otherwise
     */
    public boolean edge(V v1, V v2){
        return graph.edge(v1, v2);
    }

    /**
     * Determine the length on an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return the length of the v1-v2 edge if this edge is part of the graph or -1 otherwise
     */
    public int edgeLength(V v1, V v2){
        return graph.edgeLength(v1, v2);
    }

    /**
     * Obtain the sum of the lengths of all edges in the graph
     *
     * @return the sum of the lengths of all edges in the graph
     */
    public int edgeLengthSum(){
        return graph.edgeLengthSum();
    }

    /**
     * Remove an edge from the graph
     *
     * @param e the edge to remove
     * @return true if e was successfully removed and false otherwise
     */
    public boolean remove(E e){
        return graph.remove(e);
    }

    /**
     * Remove a vertex from the graph
     *
     * @param v the vertex to remove
     * @return true if v was successfully removed and false otherwise
     */
    public boolean remove(V v){
        return graph.remove(v);
    }

    /**
     * Obtain a set of all vertices in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return a set of all vertices in the graph
     */
    public Set<V> allVertices(){
        return graph.allVertices();
    }

    /**
     * Obtain a set of all edges incident on v.
     * Access to this set **should not** permit graph mutations.
     *
     * @param v the vertex of interest
     * @return all edges incident on v
     */
    public Set<E> allEdges(V v){
        return graph.allEdges(v);
    }

    /**
     * Obtain a set of all edges in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return all edges in the graph
     */
    public Set<E> allEdges(){
        return graph.allEdges();
    }

    /**
     * Obtain all the neighbours of vertex v.
     * Access to this map **should not** permit graph mutations.
     *
     * @param v is the vertex whose neighbourhood we want.
     * @return a map containing each vertex w that neighbors v and the edge between v and w.
     */
    public Map<V, E> getNeighbours(V v){
        return graph.getNeighbours(v);
    }


    //// add all new code above this line ////

    /**
     * This method removes some edges at random while preserving connectivity
     * <p>
     * DO NOT CHANGE THIS METHOD
     * </p>
     * <p>
     * You will need to implement allVertices() and allEdges(V v) for this
     * method to run correctly
     *</p>
     * <p><strong>requires:</strong> this graph is connected</p>
     *
     * @param rng random number generator to select edges at random
     */
    public void pruneRandomEdges(Random rng) {
        class VEPair {
            V v;
            E e;

            public VEPair(V v, E e) {
                this.v = v;
                this.e = e;
            }
        }

        /* Visited Nodes */
        Set<V> visited = new HashSet<>();
        /* Nodes to visit and the cpen221.mp2.graph.Edge used to reach them */
        Deque<VEPair> stack = new LinkedList<VEPair>();
        /* Edges that could be removed */
        ArrayList<E> candidates = new ArrayList<>();
        /* Edges that must be kept to maintain connectivity */
        Set<E> keep = new HashSet<>();

        V start = null;
        for (V v : this.allVertices()) {
            start = v;
            break;
        }
        if (start == null) {
            // nothing to do
            return;
        }
        stack.push(new VEPair(start, null));
        while (!stack.isEmpty()) {
            VEPair pair = stack.pop();
            if (visited.add(pair.v)) {
                keep.add(pair.e);
                for (E e : this.allEdges(pair.v)) {
                    stack.push(new VEPair(e.distinctVertex(pair.v), e));
                }
            } else if (!keep.contains(pair.e)) {
                candidates.add(pair.e);
            }
        }
        // randomly trim some candidate edges
        int iterations = rng.nextInt(candidates.size());
        for (int count = 0; count < iterations; ++count) {
            int end = candidates.size() - 1;
            int index = rng.nextInt(candidates.size());
            E trim = candidates.get(index);
            candidates.set(index, candidates.get(end));
            candidates.remove(end);
            remove(trim);
        }
    }
}