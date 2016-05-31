package geom.structures.dcel;

import geom.math.Vector;
import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;

/**
 * Representación de un vertice de una DCEL de dos dimensiones.
 * Hereda de la clase Vector, ya que también es un punto en el
 * plano y facilita las operaciones de la DCEL.
 *
 */
public class Vertex extends Vector {

  // Identificador único dentro de la DCEL
  private String id;

  // Arista incidente (una de la cual es origen)
  public HalfEdge incidentEdge;

  /**
   * Constuye un vértice con su id y sus coordenadas x, y.
   *
   * NOTA: El vértice se crea separado, es decir, sin arista incidente.
   *
   * @param x La coordenada x del vertice.
   * @param y La coordenada y del vertice.
   */
  public Vertex(double x, double y) {
    
    // Ocupa el constructor padre
    super(x, y);

    // Seteamos su arista incidente
    this.incidentEdge = null;

    // Asigna su id
    this.id = this.x + "," + this.y;
  }

  /**
   * Constuye un vértice a partir de un vector de dos dimensiones.
   *
   * NOTA: El vértice se crea separado, es decir, sin arista incidente.
   *
   * @param vector El vector de dos dimensiones.
   */
  public Vertex(Vector vector) {

    // Ocupa el constructor padre
    super(vector.x, vector.y);

    // Seteamos su arista incidente
    this.incidentEdge = null;

    // Asigna su id
    this.id = this.x + "," + this.y;
  }

  /**
   * Obtiene el identificador del vertice.
   *
   * @return String El identificador del vertice.
   */
  public String getId () {
    return this.id;
  }

  public static Vertex[] getCounterClockwiseVertexes(Vertex[] points) {
    Vertex minX = new Vertex(Double.MAX_VALUE, 0);
    for (Vertex v : points) {
      if (v.x < minX.x)
        minX = v;  
    }

    TreeMap<Double, Vertex> slopes =  new TreeMap<>();
    for (Vertex v : points) {
      double m = (v.y - minX.y) / (v.x - minX.x);
      slopes.put(m,v);
    }

    Vertex[] vectorsBySlope =  new Vertex[points.length];
    int i = 0;
    for (Map.Entry<Double, Vertex> node : slopes.entrySet()) {
      vectorsBySlope[i++] = node.getValue();
    }
    
    return vectorsBySlope;
  }

}