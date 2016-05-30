package geom;

import geom.gui.Proyecto02App;
import processing.core.PApplet;

/**
 *  Clase principal.
 *  
 */
public class Main {

  /**
   * Punto de entrada al programa.
   * 
   * @param args Argumentos de consola
   */
  public static void main(String[] args) {

    // Crea una instancia de la app y la inicia
    Proyecto02App app = new Proyecto02App();

    String[] sketchArgs = { "" };
    PApplet.runSketch(sketchArgs, app);
  }
}
