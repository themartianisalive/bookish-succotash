package geom.utils;

import geom.structures.dcel.*;
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
  public static Dcel readSVG (String filePath) 
  throws IOException, SAXException, 
  ParserConfigurationException {
  	/* cargamos el xml */
  	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder(); 
	Document doc = db.parse(new File(filePath));

	doc.getDocumentElement().normalize();  
	NodeList polygons = doc.getElementsByTagName("polygon");


	for (int i = 0; i < polygons.getLength(); ++i) {
		Element e = (Element)polygons.item(i);
		String points = e.getAttribute("points");
		String[] tmp = points.split(" ");
		Vertex[] vertexes =  new Vertex[tmp.length];

		for (int j = 0; j < tmp.length; ++j) {
			String [] t = tmp[j].split(",");
			Vertex v = new Vertex(Double.parseDouble(t[0]), Double.parseDouble(t[1]));
			vertexes[j] = v;
		}

		vertexes =  Vertex.getCounterClockwiseVertexes(vertexes);
		System.out.println(vertexes);
	}

    return null;
  }
}
