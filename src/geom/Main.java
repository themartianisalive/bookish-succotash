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
    String file = null;
    if (args.length > 0 && !args[0].equals("${input}")) {
      file =  args[0];
    }
    // Crea una instancia de la app y la inicia
    try {
      Proyecto02App app = new Proyecto02App("data/" + file);
      String[] sketchArgs = { "" };
      PApplet.runSketch(sketchArgs, app);
    } catch (Exception e) {
      System.err.println("No se pudo abrir el archivo ");
      e.printStackTrace();
    } 
  }
}
