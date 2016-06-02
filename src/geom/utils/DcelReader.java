package geom.utils;

import geom.structures.dcel.*;
import geom.structures.extra.*;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.*;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Clase de utilería para leer subdivisiones almacenadas en archivos SVG.
 * 
 */
public class DcelReader {

  /**
   * Lee un archivo svg
   * @param  filePath    La ruta del archivo svg que se debe cargar.
   * @return             La subdivisión leída del archivo.
   * @throws IOException Si el archivo no se puede leer o no existe.
   */
  public static Dcel readSVG (String filePath) throws IOException, Exception {
	NodeList polygons = getNodesByTag(filePath, "polygon");

	TreeMap<String, Vertex>   vertices  = new TreeMap<String, Vertex>();
	TreeMap<String, HalfEdge> halfEdges = new TreeMap<String, HalfEdge>();
	TreeMap<String, Face> 	  faces     = new TreeMap<String, Face>();
 
	for (int i = 0; i < polygons.getLength(); ++i) {

		Element e = (Element)polygons.item(i);
		String points = e.getAttribute("points");
		String id = e.getAttribute("id");
		String[] tmp = points.split(" ");
		Vertex[] vertexes =  new Vertex[tmp.length];

		for (int j = 0; j < tmp.length; ++j) {
			String [] t = tmp[j].split(",");
			Vertex v = new Vertex(Double.parseDouble(t[0]), Double.parseDouble(t[1]));
			vertices.put(v.getId(), v);
			vertexes[j] = v;
		}

		HalfEdge[] components = new HalfEdge[vertexes.length];

		for (int k = 0; k < vertexes.length; k++) {
		  Vertex a = vertexes[k];
		  Vertex b = vertexes[(k + 1) % vertexes.length];
		  HalfEdge hEdge = DcelUtils.buildEdge(a,b)[0];
		  components[k] = hEdge;
		  halfEdges.put(hEdge.getId(), hEdge);
		}

		Face face = DcelUtils.buildFace(id, components);
		faces.put(id, face);
	}
	return new Dcel(vertices, halfEdges, faces);
  }

  private static void printVertexes(Vertex[] v) {
  	for (Vertex c : v) {
  		System.out.print(c);
  	}
  	System.out.println();
  }

  public static Camera[] readCameras(String filePath) throws IOException, Exception {
	NodeList cameras = getNodesByTag(filePath, "circle");
	Camera[] tmp =  new Camera[cameras.getLength()];
	
	for (int i = 0; i < cameras.getLength(); ++i) {
		Element e = (Element)cameras.item(i);

		double x = Double.parseDouble(e.getAttribute("cx"));
		double y = Double.parseDouble(e.getAttribute("cy"));
		
		String id = e.getAttribute("room-id");
		tmp[i] = new Camera(x, y, id);
	}
	return tmp;
  }

  private static NodeList getNodesByTag(String filePath, String tagName) throws Exception {
  	/* cargamos el xml */
  	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder(); 
	Document doc = db.parse(new File(filePath));
	/* Sacamos los poligonos */
	doc.getDocumentElement().normalize();  
	return doc.getElementsByTagName(tagName);
  }
}
