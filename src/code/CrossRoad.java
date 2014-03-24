package code;

import gui.frame.Displayable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import code.Road.Cardinal;
/**
 * This class represents a crossroad. It can have entering and exiting roads and will set their intersections.
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class CrossRoad extends Displayable{
	private static final long serialVersionUID = 1L;
	//The set of roads contained in the crossroad.
	private Road[] roads;
	//The set of intersections contained in the crossroad.
	private List<Intersection> intersections;
	private String name;
	//Has the crossroad only entering roads?
	private boolean isFinal=true;
	//Has the crossroad only exiting roads?
	private boolean isSource=true;
	/**
	 * Initialize the crossroad object with the name and coordinates passed by parameter and the image.
	 * @param name The name of the crossroad (will be used as an ID).
	 * @param coordinates The coordinates of the upper left corner of the crossroad.
	 */
	CrossRoad(String name, float[] coordinates){
		this.name = name;
		this.coordinates = coordinates;
		this.roads = new Road[8];
		this.intersections = new CopyOnWriteArrayList<Intersection>();
		this.setImagePath("./images/sprites/crossroad.png");
	}
	/**
	 * Return the name (its ID) of the crossroad.
	 * @return the name (its ID) of the crossroad.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Return the list of roads contained in the crossroad.
	 * @return the list of roads contained in the crossroad.
	 */
	public List<Road> getRoads() {
		List<Road> roads = new ArrayList<Road>();
		for (Road r : this.roads)
			if (r!=null)
				roads.add(r);
		return roads;
	}
	/**
	 * Set the name (its ID) of the crossroad.
	 * @param name the name (its ID) of the crossroad.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Add an entering road to the set of roads contained in the crossroad. You have to specify where the road is attached
	 * to the crossroad thanks to the cardinal. Each road is placed in a dedicated place in the set of roads considering
	 *  its cardinal.
	 * @param road The road which is entering the crossroad.
	 * @param c The cardinal where the road is attached.
	 */
	public void addEnteringRoad(Road road, Cardinal c){
		int index;
		if (c==Cardinal.NORTH)
			index=0;
		else if (c== Cardinal.EAST)
			index=2;
		else if (c==Cardinal.SOUTH)
			index=4;
		else
			index=6;
		this.roads[index] = road;
		//There exists now an entering road, therefore the crossroad is not a source anymore.
		isSource= false;
		
	}
	/**
	 * Add an exiting road to the set of roads contained in the crossroad. You have to specify where the road is attached
	 * to the crossroad thanks to the cardinal. Each road is placed in a dedicated place in the set of roads considering
	 *  its cardinal.
	 * @param road The road which is exiting the crossroad.
	 * @param c The cardinal where the road is attached.
	 */
	public void addExitingRoad(Road road, Cardinal c){
		int index;
		if (c==Cardinal.NORTH)
			index=1;
		else if (c== Cardinal.EAST)
			index=3;
		else if (c==Cardinal.SOUTH)
			index=5;
		else
			index=7;
		this.roads[index] = road;
		//There exists now an exiting road, therefore the crossroad is not final anymore.
		isFinal = false;
	}
	/**
	 * Return the list of intersections contained in the crossroad.
	 * @return the list of intersections contained in the crossroad.
	 */
	public List<Intersection> getIntersections() {
		return intersections;
	}
	@Override
	/**
	 * Return a string representing the crossroad.
	 * @return
	 * [this.name]
	 * 		PosX: [this.coordinates[0]]
	 * 		PosY: [this.coordinates[1]]
	 * 		--->isFinal: [this.isFinal] | isSource: [this.isSource]
	 * [this.intersections]
	 */
	public String toString() {
		return ""+this.name+"\n\tPosX: "+this.coordinates[0]+"\n\tPosY: "+this.coordinates[1]+"\n--->isFinal: "+isFinal+" | isSource: "+isSource+"\n"+intersections;
	}
	/**
	 * Return a boolean whether the crossroad is final or not.
	 * @return
	 * <ul>
	 * <li>true, if the crossroad is final
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean isFinal() {
		return isFinal;
	}
	/**
	 * Return a boolean whether the crossroad is a source or not.
	 * @return
	 * <ul>
	 * <li>true, if the crossroad is a source
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean isSource() {
		return isSource;
	}
	/**
	 * Set all intersections between roads contained in the crossroad.
	 */
	public void fix(){
		if (isSource)
		{
			/*If the crossroad is a source, we add for each exiting road an intersection for allowing us to put newly created vehicles
			 * on the crossroad.*/
			for (int i=1; i<8; i+=2)
				if (this.roads[i]!=null)
					intersections.add(new Intersection(this.roads[i].getStartingPoint(), this.roads[i], false, this));
		}
		else if (isFinal)
		{
			/*If the crossroad is final, we add for each entering road an intersection for allowing us to know when we have to
			 * destroy a vehicle (it reached a dead-end or its destination).*/
			for (int i=0; i<8; i+=2)
				if (this.roads[i]!=null)
					intersections.add(new Intersection(this.roads[i].getEndingPoint(), this.roads[i], true, this));
		}
		else
		{
			/*Search for a road ending in the crossroad but crossing no roads.*/
			boolean isFinal=true;
			for (int i=0; i<8; i+=2)
			{
				if (this.roads[i]!=null)
				{	
					for (int j=1; j<8; j+=2)
						if (this.roads[j]!=null)
							if (this.roads[i].isCrossed(this.roads[j]) || (i+5)%8==j)
							{
								isFinal=false;
								break;
							}
					if (isFinal)
						//The road crosses no roads, we then add an intersection to know when we have to destroy a vehicle (a dead-end).
						this.intersections.add(new Intersection(this.roads[i].getEndingPoint(), this.roads[i], true, this));
				}
			}
			/*Add intersections for each entering road crossing an exiting road*/	
			Intersection temp, in;
			boolean equal=false;
			Iterator<Intersection> it = this.intersections.iterator();
			for (int j=0; j<8; j+=2)
			{
				if (this.roads[j]!=null)
				{
					for (int i=1; i<8; i+=2)
					{
						//Check if the roads are parallels or not.
						if (this.roads[i]!=null && this.roads[i].getAngleModuloPI()!=this.roads[j].getAngleModuloPI())
						{
							temp=new Intersection(this.roads[j], this.roads[i], this);
							while(it.hasNext() && !equal)
							{
								//If a newly created intersection has the same coordinates as an other intersections: merge.
								in = it.next();
								if (in.equals(temp))
								{
									equal=true;
									in.merge(temp);
								}
							}
							if (!equal)
								this.intersections.add(temp);
							equal=false;
						}
						else if (this.roads[i]!=null && this.roads[i].getAngleModuloPI()==this.roads[j].getAngleModuloPI() && (j+1)%8!=i)
							/*If the roads are parallels but are on the opposite side of the crossroad (a possible exiting road
							 * considering the entering road), add an intersection*/
							this.intersections.add(new Intersection(this.roads[i].getStartingPoint(), this.roads[i], false, this));
						it = this.intersections.iterator();
					}
				}
			}
		}
	}
}
