package code;

import gui.frame.Displayable;

import interfaces.Intersectable;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
/**
 * This class represents an Intersection. It implements Intersectable because we need to know when a vehicle is on an Intersection
 * to give it the next destination. It is represented by a pixel.
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class Intersection extends Point2D.Float implements Intersectable, Serializable{
	private static final long serialVersionUID = 1L;
	//We only need to store exiting road
	private Set<Road> roads;
	//A reference to the crossroad containing the intersection.
	private transient WeakReference<CrossRoad> crossRoad;
	/**
	 * Initialize an Intersection whose entering and exiting roads and the crossroad where it is are passed by parameter.
	 * @param enteringRoad The road entering the crossroad.
	 * @param exitingRoad The road exiting the crossroad.
	 * @param crossRoad The crossroad where the intersection will be stored.
	 */
	Intersection(Road enteringRoad, Road exitingRoad, CrossRoad crossRoad){
		//Coordinates of the intersection are coordinates of the crossing of the two roads
		float[] tab = enteringRoad.getCoordinatesOfIntersectionWith(exitingRoad);
		this.x = tab[0];
		this.y = tab[1];
		this.roads = new HashSet<Road>();
		this.roads.add(exitingRoad);
		this.crossRoad = new WeakReference<CrossRoad>(crossRoad);
	}
	/**
	 * Initialize an Intersection whose entering (if isEnteringRoad is true) or exiting (if isEnteringRoad is false) road,
	 * coordinates and crossroad are passed by parameter.
	 * @param coordinates The coordinates of the intersection
	 * @param road The entering (if isEnteringRoad is true) or exiting (if isEnteringRoad is false) road
	 * @param isEnteringRoad Specify if the road is entering or exiting the crossroad.
	 * @param crossRoad The crossroad where the intersection will be stored.
	 */
	Intersection(Point2D.Float coordinates, Road road, boolean isEnteringRoad, CrossRoad crossRoad){
		this.x = (float) coordinates.getX();
		this.y = (float) coordinates.getY();
		this.roads = new HashSet<Road>();
		if (!isEnteringRoad)
			this.roads.add(road);
		this.crossRoad = new WeakReference<CrossRoad>(crossRoad);
	}
	/**
	 * Return the crossroad where the intersection is stored.
	 * @return the crossroad where the intersection is stored.
	 */
	public CrossRoad getCrossRoad() {
		return crossRoad.get();
	}
	/**
	 * Set the crossroad where the intersection will be stored.
	 * @param crossRoad the crossroad where the intersection will be stored.
	 */
	public void setCrossRoad(CrossRoad crossRoad) {
		this.crossRoad = new WeakReference<CrossRoad>(crossRoad);
	}
	/**
	 * Return a road exiting the intersection whose destination is not the crossroad passed by parameter and which is not
	 * parallel to the angle passed by parameter.
	 * @param cr The crossroad where the road has to not end. 
	 * @param angle The angle of the road has to be different (+/- PI/2) 
	 * @return the matching road (if possible, otherwise null)
	 */
	public Road getAnyRoadDifferentFrom(CrossRoad cr, Displayable angle){
		Road temp = null;
		Iterator<Road> it = this.roads.iterator();
		temp = it.next();
		while (temp.getEnd()==cr || temp.getAngleModuloPI()==angle.getAngleModuloPI())
		{
			if (it.hasNext())
				temp = it.next();
			else
				break;
		}
		if (!it.hasNext() && (temp.getEnd()==cr ||temp.getAngleModuloPI()==angle.getAngleModuloPI()))
			return null;
		return temp;
	}
	/**
	 * Return a road whose destination is the crossroad passed by parameter.
	 * @param end The crossroad where the road end.
	 * @return the road whose destination is the crossroad passed by parameter (if it exists).
	 */
	public Road getRoadTo(CrossRoad end){
		if (roads==null || end==null)
			return null;
		for (Road road : roads)
		{
			if (road.getEnd().getName().equals(end.getName()))
				return road;
			
		}
		return null;
	}
	/**
	 * Add a road to the intersection. It has to be an exiting road !
	 * @param road The road to be added to the intersection.
	 */
	public void addRoad(Road road){
		this.roads.add(road);
	}
	/**
	 * Set all exiting roads to the intersection.
	 * @return all exiting roads to the intersection.
	 */
	public Set<Road> getRoads(){
		return this.roads;   
	}
	/**
	 * Test whether two Intersections are equals or not. They are equals if their coordinates are equals.
	 * @param i The Intersection to be tested with the current Intersection.
	 * @return
	 */
	public boolean equals(Intersection i){
		return (this.x==i.getX() && this.y==i.getY());
	}
	/**
	 * Merge two intersections. It will add to the current intersection all exiting roads from the intersection passed by parameter.
	 * @param in The intersection to be merged with the current intersection.
	 */
	public void merge(Intersection in){
		this.roads.addAll(in.getRoads());
	}
	/**
	 * @return false;
	 */
	@Override
	public boolean intersectWith(Intersectable inter){return false;}
	/**
	 * Return the point where the intersection is.
	 * @return the point where the intersection is.
	 */
	@Override
	public Point2D.Float[] getPoints() {
		Point2D.Float[] pt = new Point2D.Float[1];
		pt[0]=new Point2D.Float(this.x, this.y);
		return pt;
	}
	/**
	 * Return the string representing the crossroad.
	 * @return
	 * ([this.x],[this.y])
	 * 		[this.roads]
	 * [this.isFinal()]
	 */
	@Override
	public String toString() {
		return "("+(int)x+","+(int)y+")"+"\n\t"+roads+isFinal();
	}
	/**
	 * Return whether the intersection is final or not (contains no exiting road, i.e. when a dead-end).
	 * @return
	 * <ul>
	 * <li>true, if the intersection is final
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean isFinal() 
	{
		return this.roads.size()==0;
	}
}
