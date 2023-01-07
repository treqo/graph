package tests;

import graph.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SmokeTests {
    @Test
    public void testAddVertexAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        Vertex v = new Vertex(1, "Neo");
        assertTrue(g.addVertex(v));
    }

    @Test
    public void testAddMultipleVerticesAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2, "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        for (var v: vertices) {
            assertTrue(g.addVertex(v));
        }
    }

    @Test
    public void testAddDuplicateVertices1AL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(1, "Neo");
        g.addVertex(v1);
        assertFalse(g.addVertex(v2));
    }

    @Test
    public void testContainsVertexAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2,  "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        for (var v: vertices) {
            g.addVertex(v);
        }
        for (var v: vertices) {
            assertTrue(g.vertex(v));
        }
    }

    @Test
    public void testDoesNotContainsVertexAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2,  "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        var v5 = new Vertex(5, "Niobe");
        for (var v: vertices) {
            g.addVertex(v);
        }
        assertFalse(g.vertex(v5));
    }

//    @Test
//    @Disabled
//    public void testAddDuplicateVertices2() {
//        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
//        var v1 = new Vertex(1, "Neo");
//        var v2 = new Vertex(1, "Morpheus");
//        g.addVertex(v1);
//        assertFalse(g.addVertex(v2));
//    }

    @Test
    public void testAddEdgeAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        g.addVertex(v1);
        g.addVertex(v2);
        var e = new Edge<>(v1, v2);
        assertTrue(g.addEdge(e));
    }

    @Test
    public void testAddBadEdgeAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        var e = new Edge<>(v1, v3);
        assertFalse(g.addEdge(e));
    }

    @Test
    public void testContainsEdgeAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e = new Edge<>(v1, v2);
        g.addEdge(e);
        assertTrue(g.edge(new Edge(v1, v2)));
    }

    @Test
    public void testDoesNotContainsEdgeAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e = new Edge<>(v1, v2);
        g.addEdge(e);
        assertFalse(g.edge(new Edge(v1, v3)));
    }

    @Test
    public void testEdgeLengthAL() {
        MGraph<Vertex, Edge<Vertex>> g = new ALGraph<>();
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e1 = new Edge<>(v1, v2, 3);
        var e2 = new Edge<>(v1, v3);
        g.addEdge(e1);
        g.addEdge(e2);
        assertEquals(3, g.edgeLength(v1, v2));
        assertEquals(1, g.edgeLength(v1, v3));
    }

    /**
     * AMGraph
     */

    @Test
    public void testAddVertexAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        Vertex v = new Vertex(1, "Neo");
        assertTrue(g.addVertex(v));
    }

    @Test
    public void testAddMultipleVerticesAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2, "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        for (var v: vertices) {
            assertTrue(g.addVertex(v));
        }
    }

    @Test
    public void testAddDuplicateVertices1AM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(1, "Neo");
        g.addVertex(v1);
        assertFalse(g.addVertex(v2));
    }

    @Test
    public void testContainsVertexAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2,  "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        for (var v: vertices) {
            g.addVertex(v);
        }
        for (var v: vertices) {
            assertTrue(g.vertex(v));
        }
    }

    @Test
    public void testDoesNotContainsVertexAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var vertices = new Vertex[] {
                new Vertex(1, "Neo"),
                new Vertex(2,  "Morpheus"),
                new Vertex(3, "Trinity"),
                new Vertex(4, "Neo")
        };
        var v5 = new Vertex(5, "Niobe");
        for (var v: vertices) {
            g.addVertex(v);
        }
        assertFalse(g.vertex(v5));
    }

//    @Test
//    @Disabled
//    public void testAddDuplicateVertices2() {
//        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
//        var v1 = new Vertex(1, "Neo");
//        var v2 = new Vertex(1, "Morpheus");
//        g.addVertex(v1);
//        assertFalse(g.addVertex(v2));
//    }

    @Test
    public void testAddEdgeAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        g.addVertex(v1);
        g.addVertex(v2);
        var e = new Edge<>(v1, v2);
        assertTrue(g.addEdge(e));
    }

    @Test
    public void testAddBadEdgeAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        var e = new Edge<>(v1, v3);
        assertFalse(g.addEdge(e));
    }

    @Test
    public void testContainsEdgeAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e = new Edge<>(v1, v2);
        g.addEdge(e);
        assertTrue(g.edge(new Edge(v1, v2)));
    }

    @Test
    public void testDoesNotContainsEdgeAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e = new Edge<>(v1, v2);
        g.addEdge(e);
        assertFalse(g.edge(new Edge(v1, v3)));
    }

    @Test
    public void testEdgeLengthAM() {
        MGraph<Vertex, Edge<Vertex>> g = new AMGraph<>(10);
        var v1 = new Vertex(1, "Neo");
        var v2 = new Vertex(2, "Morpheus");
        var v3 = new Vertex(3, "Trinity");
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        var e1 = new Edge<>(v1, v2, 3);
        var e2 = new Edge<>(v1, v3);
        g.addEdge(e1);
        g.addEdge(e2);
        assertEquals(3, g.edgeLength(v1, v2));
        assertEquals(1, g.edgeLength(v1, v3));
    }

}
