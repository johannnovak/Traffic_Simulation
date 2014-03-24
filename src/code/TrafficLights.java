package code;

import gui.frame.Displayable;
import interfaces.Intersectable;

import java.awt.Polygon;
import java.awt.geom.Point2D;

import code.Road.Cardinal;
/**
 * This class represents a Traffic Lights. It implements Intersectable because we want to know when a vehicle intersects
 * a traffic lights. 
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class TrafficLights extends Displayable implements Intersectable{

	private static final long serialVersionUID = 1L;
	private boolean isGreen=true;
	/**
	 * Initialize a TrafficLights. Create its coordinates and directional vector thanks to the road passed by parameter. Create
	 * the circumscribing polygon.
	 * @param road The road to which the traffic lights is attached.
	 */
	public TrafficLights(Road road) {
		this.setImagePath("./images/sprites/feu_vert.png");
		this.createCoordinates(road);
		this.vector = road.getVector();
		this.createPolygon();
	}
	/**
	 * Switch the color of the traffic lights (if currently green, then red, and vice-versa).
	 */
	public void switchColor(){
		isGreen = !isGreen;
		this.setImagePath((isGreen)?"./images/sprites/feu_vert.png":"./images/sprites/feu_rouge.png");
	}
	/**
	 * Create the circumscribing polygon of the trafficlights.
	 */
	private void createPolygon()
	{
		//reset the polygon
		((Polygon)this).reset();
		/*The polygon is created from the RELATIVE upper left corner of the traffic lights and in clockwise. Relative means that
		 * if the traffic lights is for example horizontal to the right, the upper left corner is not the one you're seeing but
		 * the upper right corner (this is what we meant: relative to the image representing the traffic lights) */
		this.addPoint((int)this.coordinates[0], (int)this.coordinates[1]);
		int x, y;
		x = (int) (this.coordinates[0]-Math.sin(this.getAngle())*image.getHeight());
		y = (int) (this.coordinates[1]+Math.cos(this.getAngle())*image.getHeight());
		this.addPoint(x, y);
		x = (int) (this.coordinates[0]+Math.cos(this.getAngle())*image.getWidth()-Math.sin(this.getAngle())*image.getHeight());
		y = (int) (this.coordinates[1]+Math.sin(this.getAngle())*image.getWidth()+Math.cos(this.getAngle())*image.getHeight());
		this.addPoint(x, y);
		x = (int) (this.coordinates[0]+Math.cos(this.getAngle())*image.getWidth());
		y = (int) (this.coordinates[1]+Math.sin(this.getAngle())*image.getWidth());
		this.addPoint(x, y);
	}
	/**
	 * Return whether the traffic lights is green or not.
	 * @return
	 * <ul>
	 * <li>true, if the traffic lights is green
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean isGreen() {
		return isGreen;
	}
	/**
	 * Set the coordinates of the traffic lights thanks to the road passed by parameter.
	 * @param road The road to which the traffic lights is attached.
	 */
	public void createCoordinates(Road road)
	{
		//Depending on the road's angle, compute the coordinates in order to have them atthe end of the road
		this.coordinates = new float[2];
		Cardinal startPoint = road.getStartPoint();
		float xEnd = road.getEnd().getCoordinates()[0], yEnd = road.getEnd().getCoordinates()[1];
		if (startPoint==Cardinal.NORTH)
		{
		   this.coordinates[0]=xEnd+1/2.f*road.getEnd().getImage().getWidth();
		   this.coordinates[1]=yEnd+road.getEnd().getImage().getHeight();
		}
		else if (startPoint==Cardinal.SOUTH)
		{

		   this.coordinates[0] = (xEnd+1/2.f*road.getEnd().getImage().getWidth());
		   this.coordinates[1]= (yEnd);
		}
		else if (startPoint==Cardinal.EAST)
		{
		   this.coordinates[0]= (xEnd+road.getEnd().getImage().getWidth());
		   this.coordinates[1]= (yEnd+1/2.f*road.getEnd().getImage().getHeight());
		}
		else
		{
		   this.coordinates[0]= (xEnd);
		   this.coordinates[1]= (yEnd+road.getEnd().getImage().getHeight()/2);
		}
	}
	/**
	 * Test if an Intersectable object (will only be called with a vehicle passed by parameter) intersects it.
	 * @return
	 * <ul>
	 * <li>true, if at least one corner of the intersectable object is in the circumscribing polygon of the traffic lights
	 * <li>false, otherwise
	 * </ul>
	 */
	@Override
	public boolean intersectWith(Intersectable inter) {
		for (Point2D.Float p : inter.getPoints())
			if (this.contains(p))
			return true;
		return false;
	}
	@Override
	public Point2D.Float[] getPoints() {
		Point2D.Float[] pt = new Point2D.Float[this.npoints];
		for (int i=0; i<this.npoints; ++i)
			pt[i]=new Point2D.Float(this.xpoints[i], this.ypoints[i]);
		return pt;
	}
}
