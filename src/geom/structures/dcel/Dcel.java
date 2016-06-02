package geom.structures.dcel;

import java.util.TreeMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import processing.core.PGraphics;

/**
 * Implementación de la estructura de datos Double Connected Edge List
 * para representar una subdivision planar (dos dimensiones).
 * Se mantienen tres registros: Caras, Aritstas y Vertices.
 * Dado que una arista es adyacente a dos caras, una arista
 * se guarda en dos registros llamados half-edge.
 *
 * Ocupa la implementación en la JavaAPI de los árboles rojo-negro para
 * guardar los registros.
 *
 */
public class Dcel {

  // Registros comunes de la DCEL
  public TreeMap<String, Vertex> vertices;
  public TreeMap<String, HalfEdge> halfEdges;
  public TreeMap<String, Face> faces;

  // Contador para el id de la cara
  public int nextFaceId;

  /**
   * Construye una DCEL vacía.
   * Inicia los registros de caras, aristas y vertices vacios.
   *
   */
  public Dcel() {

    // Inicalizamos los registros
    this.vertices = new TreeMap<String, Vertex>();
    this.halfEdges = new TreeMap<String, HalfEdge>();
    this.faces = new TreeMap<String, Face>();

    // Comienza el contador de ids para caras
    this.nextFaceId = 1;
  }

  /**
   * Construye una DCEL con los registros dados.
   *
   * @param vertices Registro de vertices en un árbol rojo-negro.
   * @param halfEdges Registro de aristas en un árbol rojo-negro.
   * @param faces Registro de caras en un árbol rojo-negro.
   */
  public Dcel(TreeMap<String, Vertex> vertices, TreeMap<String, HalfEdge> halfEdges, TreeMap<String, Face> faces) {

    // Asignamos los registros dados
    this.vertices = vertices;
    this.halfEdges = halfEdges;
    this.faces = faces;

    // Comienza el contador de ids para caras
    this.nextFaceId = this.faces.size() + 1;
  }

  /**
   * Agrega una cara al registro de caras.
   *
   * @param face La cara a agregar.
   */
  public void addFace(Face face) {
    if(!this.faces.containsKey(face.getId())) {
      this.faces.put(face.getId(), face);
      this.nextFaceId++;
    }
  }

  /**
   * Agrega una arista al registro de aristas.
   *
   * @param halfEdge La arista por agregar.
   */
  public void addHalfEdge(HalfEdge halfEdge) {
    if(!this.halfEdges.containsKey(halfEdge.getId())) {
      this.halfEdges.put(halfEdge.getId(), halfEdge);
    }
  }

  /**
   * Agrega un vertice al registro de vertices.
   *
   * @param vertex El vertice por agregar.
   */
  public void addVertex(Vertex vertex) {
    if(!this.vertices.containsKey(vertex.getId())) {
      this.vertices.put(vertex.getId(), vertex);
    }
  }

  /**
   * Obtiene el registro de la cara externa (la unica que no
   * tiene componente exterior).
   *
   * @return Face La cara externa.
   */
  public Face getOuterFace() {

    // Iteramos las caras registradas
    Iterator<Face> facesIte = faces.values().iterator();
    while(facesIte.hasNext()) {
      Face face = facesIte.next();
      if(face.isOuterFace()) {
        return face;
      }
    }

    return null;
  }

    /**
   * Crea una diagonal de A a B.
   *
   * @param  a Primer media arista.
   * @param  b Segunda media arista.
   * @return   Las caras resultantes de que se partieron.
   */
  public Face[] makeDiagonal(HalfEdge a, HalfEdge b) {

    // Creamos la nueva arista
    HalfEdge[] diagonal = DcelUtils.buildEdge(a.origin, b.origin);
    this.addHalfEdge(diagonal[0]);
    this.addHalfEdge(diagonal[1]);

    // Componer los apuntadores de las aristas
    HalfEdge tmp = a.prev;
    a.prev = diagonal[1];
    diagonal[1].next = a;
    tmp.next = diagonal[0];
    diagonal[0].prev = tmp;

    tmp = b.prev;
    b.prev = diagonal[0];
    diagonal[0].next = b;
    tmp.next = diagonal[1];
    diagonal[1].prev = tmp;

    // Componemos las caras
    Face f = a.incidentFace;
    diagonal[1].incidentFace = f;
    f.outerComponent = diagonal[1];

    // Creamos la nueva cara
    Face f_ = new Face("" + this.nextFaceId, diagonal[0], null);
    this.addFace(f_);

    HalfEdge he = diagonal[0];
    while (true) {
      he.incidentFace = f_;

      he = he.next;
      if (he.equals(diagonal[0])) {
        break;
      }
    }

    Face[] faces = { f, f_ };
    return faces;
  }


  /**
   * Dibuja la DCEL en un sketch de Processing.
   *
   * @param pGraphics   Graficos donde dibujar la DCEL
   * @param colorStroke Color para lineas -1 si no se quiere
   * @param text        Boolean para saber si pintar el texto o no
   */
  public void draw(PGraphics pGraphics, int colorStroke, boolean text) {

    // Iteramos las caras registradas
    Iterator<Face> facesIte = faces.values().iterator();
    while(facesIte.hasNext()) {

      // Si no es la cara externa
      Face face = facesIte.next();
      if(!face.isOuterFace()) {

        pGraphics.beginShape();
        HalfEdge he = face.outerComponent;
        float x, y;
        while (true) {
          x = (float) he.origin.x;
          y = (float) he.origin.y;

          // Text
          if (text) {
            pGraphics.fill(0);
            pGraphics.text(he.origin.toString(), x, y);
          }

          pGraphics.vertex(x, y);

          he = he.next;
          if (he.equals(face.outerComponent)) {
            break;
          }
        }

        pGraphics.stroke(colorStroke);
        pGraphics.noFill();
        pGraphics.endShape(pGraphics.CLOSE);
      }
    }
  }

  /**
   * Dibuja la DCEL en un sketch de Processing.
   *
   * @param pGraphics   Graficos donde dibujar la DCEL
   * @param colorStroke Color para lineas -1 si no se quiere
   * @param text        Boolean para saber si pintar el texto o no
   */
  public void draw(PGraphics pGraphics, int colorStroke, int fill, boolean text) {

    // Iteramos las caras registradas
    Iterator<Face> facesIte = faces.values().iterator();
    while(facesIte.hasNext()) {

      // Si no es la cara externa
      Face face = facesIte.next();
      if(!face.isOuterFace()) {

        pGraphics.beginShape();
        HalfEdge he = face.outerComponent;
        float x, y;
        while (true) {
          x = (float) he.origin.x;
          y = (float) he.origin.y;

          // Text
          if (text) {
            pGraphics.fill(0);
            pGraphics.text(he.origin.toString(), x, y);
          }

          pGraphics.vertex(x, y);

          he = he.next;
          if (he.equals(face.outerComponent)) {
            break;
          }
        }

        pGraphics.stroke(colorStroke);
        pGraphics.fill(fill);
        pGraphics.endShape(pGraphics.CLOSE);
      }
    }
  }
}
