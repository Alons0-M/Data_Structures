package org.uma.ed.datastructures.graph;

import java.util.StringJoiner;
import org.uma.ed.datastructures.dictionary.Dictionary;
import org.uma.ed.datastructures.dictionary.JDKHashDictionary;
import org.uma.ed.datastructures.set.JDKHashSet;
import org.uma.ed.datastructures.set.Set;

/**
 * Class for undirected graphs implemented with a dictionary
 *
 * @param <V> Type for vertices in graph
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class DictionaryGraph<V> implements Graph<V> {
  private final Set<V> vertices;                // set with all vertices in graph
  private final Dictionary<V, Set<V>> diEdges;  // dictionary with sources as keys and Set of destinations as values

  /**
   * Creates an empty graph.
   */
  public DictionaryGraph() {
    vertices = JDKHashSet.empty();
    diEdges = JDKHashDictionary.empty();
  }

  /**
   * Creates an empty graph.
   *
   * @param <V> Type for vertices in graph.
   *
   * @return An empty DictionaryGraph.
   */
  public static <V> DictionaryGraph<V> empty() {
    return new DictionaryGraph<>();
  }

  /**
   * Creates a graph with given vertices and edges.
   *
   * @param vertices vertices to add to graph.
   * @param edges edges to add to graph.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryGraph with given vertices and edges.
   */
  public static <V> DictionaryGraph<V> of(Set<V> vertices, Set<Edge<V>> edges) {
    DictionaryGraph<V> graph = new DictionaryGraph<>();
    for (V vertex : vertices) {
      graph.addVertex(vertex);
    }
    for (Edge<V> edge : edges) {
      graph.addEdge(edge.vertex1(), edge.vertex2());
    }
    return graph;
  }

  /**
   * Creates a graph with same vertices and edges as given graph.
   *
   * @param graph Graph to copy.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryGraph with same vertices and edges as given graph.
   */
  public static <V> DictionaryGraph<V> copyOf(Graph<V> graph) {
    //throw new UnsupportedOperationException("Not implemented yet");
    DictionaryGraph<V> newGraph = of(graph.vertices(), graph.edges());
    return newGraph;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() {
    //throw new UnsupportedOperationException("Not implemented yet");
    return vertices().isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVertex(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    vertices.insert(vertex);
    if (!diEdges.isDefinedAt(vertex)) {
      diEdges.insert(vertex, JDKHashSet.empty());
    }
  }

  private void addDiEdge(V source, V destination) {
    //throw new UnsupportedOperationException("Not implemented yet");
    diEdges.valueOf(source).insert(destination);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addEdge(V vertex1, V vertex2) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (!vertices.contains(vertex1)) {
      throw new GraphException("first vertex is not in the graph");
    }
    if (!vertices.contains(vertex2)) {
      throw new GraphException("second vertex is not in the graph");
    }
    addDiEdge(vertex1, vertex2);
    addDiEdge(vertex2, vertex1);
  }

  private void deleteDiEdge(V source, V destination) {
    //throw new UnsupportedOperationException("Not implemented yet");
    diEdges.valueOf(source).delete(destination);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteEdge(V vertex1, V vertex2) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (!vertices.contains(vertex1)){
      throw new GraphException("first vertex is not in graph");
    }
    if (!vertices.contains(vertex2)){
      throw new GraphException("second vertex is not in graph");
    }
    deleteDiEdge(vertex1, vertex2);
    deleteDiEdge(vertex2, vertex1);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteVertex(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (vertices.contains(vertex)) {
      // delete all the appearances of the vertex so all the edges containing this vertex will be removed
      for (Set<V> adjacentEdges : diEdges.values()) {
        adjacentEdges.delete(vertex);
      }
      // delete the vertex from the dictionary
      diEdges.delete(vertex);
      vertices.delete(vertex);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<V> vertices() {
    //throw new UnsupportedOperationException("Not implemented yet");
    return vertices;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<Edge<V>> edges() {
    //throw new UnsupportedOperationException("Not implemented yet");
    Set<Edge<V>> edges = new JDKHashSet<>();
    for (V vertice1 : diEdges.keys()) {
      for (V vertice2 : diEdges.valueOf(vertice1)) {
        Edge<V> edge1 = new Edge<>(vertice1, vertice2);
        Edge<V> edge2 = new Edge<>(vertice2, vertice1);
        if (!edges.contains(edge1) && !edges.contains(edge2)) {
          // insertion is performed only if this edge does not already exist in the set (in any order)
          edges.insert(edge1);
        }
      }
    }
    return edges;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int numberOfVertices() {
    //throw new UnsupportedOperationException("Not implemented yet");
    return vertices.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int numberOfEdges() {
    //throw new UnsupportedOperationException("Not implemented yet");
    return edges().size();
  }

  /**
   * Returns the successors of a vertex in graph (i.e. vertices to which there is an edge from given vertex).
   *
   * @param vertex vertex for which we want to obtain its successors.
   *
   * @return Successors of a vertex.
   */
  @Override
  public Set<V> successors(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    return (vertices.contains(vertex) && diEdges.isDefinedAt(vertex))  ?  diEdges.valueOf(vertex)  :  null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int degree(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    return successors(vertex) == null  ?  0  :  successors(vertex).size();
  }

  @Override
  public String toString() {
    String className = getClass().getSimpleName();

    StringJoiner verticesSJ = new StringJoiner(", ", "vertices(", ")");
    for (V vertex : vertices()) {
      verticesSJ.add(vertex.toString());
    }

    StringJoiner edgesSJ = new StringJoiner(", ", "edges(", ")");
    for (Edge<V> edge : edges()) {
      edgesSJ.add(edge.toString());
    }

    StringJoiner sj = new StringJoiner(", ", className + "(", ")");
    sj.add(verticesSJ.toString());
    sj.add(edgesSJ.toString());
    return sj.toString();
  }
}
