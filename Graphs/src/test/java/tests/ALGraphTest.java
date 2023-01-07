package tests;

import graph.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ALGraphTest {

    private static Vertex v1, v2, v3, v4, v5;
    private static Edge e1, e2, e3, e4;
    private static ALGraph graph1;

    @BeforeAll
    public static void setup() {
        v1 = new Vertex(1, "A");
        v2 = new Vertex(2, "B");
        v3 = new Vertex(3, "C");
        v4 = new Vertex(4, "D");
        v5 = new Vertex(5, "E");

        e1 = new Edge<>(v1, v2, 5);
        e2 = new Edge<>(v2, v3, 7);
        e3 = new Edge<>(v1, v4, 9);
        e4 = new Edge<>(v4, v5, 3);

        graph1 = new ALGraph<>();
        graph1.addVertex(v1);
        graph1.addVertex(v2);
        graph1.addVertex(v3);
        graph1.addVertex(v4);
        graph1.addVertex(v5);

        graph1.addEdge(e1);
        graph1.addEdge(e2);
        graph1.addEdge(e3);
        graph1.addEdge(e4);

    }

    @Test
    public void test1() {
        assertEquals(24, graph1.edgeLengthSum());
        assertEquals(5, graph1.edgeLength(v1,v2));
        assertEquals(-1, graph1.edgeLength(v1,v3));
        //assertTrue();

    }
}
