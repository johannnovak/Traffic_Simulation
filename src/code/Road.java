package code;

import gui.frame.Displayable;

import java.awt.geom.Point2D;
import java.io.Serializable;
/**
 * This class represents a Road. It has two cardinals which are where the road is attached to the starting and ending
 *  crossroads and a distance (between the crossroads).
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class Road extends Displayable {

	/**
	 * Le cardinal représente l'endroit où se rattache la route par rapport au carrefour
	 * et non où est situé le carrefour source par rapport au carrefour fin (c'est l'inverse)
	 */
	/**
	 * This class represents the four cardinal (North, South, East, West). It is where the road is attached to the crossroad
	 * and NOT where the source crossroad is placed relative to the destination crossroad.
	 * 
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public enum Cardinal implements Serializable{
		NORTH,
		SOUTH,
		EAST,
		WEST;
	}
	
	private static final long serialVersionUID = 1L;
	private CrossRoad start, end;
	private Cardinal startPoint, endPoint;
	private float distance;
	//Store the beginning and the end of the road.
	private Point2D.Float startingPoint, endingPoint;
	/**
	 * Initialize the road whose starting and ending crossroads are passed by parameter. It also set its image.
	 * @param start The crossroad from which the road starts.
	 * @param end The crossroad to which the road ends.
	 */
	Road(CrossRoad start, CrossRoad end){
		this.start = start;
		this.end = end;
		this.coordinates = new float[2];
		this.setImagePath("./images/sprites/road.png");
		initialize();
		
	}
	/**
	 * Initialize the coordinates, the vector and the starting and ending points.
	 */
	private void initialize(){
		float coeff;
		float xEnd = end.getCoordinates()[0], yEnd = end.getCoordinates()[1], xStart = start.getCoordinates()[0], yStart = start.getCoordinates()[1];
		//Updates cardinals
		setLocalisation(xEnd, yEnd, xStart, yStart);
		//Compute directional vector
		if (startPoint==Cardinal.NORTH || startPoint==Cardinal.SOUTH)
		{
			coeff = start.getImage().getHeight();
			if (startPoint==Cardinal.NORTH)
				this.vector[1] = yEnd-yStart+coeff;
			else
				this.vector[1] = yEnd-yStart-coeff;
			this.vector[0] = xEnd-xStart;
		}
		else
		{
			coeff = start.getImage().getWidth();
			if (startPoint==Cardinal.WEST)
				this.vector[0] = xEnd-xStart-coeff;
			else
				this.vector[0] = xEnd-xStart+coeff;
			this.vector[1] = yEnd-yStart;
		}
		this.distance = (float) Math.sqrt(Math.pow(xStart-xEnd,2)+Math.pow(yStart-yEnd+coeff,2));
		//Compute coordinates, the circumscribing polygon and starting and ending points.
		int x, y;
		this.startingPoint = new Point2D.Float();
		this.endingPoint = new Point2D.Float();
		if (startPoint==Cardinal.NORTH)
		{
		   this.coordinates[0]=xEnd+1/2.f*end.getImage().getWidth();
		   this.coordinates[1]=yEnd+end.getImage().getHeight();
		   this.addPoint((int)this.coordinates[0], (int)this.coordinates[1]);
		   x=(int) (xEnd+end.getImage().getWidth());
		   y=(int) (yEnd+end.getImage().getHeight());
		   this.addPoint(x, y);
		   x=(int) (xStart+start.getImage().getWidth());
		   y=(int) (yStart);
		   this.addPoint(x, y);
		   x=(int) (xStart+1/2.f*start.getImage().getWidth());
		   y=(int) (yStart);
		   this.addPoint(x, y);
		   this.startingPoint.setLocation(xStart+3/4.f*start.getImage().getWidth(), yStart);
		   this.endingPoint.setLocation(xEnd+3/4.f*end.getImage().getWidth(), yEnd+end.getImage().getHeight());
		}
		else if (startPoint==Cardinal.SOUTH)
		{

		   this.coordinates[0] = (xEnd+1/2.f*end.getImage().getWidth());
		   this.coordinates[1]= (yEnd);
		   this.addPoint((int) this.coordinates[0], (int)this.coordinates[1]);
		   x=(int)(xEnd);
		   y=(int)(yEnd);
		   this.addPoint(x, y);
		   x =(int) (xStart);
		   y = (int) (yStart+start.getImage().getHeight());
		   this.addPoint(x,y);
		   x=(int) (xStart+1/2.f*start.getImage().getWidth());
		   y=(int) (yStart+start.getImage().getHeight());
		   this.addPoint(x, y);
		   this.startingPoint.setLocation(xStart+1/4.f*start.getImage().getWidth(), yStart+start.getImage().getHeight());
		   this.endingPoint.setLocation(xEnd+1/4.f*end.getImage().getWidth(), yEnd);
		}
		else if (startPoint==Cardinal.EAST)
		{
		   this.coordinates[0]= (xEnd+end.getImage().getWidth());
		   this.coordinates[1]= (yEnd+1/2.f*end.getImage().getHeight());
		   this.addPoint((int)this.coordinates[0], (int)this.coordinates[1]);
		   x=(int) (xEnd+end.getImage().getWidth());
		   y=(int) (yEnd);
		   this.addPoint(x, y);
		   x=(int) (xStart);
		   y=(int) (yStart);
		   this.addPoint(x, y);
		   x=(int) (xStart);
		   y=(int) (yStart+1/2.f*start.getImage().getHeight());
		   this.addPoint(x, y);
		   this.startingPoint.setLocation(xStart, yStart+1/4.f*start.getImage().getHeight());
		   this.endingPoint.setLocation(xEnd+end.getImage().getWidth(), yEnd+1/4.f*end.getImage().getHeight());
		}
		else
		{
		   this.coordinates[0]= (xEnd);
		   this.coordinates[1]= (yEnd+end.getImage().getHeight()/2);
		   this.addPoint((int)this.coordinates[0], (int)this.coordinates[1]);
		   x=(int) (xEnd);
		   y=(int) (yEnd+end.getImage().getHeight());
		   this.addPoint(x, y);
		   x=(int) (xStart+start.getImage().getWidth());
		   y=(int) (yStart+start.getImage().getHeight());
		   this.addPoint(x, y);
		   x=(int)(xStart+start.getImage().getWidth());
		   y=(int)(yStart+1/2.f*start.getImage().getHeight());
		   this.addPoint(x,y); 
		   this.startingPoint.setLocation(xStart+start.getImage().getWidth(), yStart+3/4.f*start.getImage().getHeight());
		   this.endingPoint.setLocation(xEnd, yEnd+3/4.f*end.getImage().getHeight());
		}
		//Add the road to both crossroads.
		start.addExitingRoad(this, this.startPoint);
		end.addEnteringRoad(this, this.endPoint);
	}
	/**
	 * Check whether the current road crosses the road passed by parameter.
	 * @param road The road to test if it crosses the current road.
	 * @return
	 * <ul>
	 * <li>true, if both roads are crossed
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean isCrossed(Road road){
		return this.getAngleModuloPI()!=road.getAngleModuloPI();
	}
	/**
	 * Compute the point where the current road and the road passed by parameter cross themselves.
	 *  /!\ We assume the roads are crossing (prior tested with isCrossed !)
	 * @param road The road needed to find the point of crossing between the current road and road.
	 * @return coordinates of the crossing.
	 */
	public float[] getCoordinatesOfIntersectionWith(Road road){
		float[] coordinates = new float[2];
		if (this.getVector()[0]==0 && road.getVector()[1]==0)
		{
			coordinates[0]=this.getStartingPoint().x;
			coordinates[1]=road.getStartingPoint().y;
		}
		else if(this.getVector()[1]==0 && road.getVector()[0]==0)
		{
			coordinates[0]=road.getStartingPoint().x;
			coordinates[1]=this.getStartingPoint().y;
		}
		else if (this.getVector()[0]==0)
		{
			coordinates[0]=this.getStartingPoint().x;
			coordinates[1]=(road.getStartingPoint().y-road.getVector()[1]/road.getVector()[0]*road.getStartingPoint().x)+road.getVector()[1]/road.getVector()[0]*coordinates[0];
		}
		else if (this.getVector()[1]==0)
		{
			coordinates[1]=this.getStartingPoint().y;
			coordinates[0]=(coordinates[1]-(road.getStartingPoint().y-road.getVector()[1]/road.getVector()[0]*road.getStartingPoint().x))/(road.getVector()[1]/road.getVector()[0]);	
		}
		else if (road.getVector()[0]==0)
		{
			coordinates[0]=road.getStartingPoint().x;
			coordinates[1]=(this.getStartingPoint().y-this.getVector()[1]/this.getVector()[0]*this.getStartingPoint().x)+this.getVector()[1]/this.getVector()[0]*coordinates[0];;
		}
		else if (road.getVector()[1]==0)
		{
			coordinates[1]=road.getStartingPoint().y;
			coordinates[0]=(coordinates[1]-(this.getStartingPoint().y-this.getVector()[1]/this.getVector()[0]*this.getStartingPoint().x))/(this.getVector()[1]/this.getVector()[0]);	
		}
		else
		{
			coordinates[0] = (float) ((road.getStartingPoint().getX()*road.getVector()[1]/road.getVector()[0]*(-1)+road.getStartingPoint().getY())-(this.getStartingPoint().getX()*this.getVector()[1]/this.getVector()[0]*(-1)+this.getStartingPoint().getY()))/(this.getVector()[1]/this.getVector()[0]-road.getVector()[1]/road.getVector()[0]);
			coordinates[1] = (float) ((this.getVector()[1]/this.getVector()[0])*coordinates[0]+(this.getStartingPoint().getX()*this.getVector()[1]/this.getVector()[0]*(-1)+this.getStartingPoint().getY()));
		}
		return coordinates;
	}
	/**
	 * Return the crossroad from which the road is starting.
	 * @return the crossroad from which the road is starting.
	 */
	public CrossRoad getStart() {
		return start;
	}
	/**
	 * Return the crossroad to which the road is ending.
	 * @return the crossroad to which the road is ending.
	 */
	public CrossRoad getEnd() {
		return end;
	}
	/**
	 * Return the distance between the two crossroads which is also the length of the road.
	 * @return the distance between the two crossroads which is also the length of the road.
	 */
	public float getDistance() {
		return distance;
	}
	/**
	 * Set the cardinal points of the road.
	 * @param xEnd The upper left corner X coordinate of the crossroad to which the road is ending.
	 * @param yEnd The upper left corner Y coordinate of the crossroad to which the road is ending.
	 * @param xStart The upper left corner X coordinate of the crossroad from which the road is starting.
	 * @param yStart The upper left corner Y coordinate of the crossroad from which the road is starting.
	 */
	private void setLocalisation(float xEnd, float yEnd, float xStart, float yStart){
		float temp = Math.abs((yEnd - yStart)/(xEnd - xStart));
		if (xEnd-xStart == 0){
			if ((yEnd-yStart)<0){
				this.startPoint = Cardinal.NORTH;
				this.endPoint = Cardinal.SOUTH;
			}
			else
			{
				this.endPoint = Cardinal.NORTH;
				this.startPoint = Cardinal.SOUTH;
			}
		}
		else if (temp>1 && (yEnd-yStart)<0){
			this.startPoint = Cardinal.NORTH;
			this.endPoint = Cardinal.SOUTH;
		}
		else if (temp>1)//(yEnd-yStart)>0
		{
			this.endPoint = Cardinal.NORTH;
			this.startPoint = Cardinal.SOUTH;
		}
		else if ((xEnd-xStart)<0)//(temp<1)
		{
			this.startPoint = Cardinal.EAST;
			this.endPoint = Cardinal.WEST;
		}
		else // ((xEnd-xStart)>0) && (temp<1)
		{
			this.startPoint = Cardinal.WEST;
			this.endPoint = Cardinal.EAST;
		}
	}
	/**
	 * Return the string representing the road.
	 * @return
	 * [this.start.getName()] to [this.end.getName()]
	 */
	@Override
	public String toString() {
		return ""+this.start.getName()+" to "+this.end.getName();
	}
	/**
	 * Return the point where is the starting point of the road.
	 * @return the point where is the starting point of the road.
	 */
	public Point2D.Float getStartingPoint() {
		return startingPoint;
	}
	/**
	 * Return the point where is the ending point of the road.
	 * @return the point where is the ending point of the road.
	 */
	public Point2D.Float getEndingPoint() {
		return endingPoint;
	}
	/**
	 * Return where the road is attached to the starting crossroad.
	 * @return where the road is attached to the starting crossroad.
	 */
	public Cardinal getStartPoint() {
		return startPoint;
	}
	/**
	 * Return where the road is attached to the ending crossroad.
	 * @return where the road is attached to the ending crossroad.
	 */
	public Cardinal getEndPoint()	
	{
		return this.endPoint;
	}
}
