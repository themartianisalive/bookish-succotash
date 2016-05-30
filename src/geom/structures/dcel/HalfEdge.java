package geom.structures.dcel;

/**
 * Media arista de una DCEL de dos dimensiones.
 *
 * Esta representa una parte, o un sentido de una arista
 * en una subdivisión planar.
 *
 * vi -----> vj
 *
 * Se guarda un registro de su arista gemela, es decir,
 * la que la complementa en sentido.
 *
 * Gemela:
 * vj -----> vi
 *
 */
public class HalfEdge {

  // Identificador único dentro de la DCEL
  private String id;

  // Vertice de inicio
  public Vertex origin;

  // Vertice de final
  public Vertex end;

  // Arista gemela
  public HalfEdge twin;

  // Arista siguiente
  public HalfEdge next;

  // Arista previa
  public HalfEdge prev;

  // Cara incidente
  public Face incidentFace;

  /**
   * Constuye una arista con su id, origen y final.
   * Este constructor asigna automaticamente que la arista
   * incidente al origen es la creada.
   *
   * NOTA: La arista se crea separada, es decir, sin gemela, ni previo, ni siguiente.
   *
   * @param origin Vertice de origen.
   * @param end Vertice de final.
   */
  public HalfEdge(Vertex origin, Vertex end) {

    // Seteamos origen y final
    this.origin = origin;
    this.end = end;

    // Esta será la arista incidente del origen
    this.origin.incidentEdge = this;

    // Apuntadoreas a gemela, siguiente, previa y cara incidente
    this.twin = null;
    this.next = null;
    this.prev = null;
    this.incidentFace = null;

    // Se crea el identificador
    this.id = this.origin.getId() + ":" + this.end.getId();
  }

  /**
   * Obtiene el identificador de la arista.
   *
   * @return String El identificador de la arista.
   */
  public String getId () {
    return this.id;
  }

  /**
   * Compara dos aristas para ver si son iguales.
   *
   * @return boolean El test de si son iguales
   */
  public boolean equals(HalfEdge he) {
    return this.origin.equals(he.origin) && this.end.equals(he.end);
  }

  @Override
  public String toString() {
    return this.origin.toString() + " -> " + this.end.toString();
  }
}