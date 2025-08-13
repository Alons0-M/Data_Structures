
package org.uma.ed.datastructures.graph;

import java.util.StringJoiner;
import org.uma.ed.datastructures.dictionary.Dictionary;
import org.uma.ed.datastructures.dictionary.JDKHashDictionary;
import org.uma.ed.datastructures.set.JDKHashSet;
import org.uma.ed.datastructures.set.Set;

/**
 * Class for directed graphs implemented with a dictionary.
 *
 * @param <V> Type for vertices in graph
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class DictionaryDiGraph<V> implements DiGraph<V> {
  private final Set<V> vertices;               // set with all vertices in graph
  private final Dictionary<V, Set<V>> diEdges; // dictionary with sources as keys and Set of destinations as values

  public DictionaryDiGraph() {
    vertices = JDKHashSet.empty();
    diEdges = JDKHashDictionary.empty();
  }

  /**
   * Creates an empty directed graph.
   *
   * @param <V> Type for vertices in graph.
   *
   * @return An empty directed graph.
   */
  public static <V> DictionaryDiGraph<V> empty() {
    return new DictionaryDiGraph<>();
  }

  /**
   * Creates a directed graph with given vertices and edges.
   *
   * @param vertices vertices to add to graph.
   * @param edges edges to add to graph.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryDiGraph with given vertices and edges.
   */
  public static <V> DictionaryDiGraph<V> of(Set<V> vertices, Set<DiEdge<V>> edges) {
    DictionaryDiGraph<V> diGraph = new DictionaryDiGraph<>();
    for (V vertex : vertices) {
      diGraph.addVertex(vertex);
    }
    for (DiEdge<V> edge : edges) {
      diGraph.addDiEdge(edge.source(), edge.destination());
    }
    return diGraph;
  }

  /**
   * Creates a directed graph with same vertices and edges as given graph.
   *
   * @param diGraph Graph to copy.
   * @param <V> Type for vertices in graph.
   *
   * @return A DictionaryDiGraph with same vertices and edges as given graph.
   */
  public static <V> DictionaryDiGraph<V> copyOf(DiGraph<V> diGraph) {
    //throw new UnsupportedOperationException("Not implemented yet");
    DictionaryDiGraph<V> newGraph =  of(diGraph.vertices(), diGraph.edges());
    return newGraph;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() {
    //throw new UnsupportedOperationException("Not implemented yet");
    return vertices.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addVertex(V v) {
    //throw new UnsupportedOperationException("Not implemented yet");
    vertices.insert(v);
    diEdges.insert(v, JDKHashSet.empty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addDiEdge(V source, V destination) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (!vertices.contains(source) || !vertices.contains(destination)) {
      throw new GraphException("some of the vertices is not defined in a graph");
    }
    if (!diEdges.isDefinedAt(source)) {
      addVertex(source);
    }
    diEdges.valueOf(source).insert(destination);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteDiEdge(V source, V destination) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (vertices.contains(source) && vertices.contains(destination) && diEdges.isDefinedAt(source)) {
      diEdges.valueOf(source).delete(destination);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteVertex(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    //diEdges.valueOf(vertex).clear(); not needed because line down
    diEdges.delete(vertex); // remove from vertices set
    vertices.delete(vertex); // remove from dictionary
    for (var destinations : diEdges.values()) { // remove all appearances in the dictionary (as a destination)
      destinations.delete(vertex);
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
  public Set<DiEdge<V>> edges() {
    //throw new UnsupportedOperationException("Not implemented yet");
    Set<DiEdge<V>> edges = new JDKHashSet<>();
    for (var entry : diEdges.entries()) {
      for (var destination : entry.value()) {
        edges.insert(DiEdge.of(entry.key(), destination));
      }
    }
    //2º way
    //for (var source : diEdges.keys()) {
    //  for (var destination : diEdges.valueOf(source)) {
    //    edges.insert(DiEdge.of(source, destination));
    //  }
    //}
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
   * @param source vertex for which we want to obtain its successors.
   *
   * @return Successors of a vertex.
   */
  @Override
  public Set<V> successors(V source) {
    //throw new UnsupportedOperationException("Not implemented yet");
    if (vertices.contains(source) && diEdges.isDefinedAt(source)) {
      return diEdges.valueOf(source);
    } else {
      return new JDKHashSet<>();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<V> predecessors(V destination) {
    //throw new UnsupportedOperationException("Not implemented yet");
    Set<V> predecessors = new JDKHashSet<>();
    for (var source : diEdges.keys()) {
      if (!source.equals(destination) && diEdges.valueOf(source).contains(destination)) {
        predecessors.insert(source);
      }
    }
    return predecessors;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int inDegree(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    return predecessors(vertex).size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int outDegree(V vertex) {
    //throw new UnsupportedOperationException("Not implemented yet");
    return diEdges.valueOf(vertex).size();
  }

  @Override
  public String toString() {
    String className = getClass().getSimpleName();

    StringJoiner verticesSJ = new StringJoiner(", ", "vertices(", ")");
    for (V vertex : vertices()) {
      verticesSJ.add(vertex.toString());
    }

    StringJoiner edgesSJ = new StringJoiner(", ", "edges(", ")");
    for (DiEdge<V> edge : edges()) {
      edgesSJ.add(edge.toString());
    }

    StringJoiner sj = new StringJoiner(", ", className + "(", ")");
    sj.add(verticesSJ.toString());
    sj.add(edgesSJ.toString());
    return sj.toString();
  }
}
