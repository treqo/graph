package tests;

import graph.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class AMGraphTest {

    Vertex v1 = new Vertex(1, "v1");
    Vertex v2 = new Vertex(2, "v2");
    Vertex v3 = new Vertex(3, "v3");
    Vertex v4 = new Vertex(4, "v4");
    Vertex v5 = new Vertex(5, "v5");
    Vertex v6 = new Vertex(6, "v6");

    @Test
    void testAddVertex() {
        AMGraph<Vertex, Edge<Vertex>> graph = new AMGraph<>(5);

        assertTrue(graph.addVertex(v1));
        assertTrue(graph.addVertex(v2));
        assertTrue(graph.addVertex(v3));
        assertTrue(graph.addVertex(v4));
        assertTrue(graph.addVertex(v5));
        assertFalse(graph.addVertex(v6)); // should return false because the graph is full
        assertFalse(graph.addVertex(v1)); //should return false because already in te graph

        assertTrue(graph.vertex(v1));
        assertTrue(graph.vertex(v2));
        assertTrue(graph.vertex(v3));
        assertTrue(graph.vertex(v4));
        assertTrue(graph.vertex(v5));
        assertFalse(graph.vertex(v6)); // should return false because v6 was not added to the graph
    }

    @Test
    void testAddEdge() {
        AMGraph<Vertex, Edge<Vertex>> graph = new AMGraph<>(5);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 2);
        Edge<Vertex> e3 = new Edge<>(v1, v3, 3);
        Edge<Vertex> e4 = new Edge<>(v3, v1, 4);

        assertTrue(graph.addEdge(e1));
        assertTrue(graph.addEdge(e2));
        assertTrue(graph.addEdge(e3));
        assertFalse(graph.addEdge(e4)); // should return false because e4 has the same vertices as e3 but different length

        assertTrue(graph.edge(e1));
        assertTrue(graph.edge(v1, v2));
        assertFalse(graph.edge(v4, v1)); //should return false because v4 not connected to anything
        assertTrue(graph.edge(e2));
        assertTrue(graph.edge(e3));
        assertFalse(graph.edge(e4)); // should return false because e4 was not added to the graph

        assertEquals(1, graph.edgeLength(v1, v2));
        assertFalse(4 == graph.edgeLength(v1, v3));

        assertEquals(6, graph.edgeLengthSum());
    }

    @Test
    void testGetNeighbors() {
        AMGraph<Vertex, Edge<Vertex>> graph = new AMGraph<>(5);

        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 2);
        Edge<Vertex> e3 = new Edge<>(v1, v3, 3);
        Edge<Vertex> e4 = new Edge<>(v3, v4, 4);
        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);
        graph.addEdge(e4);

        Map<Vertex, Edge<Vertex>> neighbors1 = graph.getNeighbours(v1);

        assertEquals(2, neighbors1.size());
        assertTrue(neighbors1.containsKey(v2));
        assertTrue(neighbors1.containsKey(v3));
        assertTrue(neighbors1.get(v2).equals(e1));
        assertTrue(neighbors1.get(v3).equals(e3));
    }

    @Test
    void testAllVertices() {
        AMGraph<Vertex, Edge<Vertex>> graph = new AMGraph<>(5);
        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        Set<Vertex> allVertices = graph.allVertices();
        assertEquals(4, allVertices.size());
        assertTrue(allVertices.contains(v1));
        assertTrue(allVertices.contains(v2));
        assertTrue(allVertices.contains(v3));
    }
}
