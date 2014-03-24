package code;

import gui.frame.Displayable;
import interfaces.CarObservable;
import interfaces.CarObserver;
import interfaces.Intersectable;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
/**
 * This class represents a vehicle in a train. It inherits from Displayable which means it can be displayed on screen,
 * implements Intersectable and CarObservable which means it can interact with other classes implementing Intersectable when
 * they intersect themselves and means that they can communicate with the CarObserver to gather information or notify the
 * CarObserver that a change occurred in the train.
 * 
 * @see Displayable
 * @see Intersectable
 * @see CarObservable
 * @see CarObserver
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class Car extends Displayable implements Intersectable, CarObservable{
	/**
	 * This enumeration limits the choice for the user to 3 different speeds for the vehicle:
	 * <ul>
	 * <li>SLOW (1.2 times slower than NORMAL)
	 * <li>NORMAL
	 * <li>FAST (1.2 times faster than NORMAL)
	 * </ul>
	 * 
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 *
	 * @version v0.1
	 */
	public enum Speed{
		SLOW(0.1f/1.2f), NORMAL(0.1f), FAST(0.1f*1.2f);
		float speed;
		/**
		 * Create a new speed with its value in parameter.
		 * 
		 * @param speed The value of the speed.
		 */
		Speed(float speed){
			this.speed = speed;
		}
		/**
		 * Return the value of the speed.
		 * 
		 * @return the value of the speed.
		 */
		public float getValue() {
			return speed;
		}
	}
	private static final long serialVersionUID = 1L;
	//Is the vehicle running?
	private boolean state=true;
	//Retrieve the last intersection on which we drove (avoid infinite loop when testing intersections)
	private Intersection traversedIntersection;
	/* Intermediate: next Intersection
	 * Destination: Intersection which is the end of the trip*/
	private CrossRoad destination, intermediate;
	private Speed speed=Speed.NORMAL;
	/*The vehicle has a reference on the train it's currently attached.*/
	private transient WeakReference<Train> train;
	private ArrayList<CarObserver> observer;
	/*Be sure that we don't stop a vehicle while it's running on traffic lights.*/
	private boolean isTraversingTrafficLights=false;
	/*Variables for the crash animation.*/
	private long TIME_BEFORE_DEATH = 2000;
	private long hourOfDeath = 0;
	/*Boolean stating whether the vehicle has to quit the current train (this is set by user's interaction with the program.)*/
	public boolean forceQuit = false;
	/**
	 * Create a new vehicle with a starting and a destination cross road.
	 * 
	 * @param start The cross road from where the vehicle will start.
	 * @param destination The cross road to where the vehicle SHOULD end (can be modified if collisions or user's interaction
	 * @param graph The graph of the map (needed for finding the next intermediate cross road.
	 */
	public Car(CrossRoad start, CrossRoad destination, Graph graph){
		Random random = new Random();
		List<Intersection> itList = new ArrayList<Intersection>(start.getIntersections());
		//Find the next cross road on the shortest path from start to destination (see Graph)
		CrossRoad intermediate = graph.getNextIntermediate(start, destination);
		this.destination = destination;
		this.intermediate = intermediate;
		observer = new ArrayList<CarObserver>();
		Road r;
		//Assign a random image to the vehicle
		switch(random.nextInt(6)){
			case 0: this.setImagePath("./images/sprites/vehicule_bleu_c.png");break;
			case 1: this.setImagePath("./images/sprites/vehicule_rose_c.png");break;
			case 2: this.setImagePath("./images/sprites/vehicule_gris_c.png");break;
			case 3: this.setImagePath("./images/sprites/vehicule_camion_c.png");break;
			case 4: this.setImagePath("./images/sprites/vehicule_police_c.png");break;
			case 5: this.setImagePath("./images/sprites/vehicule_vert_c.png");break;
		}
		//Search for an Intersection which has a road from start to the next intermediate
		for (Intersection it : itList)
		{
			if ((r=it.getRoadTo(intermediate))!=null)
			{
				//Found road leading to next intermediate
				this.vector = r.getVector();
				double temp = this.getAngle();
				this.coordinates = new float[2];
				/*Set X & Y coordinates*/
				float x, y;
				x=(float) (-this.image.getWidth()/2);
				y = (float) (-this.image.getHeight()/2);
				this.coordinates[0]=(float) (x*Math.cos(temp)-y*Math.sin(temp)+it.getX());
				this.coordinates[1]= (float) (x*Math.sin(temp)+y*Math.cos(temp)+it.getY());
				traversedIntersection = it;
				break;
			}
		}
		//Create the polygon needed for testing collision between two Intersectables
		createPolygon();
	}
	/**
	 * Create the polygon circumscribing the vehicle
	 */
	private void createPolygon() {
		//Reset polygon
		((Polygon)this).reset();
		/*The polygon is created from the RELATIVE upper left corner of the vehicle and in clockwise. Relative means that
		 * if the vehicle is for example horizontal to the right, the upper left corner is not the one you're seeing but
		 * the upper right corner (this is what we meant: relative to the image representing the vehicle) */
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
	 * Set the train the vehicle is currently attached.
	 * @param t the train the vehicle is currently attached.
	 */
	public void setTrain(Train t){
		this.train = new WeakReference<Train>(t);
	}
	/**
	 * Return the next intermediate on the trip from start to destination.
	 * @return the next intermediate on the trip from start to destination.
	 */
	public CrossRoad getIntermediate() {
		return this.intermediate;
	}
	/**
	 * Return the coordinates of the relative upper left corner of the vehicle (X and Y).
	 * @return the coordinates of the relative upper left corner of the vehicle (X and Y).
	 */
	public float[] getCoordinates() {
		return this.coordinates;
	}
	
	/**
	 * Set the coordinates of the relative upper left corner of the vehicle (a float[2] with X and Y) and update the polygon.
	 * @param coordinates the coordinates of the relative upper left corner of the vehicle (a float[2] with X and Y).
	 */
	public void setCoordinates(float[] coordinates) {
		this.coordinates = coordinates;
		createPolygon();
	}
	/**
	 * Set the speed of the vehicle.
	 * @param speed The speed to be set.
	 */
	public void setSpeed(Speed speed) {
		this.speed = speed;
	}
	/**
	 * Return the speed of the vehicle
	 * @return the speed of the vehicle
	 */
	public Speed getSpeed() {
		return speed;
	}
	/**
	 * Set the next intermediate.
	 * @param cr The next intermediate of the trip from the current cross road to the destination.
	 */
	public void setIntermediate(CrossRoad cr)
	{
		this.intermediate = cr;
	}
	/**
	 * Update the position of the vehicle thanks to its speed and state.
	 * @param time The time (in ms) elapsed between two updates.
	 */
	public void updatePosition(float time) {
		//Checking the vehicle is not crashed
		if (this.imagePath.contains("explosion"))
		{
			//If it is, do the animation of crash
			this.hourOfDeath +=time;
			if (this.hourOfDeath>=4*this.TIME_BEFORE_DEATH/5)
				this.setImagePath("./images/sprites/explosion_4.png");
			else if (this.hourOfDeath>=3*this.TIME_BEFORE_DEATH/5)
				this.setImagePath("./images/sprites/explosion_3.png");
			else if(this.hourOfDeath>=2*this.TIME_BEFORE_DEATH/5)
				this.setImagePath("./images/sprites/explosion_2.png");
			if (this.hourOfDeath>this.TIME_BEFORE_DEATH)
			{
				//The animation is finished 
				if (this.getTrain().hasOnlyOneCar())
					//The train contains only this car, we have to remove the train from the global train list.
					for (CarObserver obs : observer)
						obs.removeTrain(this.getTrain());
				else
				{
					//The train contains other vehicles, we have to delete the crashed car from the list of owned cars in the train
					ArrayList<Car> temp = new ArrayList<Car>(this.getTrain().getCars());
					temp.remove(this);
					this.getTrain().setCars(temp);
				}
			}
			return;
		}
		if (!state)
			return;
		//The vehicle is running
		//Compute the distance the vehicle should have done if the movement was linear (the distance between the two updates).
		double distance = Math.sqrt(Math.pow(this.vector[0], 2)+Math.pow(this.vector[1], 2));
		try{
			this.coordinates[0]+=this.vector[0]*this.speed.getValue()*time/distance;
			this.coordinates[1]+=this.vector[1]*this.speed.getValue()*time/distance;
		}catch(ArithmeticException e){}
		//Update the polygon
		createPolygon();
	}
	/**
	 * Crash the vehicle. It launches the animation of crash and stops the car.
	 */
	public void crash()
	{
		//Update the image to the first image in the animation of crash
		this.setImagePath("./images/sprites/explosion_1.png");
		//The vehicle can't run anymore
		this.state=false;
		//Notify observer for its death
		for (CarObserver obs : observer)
			obs.crash();			
			
	}
	/**
	 * Return the destination of the trip.
	 * @return the destination of the trip.
	 */
	public CrossRoad getDestination() {
		return destination;
	}
	/**
	 * (User's interaction) Force the vehicle to quit the current train even if it induces a wrong way.
	 */
	public void forceQuit(){
		forceQuit = true;
	}
	/**
	 * Quit the current train. Create a new one whose head is the quitting vehicle.
	 */
	private void quit() {
		//Retrieve all vehicles after the quitting vehicle and add them to the newly created train
		ArrayList<Car> tab = new ArrayList<Car>();
		List<Car> cars = this.getTrain().getCars();
		for(int i = cars.indexOf(this); i < cars.size(); ++i)
			tab.add(cars.get(i));
		//Remove vehicles of the newly created train from the original train
		cars.removeAll(tab);
		this.getTrain().setCars(tab);
		//Notify the observer for a train creation
		for(CarObserver obs : this.observer)
			obs.addTrain(new Train(cars));
	}
	/**
	 * Return the train the vehicle is currently attached.
	 * @return the train the vehicle is currently attached.
	 */
	public Train getTrain()
	{
		return this.train.get();
	}
	/**
	 * Find if there is an intersection with an Intersectable object. There exists one if at least one of the points of the
	 * polygon of the vehicle or the Intersectable object is contained in the polygon of the other. If there exists one, if
	 * the Intersectable object is:
	 * <ul>
	 * <li>itself, do nothing
	 * <li>a vehicle, call doIntersectAction(Car)
	 * <li>a traffic lights, call doIntersectAction(TrafficLights)
	 * <li>an Intersection (the exact point where two roads cross each other), call doIntersectAction(Intersection)
	 * </ul>
	 * @param inter The Intersectable object we want to know if it intersects the vehicle.
	 * 
	 * @return
	 * <ul>
	 * <li>true, if intersection between those two objects
	 * <li>false, otherwise
	 * </ul>
	 */
	public boolean intersectWith(Intersectable inter) 
	{
		boolean b = false;
		//Be sure that we are not testing intersection on itself
		if(this==inter)
			return false;
		/*Check that at least one of the points of the polygon of the Intersectable object is contained in the polygon 
		 * of the vehicle.*/
		for (Point2D.Float p : inter.getPoints())
			if (this.contains(p))
				b = true;
		if(!b)
			if (inter.getClass()==Car.class)
				/*If it's not the case, and if the Intersectable object is a vehicle, check that at least one
				 *  of the points of the polygon of the vehicle is contained in the polygon of the Intersectable object
				 *  (which is a vehicle).*/ 
				for (Point2D.Float p : this.getPoints())
					if (((Car)inter).contains(p))
						b = true;
		if (!b)
			return false;
		if (inter.getClass()==Car.class)
			this.doIntersectAction((Car)inter);
		else if (inter.getClass()==TrafficLights.class)
			this.doIntersectAction((TrafficLights)inter);
		else if (inter.getClass()==Intersection.class)
			this.doIntersectAction((Intersection)inter);
		return true;
	}
	@Override
	public Point2D.Float[] getPoints() {
		Point2D.Float[] pt = new Point2D.Float[this.npoints];
		for (int i=0; i<this.npoints; ++i)
			pt[i]=new Point2D.Float(this.xpoints[i], this.ypoints[i]);
		return pt;
	}
	/**
	 * Manage intersection with an other vehicle.
	 * @param car The vehicle intersecting the current vehicle.
	 */
	private void doIntersectAction(Car car)
	{
		if (this.imagePath.contains("explosion"))
			//Do nothing if intersection with a crashed vehicle.
			return;
		if (this.getVector()[0]==-car.getVector()[0] && this.getVector()[1]==-car.getVector()[1])
			//Do nothing if vehicles are on parallel roads.
			return;
		if (this.getTrain().isJoining(car.getTrain()) || car.getTrain().isJoining(this.getTrain()))
			//Do nothing if the vehicle are already joining the train in front.
			return;
		//If train of the first vehicle and train of the second vehicle have several vehicles.
		if(!this.getTrain().hasOnlyOneCar() && !car.getTrain().hasOnlyOneCar())
		{
			//Trains intersections
			if (this.contains(car.getCoordinates()[0], car.getCoordinates()[1]))
				//The train owning the current vehicle is behind the vehicle passed by parameter
			{
				this.getTrain().waitUntilTrainPasses(car.getTrain());
				car.getTrain().waitUntilTrainPasses(null);
			}
			else
				//The train owning the current vehicle is in front of the vehicle passed by parameter
			{
				car.getTrain().waitUntilTrainPasses(this.getTrain());
				this.getTrain().waitUntilTrainPasses(null);
			}
		}
		else
		{
			//At least, one of the trains contains only one vehicle
			if (this.getTrain().hasOnlyOneCar())
			{
				if (car.getTrain().hasOnlyOneCar())
				{
					//Both trains have only one vehicle
					if (this.getVector()[0]==car.getVector()[0] && this.getVector()[1]==car.getVector()[1])
					{
						if (this.getAngle()>=Math.toRadians(89) && this.getAngle()<=Math.toRadians(138))
							//They are on the same road
						{
							if (car.contains(this.getCoordinates()[0], this.getCoordinates()[1]))
								//The train owning the current vehicle is in front of the vehicle passed by parameter
							{
								this.getTrain().join(car.getTrain());
								car.getTrain().join(null);
							}
							else
							{ 
								car.getTrain().join(this.getTrain());
								this.getTrain().join(null);
							}
						}
						else 
						{
							if (this.contains(car.getCoordinates()[0], car.getCoordinates()[1]))
								//The train owning the current vehicle is behind the vehicle passed by parameter
							{
								this.getTrain().join(car.getTrain());
								car.getTrain().join(null); 
							} 
							else
							{
								car.getTrain().join(this.getTrain());
								this.getTrain().join(null);
							}
						}
					}
					else
						//Both trains are on different roads
					{
						this.crash();
						car.crash();
					}
				}
				else
					//The train owning the current vehicle is the only one having only one vehicle
				{
					if (this.getVector()[0]==car.getVector()[0] && this.getVector()[1]==car.getVector()[1])
					{
						//They are on the same road
						if (speed==Speed.SLOW && state)
							//Vehicle in front has a slow speed
						{
							/*Split the train in two: one part of vehicles in front of the crashed vehicle and one part of
							 * vehicles behind the crashed vehicle*/
							this.crash();
							car.quit();
							car.crash();
						}
						else
							//The vehicle is stopped or fast
						{
							car.getTrain().join(this.getTrain());
							this.getTrain().join(null);
						}
					}
					else
						//Trains are on different roads
					{
						/*Split the train in two: one part of vehicles in front of the crashed vehicle and one part of
						 * vehicles behind the crashed vehicle*/
						this.crash();
						car.quit();
						car.crash();
					}
				}
				return;
			}
			//The train of the vehicle passed by parameter is the only one having only one vehicle.
			if (car.getTrain().hasOnlyOneCar())
			{
				if (this.getVector()[0]==car.getVector()[0] && this.getVector()[1]==car.getVector()[1])
				{
					//They are on the same road
					if (speed==Speed.SLOW && state)
						//Vehicle in front has a slow speed
					{
						/*Split the train in two: one part of vehicles in front of the crashed vehicle and one part of
						 * vehicles behind the crashed vehicle*/
						car.crash();
						this.quit();
						this.crash();
					}
					else
						//The vehicle is stopped or fast
					{
							car.getTrain().join(this.getTrain());
							this.getTrain().join(null);
					}
				}
				else
				{
					//The trains are on different roads
					/*Split the train in two: one part of vehicles in front of the crashed vehicle and one part of
					 * vehicles behind the crashed vehicle*/
					car.crash();
					this.quit();
					this.crash();
				}
			}
		}
	}
	/**
	 * Check whether the vehicle is currently traversing a traffic lights or not.
	 * @return
	 * <ul>
	 * <li>true, if traversing.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean isTraversingTrafficLights(){
		return this.isTraversingTrafficLights;
	}
	/**
	 * Check whether is currently traversing the Intersection passed by parameter.
	 * @param inter The Intersection we want to test if the vehicle is already traversing it.
	 * @return
	 * <ul>
	 * <li>true, if traversing.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean isTraversing(Intersection inter)
	{
		return inter==traversedIntersection;
	}
	/**
	 * Manage intersection with an Intersection.
	 * @param inter The Intersection currently intersecting the vehicle.
	 */
	private void doIntersectAction(Intersection inter)
	{
		if(isTraversing(inter) || !state)
			//Check whether the Intersection is already been traversing or the vehicle is stopped.
			return;
		//Find the next intermediate
		CrossRoad intermediate = observer.get(0).newIntermediate(this.intermediate, destination);
		Road r;
		if(forceQuit)
		{
			//If the head of the train goes in the same direction as the vehicle, we chose any other road
			if (this.getTrain().getHead().getIntermediate()==intermediate)
				r = inter.getAnyRoadDifferentFrom(intermediate, this);
			else
				//Otherwise, the road is the one that leads to the next intermediate
				r = inter.getRoadTo(intermediate);
		}
		else 
			//The road is the one that leads to the next intermediate
			r = inter.getRoadTo(intermediate);
		if(!forceQuit && inter.getCrossRoad()!=this.intermediate && inter.getCrossRoad()!=this.destination)
			/*If the vehicle isn't force quitting, its current cross road is neither the next intermediate nor its destination,
			 * 	we ignore the intersection.*/
				return;	
		if ((inter.isFinal() && this.intermediate==null) || this.destination == inter.getCrossRoad())
		{
			/*If the intersection is final (no exiting road) and there doesn't exist a next intermediate (because it is a dead-end)
			 * or the vehicle reached its destination, we delete the vehicle.
			 */
			this.delete();
			return;
		}
		//Check if the intermediate has an exiting road to the next intermediate.
		Road rtemp = null;
		for (Intersection inte : this.intermediate.getIntersections())
			if (inte.getRoadTo (intermediate)!=null)
			{
				rtemp = inte.getRoadTo(intermediate);
				break;
			}
		if ((rtemp==null && inter.isFinal()))
		{
			//There is no road to the next intermediate and the intersection is final: deleting the vehicle
			this.delete();
			return;
		}
		//If the next intermediate doesn't exist (consequence of force quitting in a forbidden road (a dead-end))
		if(intermediate == null)
		{
			//Update the vehicle on any road owned by the visited intersection
			this.intermediate = intermediate;
			Iterator<Road> it = inter.getRoads().iterator();
			if (!it.hasNext())
				return;
			r = it.next();
			this.intermediate = r.getEnd();
			float x, y, temp=(float) r.getAngle();
			x=(float) (-this.image.getWidth()/2.f);
			y = (float) (-this.image.getHeight()/2.f);
			this.coordinates[0]=(float) (x*Math.cos(temp)-y*Math.sin(temp)+inter.getX());
			this.coordinates[1]= (float) (x*Math.sin(temp)+y*Math.cos(temp)+inter.getY());
			this.vector = r.getVector();
			createPolygon();
			forceQuit=false;
			this.isTraversingTrafficLights = false;
			return;
		}
		//If there is no road to the next intermediate
		if(r==null)
		{
			//Check if the cross road where the Intersection is, owned an Intersection which has a road to the next intermediate.
			Road temp = null;
			for (Intersection inte : this.intermediate.getIntersections())
				if (inte.getRoadTo(intermediate)!=null)
				{
					temp = inte.getRoadTo(intermediate);
					break;
				}
			if (temp!=null && temp.getAngleModuloPI()==this.getAngleModuloPI() && temp.getAngle()!=this.getAngle())
			{
				//There exists a road to (which is parallel to the current road)
				double xV=inter.getX(), yV=inter.getY()-1, xO=inter.getX(), yO=inter.getY(), xE, yE, a2, b2, c2, angle;
				ArrayList<Intersection> itList = new ArrayList<Intersection>();
				//Search for all intersections which are on the current road.
				for (Intersection it : inter.getCrossRoad().getIntersections())
				{
					xE = it.getX();
					yE = it.getY();
					a2 = (xO-xE)*(xO-xE)+(yO-yE)*(yO-yE);
					b2 = (yO-yV)*(yO-yV);
					c2 = (xV-xE)*(xV-xE)+(yV-yE)*(yV-yE);
					angle = Math.acos((-(c2)+a2+b2)/(2*Math.sqrt(a2)*Math.sqrt(b2)));
					String s = Double.toString(angle);
					s = s.substring(0, (s.length()>=15)?15:s.length());
					angle = Double.parseDouble(s);
					itList.add(inter);
					if (angle==this.getAngle())
						itList.add(it);
				}
				//Check if one of the intersections on the road allows the vehicle to reach its destination
				CarObserver obs = this.observer.get(0);
				for (Intersection it : itList)
				{
					for (Road road : it.getRoads())
						if (obs.newIntermediate(road.getEnd(), this.destination)!=null)
						{
							//The intersection on the road allows the vehicle to reach its destination
							r = road;
							intermediate=road.getEnd();
							break;
						}
					if (r!=null)
						break;
				}
				if (r==null)
				{
					//We didn't find any road: deletion of the vehicle (it cannot go to any road!)
					this.delete();
					return;
				}
			}
		}
		//If the found road or the current cross road is null (dead-end)
		if(r!=null || this.intermediate==null)
		{
			//Be sure we didn't compute again intersection with this Intersection
			this.traversedIntersection = inter;
			if(forceQuit)
			{
				//Force quitting: give it any road.
				this.intermediate = r.getEnd();
				forceQuit = false;
			}
			else
				this.intermediate = intermediate;
			//Update the vehicle and center it on the intersection.
			float x, y, temp=(float) r.getAngle();
			x=(float) (-this.image.getWidth()/2.f);
			y = (float) (-this.image.getHeight()/2.f);
			this.coordinates[0]=(float) (x*Math.cos(temp)-y*Math.sin(temp)+inter.getX());
			this.coordinates[1]= (float) (x*Math.sin(temp)+y*Math.cos(temp)+inter.getY());
			this.vector = r.getVector();
			createPolygon();
			this.isTraversingTrafficLights = false;
			forceQuit=false;
			//If the train has a different way from the current vehicle, do it quit it.
			if(this.getTrain().getNextIntermediateDestination() != this.getIntermediate())
				this.quit();
		}
	}
	/**
	 * Delete the car when reaching destination or dead-end. Update the train (the head is mandatory reaching firstly the
	 * destination or the dead-end) by deleting its head.
	 */
	private void delete() {
		if (this.getTrain().hasOnlyOneCar())
			for(CarObserver obs : this.observer)
				obs.removeTrain(this.getTrain());
		else
		{
			ArrayList<Car> temp = new ArrayList<Car>(this.getTrain().getCars());
			temp.remove(this.getTrain().getHead());
			this.getTrain().setCars(temp);
		}
		//Notify observer that a vehicle reached its destination
		for (CarObserver obs : observer)
			obs.reach();
	}
	/**
	 * Manage intersection with a traffic lights.
	 * @param traff The traffic lights intersecting the current vehicle
	 */
	private void doIntersectAction(TrafficLights traff)
	{
		//Traffic lights on the same road as the vehicle
		if (traff.getVector()[0]==this.getVector()[0] && traff.getVector()[1]==this.getVector()[1])
		{
			if(!isTraversingTrafficLights && traff.isGreen())
			//Traffic lights is green and not already being traversed
			{
				isTraversingTrafficLights = true;
				state = true;
			}
			else if(!isTraversingTrafficLights && !traff.isGreen())
			//Traffic lights is red and not already being traversed
			{
				state = false;
				//If the vehicle is not the head of its train, do it quit it.
				if(this.getTrain().getHead() != this)
					this.quit();
			}
		}
	}	
	/**
	 * Return the Intersection which is being traversed.
	 * @return the Intersection which is being traversed.
	 */
	public Intersection getTraversedIntersection() {
		return traversedIntersection;
	}
	@Override
	public void addCarObserver(CarObserver obs) 
	{
		this.observer.add(obs);
	}
	/**
	 * Return the state of the vehicle (running or not).
	 * @return the state of the vehicle (running or not).
	 */
	public boolean getState() {
		return state;
	}
	/**
	 * Return the list of observers for the vehicle.
	 * @return the list of observers for the vehicle.
	 */
	public ArrayList<CarObserver> getObserver() {
		return observer;
	}
	@Override
	public void removeObserver() {
		observer = new ArrayList<CarObserver>();
	}
	@Override
	public void notifyObserver() {
		for (CarObserver obs : observer)
			obs.notify();
	}
}
