package geom.structures.dcel;


import java.util.List;
import java.util.TreeMap;

/**
 * Clase con algunos métodos de utilería para manejo y 
 * construcción de la estructura Double Connected Edge List (DCEL).
 *
 */
public class DcelUtils {

  /**
   * Construye una arista completa, es decir, sus dos medias aristas.
   * 
   * @param  a Vertice de inicio
   * @param  b Vertice final
   * @return   Las dos medias aristas que forman la arista completa como arreglo.
   */
   public static HalfEdge[] buildEdge(Vertex a, Vertex b) {

    // Creando las aristas
    HalfEdge e1 = new HalfEdge(a, b);
    HalfEdge e2 = new HalfEdge(b, a);

    // Asginarles gemelas
    e1.twin = e2;
    e2.twin = e1;

    // Asignar aristas incidentes
    a.incidentEdge = e1;
    b.incidentEdge = e2;

    HalfEdge[] edge = { e1, e2 };
    return edge;
  }

  /**
   * Construye una cara dado su id y su componente.
   *
   * @param id El identificador para la cara.
   * @param component El componente de la cara.
   */
  public static Face buildFace(String id, HalfEdge[] component) {

    // Creamos la cara (ponemos como refencia de componente a la primera media arista)
    Face face = new Face(id, component[0], null);

    // Componemos prev y next
    DcelUtils.linkComponent(component);

    // Recorremos las medias aristas
    for (int i = 0; i < component.length; i++) {

      // Asignamos la cara incidente
      component[i].incidentFace = face;
    }

    return face;
  }

  /**
   * Enlaza un componente, es decir, asigna el previo y el siguiente.
   *
   * @param component El componente a linkear como arreglo. Debe venir en el orden
   * que se requiere
   */
  public static void linkComponent(HalfEdge[] component) {

    HalfEdge current;
    HalfEdge next;
    for (int i = 0; i < component.length; i++) {
      current = component[i];
      next    = component[(i + 1) % component.length];

      // Componer los registros de siguiente y anterior
      current.next = next;
      next.prev    = current;
    }
  }

  public static Dcel buildDcelFromList(List<Vertex> polygon) {

    TreeMap<String, Vertex>   vertices  = new TreeMap<String, Vertex>();
    TreeMap<String, HalfEdge> halfEdges = new TreeMap<String, HalfEdge>();
    TreeMap<String, Face>     faces     = new TreeMap<String, Face>();
      

    Vertex[] vertexes =  new Vertex[polygon.size()];

    int i = 0;
    
    for (Vertex v : polygon) {
      vertexes[i++] = v;
      vertices.put(v.getId(), v);
    }

    HalfEdge[] components = new HalfEdge[polygon.size()];

    for (int k = 0; k < components.length; k++) {
      Vertex a = vertexes[k];
      Vertex b = vertexes[(k + 1) % vertexes.length];
      HalfEdge hEdge = DcelUtils.buildEdge(a,b)[0];
      components[k] = hEdge;
      halfEdges.put(hEdge.getId(), hEdge);
    }

    Face face = DcelUtils.buildFace("F1", components);
    faces.put("F1", face);

    return new Dcel(vertices, halfEdges, faces);
  }
}