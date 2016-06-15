package geom.algorithms;

import geom.structures.dcel.*;
import geom.math.Vector;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;
/**
 * Representa el algoritmo para calcular el polígono de visibilidad de un punto
 * que está dentro de una cara de una subdivisión planar.
 * 
 */
public class VisibilityPolygon {

  final static int SEGMENT_START = 0;
  final static int SEGMENT_END = 1;

  /**
   * Regresa el polígono de visibilidad que ve un punto en una cara poligonal dada de una
   * DCEL.
   * 
   * @param  dcel  La dcel de donde viene la cara
   * @param  face  La cara donde está el punto
   * @param  point Un punto dentro de la cara
   * @return el polígono de visibilidad almacenado como una cara dentro de una DCEL.
   */
  public static Dcel calculateVisibilityPolygon (Dcel dcel, Face face, Vector point) {
    LinkedList<Vertex> polygon =  new LinkedList<Vertex>();
     /* necesitamos la lista de eventos */ 
    LinkedList<HalfEdge> eventsQueue = buildEventsQueue(dcel, point);
    IntersectionComparator statusComparator = new IntersectionComparator(point);
    TreeMap<Vector, HalfEdge> status = new TreeMap<>(statusComparator);

    HalfEdge firstEvent = checkFirstEvent(point, eventsQueue.getFirst(), face);
    
    if (firstEvent != null) {
      status.put(point, firstEvent);
    }

    while (eventsQueue.size() > 0) {
      HalfEdge current = eventsQueue.removeFirst();
      Vertex currentV = current.origin;

      status.put(current.origin, current);

      for (Map.Entry<Vector,HalfEdge> entry : status.entrySet()) {
        Vector key = entry.getKey();
        HalfEdge value = entry.getValue();
        Vector intersection = getIntersection(point, current.origin, value.origin, value.end);
        /* Aquí ya se intersectaron, veremos a cual le pega */
        if (intersection != null) {
          polygon.add(new Vertex(intersection));
        }
      }

      /* 
      *  cuando detectamos el vertice final de un HE ya esta en el arbol. significa que 
      *  ya terminamos de recorrerlo y termino su vida (pues ya fue el primer vertice 
      *  de otro segmento) y debemos sacarlo
      */
      if (status.containsKey(current.end)) {
        status.remove(current.end);
      }
    }

    System.out.println(status);
    System.out.println(polygon);

    return DcelUtils.buildDcelFromList(polygon);
  }
  
  private static LinkedList<HalfEdge> buildEventsQueue(Dcel polygon, Vector p) {
    // Obtengo los vertices del poligono como arreglo
    HalfEdge[] halfEdges = polygon.halfEdges.values().toArray(new HalfEdge[0]);

    // Separamos los vertices en upper y lower
    LinkedList<HalfEdge> upper = new LinkedList<HalfEdge>();
    LinkedList<HalfEdge> lower = new LinkedList<HalfEdge>();

    HalfEdge current;
    for (int i = 0; i < halfEdges.length; i++) {
      current = halfEdges[i];

      if (current.origin.y < p.y) {
        upper.add(current);
      } else {
        lower.add(current);
      }
    }

    // Ordenamos cada uno de los conjuntos
    RadialComparator comparator = new RadialComparator(p);

    Collections.sort(upper, comparator);
    Collections.sort(lower, comparator);

    // Concatenamos y regresamos
    upper.addAll(lower);

    return upper; 
  }

  /**
   * Verifica si el primer evento lo es, y si no, regresa la arista con la que
   * choca.
   */
  private static HalfEdge checkFirstEvent(Vector p, HalfEdge firstEvent, Face f) {
    IntersectionComparator comparator = new IntersectionComparator(p);

    HalfEdge nearest = null;
    Vector nearestIntersection = null;

    HalfEdge he = f.outerComponent;
    while (true) {
      Vector intersection = getIntersection(p, firstEvent.origin, he.origin, he.end);
      if (intersection != null) {
        if (nearest == null || comparator.compare(nearestIntersection, intersection) > 0) {
          nearest = he;
          nearestIntersection = intersection;
        }
      }

      he = he.next;
      if (he.equals(f.outerComponent)) {
        break;
      }
    } 
    return nearest;
  }

  private static Vector getIntersection(Vector p1, Vector p2, Vector q1, Vector q2) {

    // Calcula la direccion de los segmentos
    Vector r  = Vector.sub(p2, p1);
    Vector s  = Vector.sub(q2, q1);

    // qp = q1 - p1
    Vector qp = Vector.sub(q1, p1);

    // r x s
    double rs = Vector.area2(r, s);

    if (rs != 0) {

      // t = (q-p) x s / (r x s)
      double t = Vector.area2(qp, s) / rs;

      // u = (q-p) x r / (r x s)
      double u = Vector.area2(qp, r) / rs;

      if (0 < t && t < 1 && 0 < u && u < 1) {

        // p1 + tr
        r.mult(t);
        return Vector.add(p1, r);
      }
    }

    return null;
  }

  /**
   * Comparador radial.
   */
  public static class RadialComparator implements Comparator<HalfEdge> {

    // Guardamos el punto de comparacion radial (foco)
    Vector p;

    // Constructor del comparador
    public RadialComparator(Vector p) {
      this.p = p;
    }

    public int compare(HalfEdge e1, HalfEdge e2) {

      // Obtenemos el signo del area del paralelogramo
      double areaSign = Vector.areaSign(p, e1.origin, e2.origin);

      // Analizar los casos
      if (areaSign == 0) {

        // Obtenemos la distancia de los puntos
        double distE1 = Vector.dist(e1.origin, p);
        double distE2 = Vector.dist(e2.origin, p);

        if (distE1 < distE2) {
          return -1;
        } else {
          return 1;
        }
      }

      return (int) areaSign;
    }
  }

  /**
   * Comparador de intersecciones.
   * Sirve para el arbol de estado del algoritmo
   */
  public static class IntersectionComparator implements Comparator<Vector> {

    // Guardamos el punto de comparacion radial (foco)
    Vector p;

    // Constructor del comparador
    public IntersectionComparator(Vector p) {
      this.p = p;
    }

    public int compare(Vector v1, Vector v2) {

      // Obtenemos las distancias de los puntos
      double distV1 = Vector.dist(v1, p);
      double distV2 = Vector.dist(v2, p);

      return (int) (distV1 - distV2);
    }
  }
}
