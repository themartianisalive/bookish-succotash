package geom.structures.extra;

import geom.structures.dcel.Vertex;
import java.util.List;
import java.util.LinkedList;

/**
 * Representación de una camara dentro del ambiente
 * necesitamos su posición y el id del cuarto en el 
 * que existe
 */
public class Camera extends Vertex {
	
	private String roomId;

	public Camera(double x, double y, String roomId) {
		super(x,y);
		this.roomId = roomId;
	}

	public String getRoomId() {
		return roomId;
	}
}