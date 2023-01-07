package tests;

import graph.*;
import org.junit.jupiter.api.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    private static List<String> alphabet = List.of(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    );

    /* Graph 0 */
    private static List<Vertex> lv0 = List.of(
            new Vertex(1, "A"),
            new Vertex(2, "B"),
            new Vertex(3, "C"),
            new Vertex(4, "D")
    );
    private static List<Edge<Vertex>> le0 = List.of(
            new Edge<>(lv0.get(0), lv0.get(1), 5),
            new Edge<>(lv0.get(1), lv0.get(2), 7),
            new Edge<>(lv0.get(0), lv0.get(3), 9)
    );

    /* Graph 1 (No connection) */
    private static List<Edge<Vertex>> le1 = List.of(
            new Edge<>(lv0.get(0), lv0.get(1), 5),
            new Edge<>(lv0.get(2), lv0.get(3), 7)
    );

    /* Full Alphabet vertices */
    private static Map<String, Vertex> lv1 = IntStream.range(0, 26)
            .mapToObj(a -> new AbstractMap.SimpleEntry<>(
                    alphabet.get(a), new Vertex(a, alphabet.get(a))
            ))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    /*                 Graph 2
     *
     *                    D
     *  +V----------------+--+
     *  |                 |  E
     *  |                 |
     * U+-----+S         B+-----+
     *  |     |    Q      |     C        +G
     *  +T   R+----+      |              |
     *        |    |      |              |
     *        |    +------+---------+F   |
     *    Y   |    P      A         |    |
     *    +---+X-------------------W+----+H
     *    |   |                     |    |
     *    +   +-------+N----K+------+J   |
     *    Z   O       |      |           |
     *                |      |           +I
     *                |      |
     *                +------+L
     *                M
     */
    private static List<Edge<Vertex>> le2 = List.of(
            new Edge<>(lv1.get("A"), lv1.get("P"), 6),
            new Edge<>(lv1.get("A"), lv1.get("B"), 3),
            new Edge<>(lv1.get("A"), lv1.get("F"), 9),
            new Edge<>(lv1.get("B"), lv1.get("D"), 4),
            new Edge<>(lv1.get("B"), lv1.get("C"), 5),
            new Edge<>(lv1.get("D"), lv1.get("E"), 7),
            new Edge<>(lv1.get("D"), lv1.get("V"), 15),
            new Edge<>(lv1.get("F"), lv1.get("W"), 2),
            new Edge<>(lv1.get("G"), lv1.get("H"), 43),
            new Edge<>(lv1.get("H"), lv1.get("I"), 8),
            new Edge<>(lv1.get("J"), lv1.get("W"), 1),
            new Edge<>(lv1.get("J"), lv1.get("K"), 10),
            new Edge<>(lv1.get("K"), lv1.get("L"), 11),
            new Edge<>(lv1.get("L"), lv1.get("M"), 13),
            new Edge<>(lv1.get("M"), lv1.get("N"), 14),
            new Edge<>(lv1.get("N"), lv1.get("O"), 16),
            new Edge<>(lv1.get("N"), lv1.get("K"), 19),
            new Edge<>(lv1.get("O"), lv1.get("X"), 18),
            new Edge<>(lv1.get("P"), lv1.get("Q"), 22),
            new Edge<>(lv1.get("Q"), lv1.get("R"), 23),
            new Edge<>(lv1.get("R"), lv1.get("S"), 24),
            new Edge<>(lv1.get("R"), lv1.get("X"), 25),
            new Edge<>(lv1.get("S"), lv1.get("U"), 26),
            new Edge<>(lv1.get("T"), lv1.get("U"), 27),
            new Edge<>(lv1.get("U"), lv1.get("V"), 28),
            new Edge<>(lv1.get("W"), lv1.get("H"), 29),
            new Edge<>(lv1.get("X"), lv1.get("Y"), 30),
            new Edge<>(lv1.get("X"), lv1.get("W"), 42),
            new Edge<>(lv1.get("Y"), lv1.get("Z"), 40)
    );

    private static Graph<Vertex, Edge<Vertex>> g0 = formGraph(lv0, le0);
    private static Graph<Vertex, Edge<Vertex>> g1 = formGraph(lv0, le1);
    private static Graph<Vertex, Edge<Vertex>> g2 = new Graph<>();
    private static Graph<Vertex, Edge<Vertex>> g3 = formGraph(new ArrayList<>(lv1.values()), le2);
    private static Graph<Vertex, Edge<Vertex>> g4 = formGraph(new ArrayList<>(lv1.values()), le2);
    private static Graph<Vertex, Edge<Vertex>> g5 = formGraph(new ArrayList<>(lv1.values()), le2);

    static {
        g4.remove(new Edge<Vertex>(lv1.get("W"), lv1.get("H")));
        g5.remove(new Edge<Vertex>(lv1.get("W"), lv1.get("H")));
        g5.remove(new Edge<Vertex>(lv1.get("O"), lv1.get("X")));
        g5.remove(new Edge<Vertex>(lv1.get("W"), lv1.get("J")));
    }

    private static Graph<Vertex, Edge<Vertex>> formGraph(List<Vertex> lv, List<Edge<Vertex>> le) {
        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        lv.forEach(g::addVertex);
        le.forEach(g::addEdge);
        return g;
    }

    /**
     * Argument provider
     *
     * @return Graph to test, expected
     */
