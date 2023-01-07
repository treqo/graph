package tests;

import graph.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SmokeTests2 {
    MGraph<Vertex, Edge<Vertex>> createCompleteGraph(Vertex[] vertices) {
        Random rng = new Random();

        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();

        int numVertices = vertices.length;

        for (int i = 0; i < numVertices; i++) {
            int id = (i * numVertices) + rng.nextInt(numVertices);
            vertices[i] = new Vertex(id, Integer.toString(id));
            g.addVertex(vertices[i]);
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                int edgeLength = rng.nextInt(100) + 1;
                g.addEdge(new Edge<>(vertices[i], vertices[j], edgeLength));
            }
        }

        return g;
    }

    @Test
    public void testEdgeLengthSum_EmptyGraph() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        assertEquals(0, g.edgeLengthSum());
    }

    @Test
    public void testEdgeLengthSum_SingleEdge() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        g.addVertex(v1);
        g.addVertex(v2);
        var e = new Edge<>(v1, v2);
        g.addEdge(e);
        assertEquals(1, g.edgeLengthSum());
    }

    @Test
    public void testEdgeLengthSum_MultipleEdges() {
        Random rng        = new Random();
        int numVertices   = rng.nextInt(20) + 1;
        Vertex[] vertices = new Vertex[numVertices];

        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();

        for (int i = 0; i < numVertices; i++) {
            int id = (i * numVertices) + rng.nextInt(numVertices);
            vertices[i] = new Vertex(id, Integer.toString(id));
            g.addVertex(vertices[i]);
        }

        int edgeLengthSum = 0;
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                int edgeLength = rng.nextInt(100) + 1;
                edgeLengthSum += edgeLength;
                g.addEdge(new Edge<>(vertices[i], vertices[j], edgeLength));
            }
        }

        assertEquals(edgeLengthSum, g.edgeLengthSum());
    }

    @Test
    public void testRemoveVertex() {
        int numVertices = 15;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);
        g.remove(vertices[0]);
        assertFalse(g.vertex(vertices[0]));
        for (int i = 1; i < numVertices; i++) {
            assertFalse(g.edge(vertices[0], vertices[i]));
        }
        assertTrue(g.vertex(vertices[1]));
        for (int i = 2; i < numVertices; i++) {
            assertTrue(g.edge(vertices[i], vertices[1]));
        }
    }

    @Test
    public void testRemoveEdge() {
        int numVertices = 15;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);
        g.remove(new Edge<Vertex>(vertices[1], vertices[5]));
        assertFalse(g.edge(vertices[1], vertices[5]));
        assertTrue(g.vertex(vertices[1]));
        assertTrue(g.vertex(vertices[5]));
        assertTrue(g.edgeLengthSum() > 0);
    }

    @Test
    public void testAllVertices() {
        Random rng            = new Random();
        int numVertices       = rng.nextInt(20) + 1;
        TestVertex[] vertices = new TestVertex[numVertices];
        Set<TestVertex> vSet  = new HashSet<>();

        MGraph<TestVertex, Edge<TestVertex>> g = new ALGraph<>();

        for (int i = 0; i < numVertices; i++) {
            int id = (i * numVertices) + rng.nextInt(numVertices);
            vertices[i] = new TestVertex(id, Integer.toString(id));
            vSet.add(vertices[i]);
            g.addVertex(vertices[i]);
        }

        assertEquals(vSet, g.allVertices());

        g.remove(vertices[0]);
        assertTrue(vSet.remove(vertices[0]));
        assertEquals(vSet, g.allVertices());
    }

    @Test
    public void testAllEdges() {
        int numVertices = 16;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);
        assertEquals((16 * 15)/2, g.allEdges().size());
        g.remove(new Edge<Vertex>(vertices[1], vertices[5]));
        assertEquals((16 * 15)/2 - 1, g.allEdges().size());
    }

    @Test
    public void testAllEdges_Immutability() {
        int numVertices = 16;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);

        var edgeSet = g.allEdges();
        try {
            edgeSet.remove(new Edge<>(vertices[2], vertices[10]));
            assertEquals((16 * 15) / 2, g.allEdges().size());
        }
        catch (Exception e) {
            // using an immutable set type?
            // should be okay ... nothing more to do
        }
    }

    @Test
    public void testAllIncidentEdges() {
        int numVertices = 16;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);
        var eSet = g.allEdges(vertices[7]);
        assertEquals(15, eSet.size());
        assertTrue(eSet.contains(new Edge<>(vertices[7], vertices[9])));
    }

    @Test
    public void testAllEdges_EmptyGraph() {
        var g = new ALGraph<Vertex, Edge<Vertex>>();
        assertEquals(0, g.allEdges().size());
    }

    @Test
    public void testAllVertices_EmptyGraph() {
        var g = new ALGraph<Vertex, Edge<Vertex>>();
        assertEquals(0, g.allVertices().size());

    }

    @Test
    public void testGetNeighbours() {
        int numVertices = 16;
        Vertex[] vertices = new Vertex[numVertices];
        var g = createCompleteGraph(vertices);
        var neighbourhood = g.getNeighbours(vertices[10]);
        assertEquals(15, neighbourhood.size());
        assertEquals(new Edge<>(vertices[10], vertices[4]), neighbourhood.get(vertices[4]));
        assertFalse(neighbourhood.containsKey(vertices[10]));
    }

    private class TestVertex extends Vertex {
        public TestVertex(int id, String name) {
            super(id, name);
        }
    }
}
