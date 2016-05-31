package geom.utils;

import geom.structures.dcel.Dcel;
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

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
  		throws IOException, SAXException, ParserConfigurationException {
  	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder(); 
	File f = new File(filePath);
	Document doc = db.parse(f);
    return null;
  }
}
