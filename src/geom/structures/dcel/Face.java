package geom.structures.dcel;

import java.util.List;
import java.util.LinkedList;

/**
 * Cara de una DCEL de dos dimensiones.
 *
 * La orientación de las aristas sigue la convencion
 * de tener la cara del lado izquierdo.
 *
 */
public class Face {

  // Identificador único dentro de la DCEL
  private String id;

  // Apuntador a una media arista que compone la componente exterior
  public HalfEdge outerComponent;

  // Mantiene una lista de las medias aristas que componen las
  // compontes interiores
  public List<HalfEdge> innerComponents;

  /**
   * Constuye una cara con su id, componente externo y sus componentes internos.
   *
   * @param id Identificador único dentro de la DCEL.
   * @param outerComponent Arista del componente externo.
   * @param innerComponents Lista de aristas de cada componentes internos (a.k.a hoyos).
   */
  public Face(String id, HalfEdge outerComponent, List<HalfEdge> innerComponents) {
    this.id = id;

    this.outerComponent  = outerComponent;
    this.innerComponents = innerComponents;
  }

  /**
   * Obtiene el identificador de la cara.
   *
   * @return String El identificador de la cara.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Evalua si la cara es la cara externa.
   *
   * @return boolean Si es la cara externa o no.
   */
  public boolean isOuterFace() {
    return this.outerComponent == null;
  }

  /**
   * Compara dos caras para ver si son iguales.
   *
   * @return boolean El test de si son iguales
   */
  public boolean equals(Face face) {
    return this.getId().equals(face.getId());
  }

  /**
   * Regresa el componente externo de la cara como un arreglo.
   *
   * @return HalfEdge[] El componente externo en un arreglo.
   */
  public HalfEdge[] getComponent() {

    // Creamos una lista para ir agregando
    LinkedList<HalfEdge> component = new LinkedList<HalfEdge>();

    // Empezamos en la arista marcada
    HalfEdge he = this.outerComponent;
    while(true) {
      component.add(he);
      he = he.next;
      if(he.equals(this.outerComponent))
        break;
    }

    // Regresamos como array
    return component.toArray(new HalfEdge[0]);
  }
}
