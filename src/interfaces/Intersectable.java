package interfaces;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * This interface gives a mean to check intersections between two objects of implementing this interface and retrieve the
 * circumscribing polygon of an object implementing it.
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public interface Intersectable extends Serializable {
	/*Représente un objet qui puisse entrer en collision avec un objet de cette même classe*/
	/**
	 * Find if there is an intersection with an Intersectable object. There exists one if at least one of the points of the
	 * current Intersectable object or the Intersectable object passed by parameter is contained in the polygon of the other.
	 * @param inter The Intersectable object we want to know if it intersects the current Intersectable object.
	 * 
	 * @return
	 * <ul>
	 * <li>true, if intersection between those two objects
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean intersectWith(Intersectable inter);
	/**
	 * Return the points of the circumscribing polygon.
	 * @return the points of the circumscribing polygon. 
	 */
	public Point2D.Float[] getPoints();
}
