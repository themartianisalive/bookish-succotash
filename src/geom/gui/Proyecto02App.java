package geom.gui;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PGraphics;

import java.io.IOException;

import geom.utils.DcelReader;
import geom.structures.dcel.*;
import geom.structures.extra.*;

import geom.algorithms.*;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Aplicación gráfica para el proyecto 02.
 */
public class Proyecto02App extends PApplet {
  
  Dcel original;
  Dcel visibilidad;
  Dcel nueva;

  Camera[] cameras;
  PShape plane;
  PGraphics graphics;
  String filePath;

  /* 0 original, 1 visibilidad, 2 nueva */
  int mode = 0;

  public Proyecto02App(String filePath) throws Exception {
    this.filePath = filePath;
    original = DcelReader.readSVG(filePath);
    cameras = DcelReader.readCameras(filePath);
  }

  private void drawCameras(Camera[] cameras) {
    fill(0);
    for (Camera cam : cameras) {
      //text("p", (float) cam.x + 5, (float) cam.y + 5);
      ellipse((float) cam.x, (float) cam.y, 4, 4);  
    }
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
    smooth();
    pixelDensity(displayDensity());
  }

  /**
   * Se ejecuta después de establecer las configuraciones de la app.
   * Sirve para incializar variables globales de la app.
   */
  @Override
  public void setup() {
    frameRate(5);
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
    if (keyPressed) {
      if (key == 'c' || key == 'C') {
        mode = 2;
        cameras = SurveillanceCameras.getSurveillanceCameras(original);
        drawCameras(cameras);
        nueva =  original;
      } 
      if (key == 'v' || key == 'V') {
        mode = 1;
        if (cameras.length > 0) {
          for (Camera cam : cameras) {
            Dcel tmp = VisibilityPolygon.getVisibilityPolygon(original, cam);
            tmp.draw(this.g, color(0, 0, 0), true);
          }
          drawCameras(cameras);
        }
        else {
          showMessageDialog(null, "El archivo no tenia camaras indicadas");
        }
      }
      if (key == 'o' || key == 'O') {
        mode = 0;
      }
    }
    if (mode == 1) {
      //visibilidad.draw(this.g, color(0, 0, 0), true);
    } else if (mode == 2) {
      nueva.draw(this.g, color(0, 0, 0), true);
    } else {
      original.draw(this.g, color(0, 0, 0), true);
    }
  }

}