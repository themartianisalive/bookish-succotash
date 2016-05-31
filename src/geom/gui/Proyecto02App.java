package geom.gui;

import processing.core.PApplet;
import processing.core.PShape;

import java.io.IOException;

import geom.utils.DcelReader;
import geom.structures.dcel.*;

/**
 * Aplicación gráfica para el proyecto 02.
 */
public class Proyecto02App extends PApplet {
  Dcel face;
  PShape plane;
  String filePath;

  public Proyecto02App(String filePath) throws Exception {
    this.filePath = filePath;
    face = DcelReader.readSVG(filePath);
  }
  /**
   * Se ejecuta al incio de la app.
   * Sirve para establecer configuraciones de la ventana de la app,
   * como el tamaño.
   * 
   */
  @Override
  public void settings() {
    size(700, 700);
  }

  /**
   * Se ejecuta después de establecer las configuraciones de la app.
   * Sirve para incializar variables globales de la app.
   */
  @Override
  public void setup() {
    frameRate(5);
    plane = loadShape(filePath);
  }

  /**
   * Se ejecuta de acuerdo al frameRate establecido.
   * Por default el frameRate es 60, es decir, esta función
   * se ejecuta 60 veces por segundo (60fps).
   *
   * En ella deben estar todos las llamadas a funciones de dibujo.
   */
  @Override
  public void draw() {
    shape(plane, 50, 50, 650, 650);
  }

}