//    private static Stream<Arguments> minimumSpanningComponentsProvider() {
//        /* A<->F, D<->V, L<->M, X<->W */
//
//        Set<Edge<Vertex>> g3Excluded = Set.of(le2.get(16), le2.get(21), le2.get(24), le2.get(27));
//        Graph<Vertex, Edge<Vertex>> g3_MST = formGraph(new ArrayList<>(lv1.values()), le2);
//        for (Edge<Vertex> e: g3Excluded) {
//            g3_MST.remove(e);
//        }
//
//        Graph<Vertex, Edge<Vertex>> g4_NoEdges = formGraph(new ArrayList<>(lv1.values()), new ArrayList<>());
//        var g4Components = new HashSet<Graph<Vertex, Edge<Vertex>>>();
//        for (Vertex v: lv1.values()) {
//            g4Components.add(
//                new Graph<>() {{ addVertex(v); }}
//            );
//        }
//
//        return Stream.of(
//            Arguments.of(g2, Set.of(), 1),
//            Arguments.of(g3, Set.of(g3_MST), 1),
//            Arguments.of(g4_NoEdges, g4Components, lv1.values().size())
//        );
//    }
    @Test
    public void testMSC1() {
        var msc1 = g2.minimumSpanningComponents(1);
        assertEquals(0, msc1.size());
    }

    @Test
    public void testMSC2() {
        Set<Edge<Vertex>> g3Excluded = Set.of(le2.get(16), le2.get(21), le2.get(24), le2.get(27));
        Graph<Vertex, Edge<Vertex>> g3_MST = formGraph(new ArrayList<>(lv1.values()), le2);
        for (Edge<Vertex> e: g3Excluded) {
            g3_MST.remove(e);
        }

        var msc2 = g3.minimumSpanningComponents(1);
        assertEquals(1, msc2.size());
        var eSet2 = g3_MST.allEdges();
        for (var comp: msc2) {
            var eSet1 = ((Graph<Vertex, Edge<Vertex>>) comp).allEdges();
            assertEquals(eSet2, eSet1);
        }
    }

    @Test
    public void testMSC3() {
        Graph<Vertex, Edge<Vertex>> g4_NoEdges = formGraph(new ArrayList<>(lv1.values()), new ArrayList<>());
        var g4Components = new HashSet<Graph<Vertex, Edge<Vertex>>>();
        for (Vertex v: lv1.values()) {
            g4Components.add(
                    new Graph<>() {{ addVertex(v); }}
            );
        }

        var msc3 = g4_NoEdges.minimumSpanningComponents(lv1.size());

        assertEquals(lv1.size(), msc3.size());

        for (var component: msc3) {
            assertEquals(1, ((Graph<Vertex, Edge<Vertex>>) component).allVertices().size());
        }

    }
    @Test
    public void testFourComponents() {
        var component1 = new Graph<Vertex, Edge<Vertex>>() {
            {
                addVertex(lv1.get("A"));
                addVertex(lv1.get("Q"));
                addVertex(lv1.get("B"));
                addVertex(lv1.get("R"));
                addVertex(lv1.get("C"));
                addVertex(lv1.get("S"));
                addVertex(lv1.get("T"));
                addVertex(lv1.get("D"));
                addVertex(lv1.get("U"));
                addVertex(lv1.get("E"));
                addVertex(lv1.get("F"));
                addVertex(lv1.get("V"));
                addVertex(lv1.get("W"));
                addVertex(lv1.get("X"));
                addVertex(lv1.get("H"));
                addVertex(lv1.get("I"));
                addVertex(lv1.get("J"));
                addVertex(lv1.get("K"));
                addVertex(lv1.get("L"));
                addVertex(lv1.get("M"));
                addVertex(lv1.get("N"));
                addVertex(lv1.get("O"));
                addVertex(lv1.get("P"));
                addEdge(new Edge<>(lv1.get("A"), lv1.get("P"), 6));
                addEdge(new Edge<>(lv1.get("A"), lv1.get("B"), 3));
                addEdge(new Edge<>(lv1.get("A"), lv1.get("F"), 9));
                addEdge(new Edge<>(lv1.get("P"), lv1.get("Q"), 22));
                addEdge(new Edge<>(lv1.get("Q"), lv1.get("R"), 23));
                addEdge(new Edge<>(lv1.get("B"), lv1.get("D"), 4));
                addEdge(new Edge<>(lv1.get("B"), lv1.get("C"), 5));
                addEdge(new Edge<>(lv1.get("R"), lv1.get("S"), 24));
                addEdge(new Edge<>(lv1.get("B"), lv1.get("C"), 5));
                addEdge(new Edge<>(lv1.get("S"), lv1.get("U"), 26));
                addEdge(new Edge<>(lv1.get("T"), lv1.get("U"), 27));
                addEdge(new Edge<>(lv1.get("D"), lv1.get("E"), 7));
                addEdge(new Edge<>(lv1.get("D"), lv1.get("V"), 15));
                addEdge(new Edge<>(lv1.get("F"), lv1.get("W"), 2));
                addEdge(new Edge<>(lv1.get("J"), lv1.get("W"), 1));
                addEdge(new Edge<>(lv1.get("H"), lv1.get("W"), 29));
                addEdge(new Edge<>(lv1.get("O"), lv1.get("X"), 18));
                addEdge(new Edge<>(lv1.get("H"), lv1.get("I"), 8));
                addEdge(new Edge<>(lv1.get("J"), lv1.get("K"), 10));
                addEdge(new Edge<>(lv1.get("K"), lv1.get("L"), 11));
                addEdge(new Edge<>(lv1.get("L"), lv1.get("M"), 13));
                addEdge(new Edge<>(lv1.get("M"), lv1.get("N"), 14));
                addEdge(new Edge<>(lv1.get("O"), lv1.get("N"), 16));
                addEdge(new Edge<>(lv1.get("O"), lv1.get("X"), 18));
                addEdge(new Edge<>(lv1.get("P"), lv1.get("Q"), 22));
            }
        };

        var component2 = new Graph<Vertex, Edge<Vertex>>() {
            {
                addVertex(lv1.get("Y"));
            }
        };

        var component3 = new Graph<Vertex, Edge<Vertex>>() {
            {
                addVertex(lv1.get("Z"));
            }
        };

        var component4 = new Graph<Vertex, Edge<Vertex>>() {
            {
                addVertex(lv1.get("G"));
            }
        };

        var g3_4Components = new HashSet<Graph<Vertex, Edge<Vertex>>>() {
            {
                add(component1);
                add(component2);
                add(component3);
                add(component4);
            }
        };

        var output = g3.minimumSpanningComponents(4);

        for (var component: output) {
            var c = (Graph<Vertex, Edge<Vertex>>) component;
            if (c.allVertices().size() == 1) {
                assertTrue(
                        c.allVertices().equals(component2.allVertices()) ||
                                c.allVertices().equals(component3.allVertices()) ||
                                c.allVertices().equals(component4.allVertices())
                );
            }
            else {
                assertTrue(
                        c.allEdges().equals(component1.allEdges())
                );
            }
        }

//        for (var component: output) {
//            Graph<Vertex, Edge<Vertex>> gr = (Graph<Vertex, Edge<Vertex>>) component;
//            for (Vertex v: gr.allVertices()) {
//                System.out.println(v.name());
//                System.out.println(gr.getNeighbours(v));
//            }
//            System.out.println("end component");
//        }

//        assertEquals(g3_4Components, output);

    }
}
