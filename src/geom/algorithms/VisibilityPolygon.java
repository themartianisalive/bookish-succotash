package geom.algorithms;

import geom.math.Vector;
import geom.structures.dcel.Dcel;
import geom.structures.dcel.Face;

/**
 * Representa el algoritmo para calcular el polígono de visibilidad de un punto
 * que está dentro de una cara de una subdivisión planar.
 * 
 */
public class VisibilityPolygon {

  /**
   * Regresa el polígono de visibilidad que ve un punto en una cara poligonal dada de una
   * DCEL.
   * 
   * @param  dcel  La dcel de donde viene la cara
   * @param  face  La cara donde está el punto
   * @param  point Un punto dentro de la cara
   * @return       el polígono de visibilidad almacenado como una cara dentro de una DCEL.
   */
  public static Dcel calculateVisibilityPolygon (Dcel dcel, Face face, Vector point) {
    return null;
  }
}
