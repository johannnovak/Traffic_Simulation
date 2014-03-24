package code;


import interfaces.CarObserver;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import code.Car.Speed;

/**
 * This class represents a train. It contains a list of vehicles which are in the train. It can join an other train or wait a train to quit the crossroad
 * in order to avoid collision.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class Train implements Serializable{

	private static final long serialVersionUID = 1L;
	private CopyOnWriteArrayList<Car> cars;
	private Train wait=null, join=null;
	/**
	 * Create new train with vehicles in the list passed by parameter.
	 * @param tab The list of vehicles to be added to the list of vehicles in the train.
	 */
	public Train(List<Car> tab)
	{
		this.cars = new CopyOnWriteArrayList<Car>();
		for (Car car : tab)
		{
			this.cars.add(car);
			car.setTrain(this);
		}     
	}
	/**
	 * Create new train with vehicles in the list passed by parameter.
	 * @param tab The list of vehicles to be added to the list of vehicles in the train.
	 */
	public Train(Car... cars) {
		this.cars = new CopyOnWriteArrayList<Car>();
		for (Car car : cars)
		{
			this.cars.add(car);
			car.setTrain(this);
		}
	}
	/**
	 * Check if the train contains only one vehicle.
	 * @return
	 * <ul>
	 * <li>true, if the train contains only one vehicle.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean hasOnlyOneCar(){
		return this.cars.size()==1;
	}
	/**
	 * Return the next intermediate of the current head of the train.
	 * @return the next intermediate of the current head of the train.
	 */
	public CrossRoad getNextIntermediateDestination()
	{
		return this.getHead().getIntermediate();
	}
	/**
	 * Add vehicles to the list of vehicles in the train.
	 * @param cars2 The list of vehicles to be added to the list of vehicles in the train.
	 */
	public void addCars(List<Car> cars2) {
		for (Car c : cars2){
			c.setTrain(this);
		}
		this.cars.addAll(cars2);
	}
	/**
	 * Set the list of vehicles in the train.
	 * @param cars The list of vehicles which will be the list of vehicles in the train.
	 */
	public void setCars(List<Car> cars) {
		this.cars = new CopyOnWriteArrayList<Car>(cars);
	}
	/**
	 * Update positions of each vehicle in the train thanks to the time elapsed between two updates.
	 * @param time The time elapsed between two updates.
	 */
	private void updatePositions(float time){
		if(this.cars.size()==0)
			return;
		//Update the head of the train.
		cars.get(0).updatePosition(time);
		//Check if the head of the train is running.
		if (this.getHead().getState())
			//Head running = update all other vehicles in the train.
			for (Car car : cars)
				if (car!=this.getHead())
					car.updatePosition(time);
	}
	/**
	 * Ask the train to join an other train.
	 * @param train The train to be joined.
	 */
	public void join(Train train){
		if (join == train)
			//The train to be joined is the same as the current train
			return;
		join = train;
		//If the current train contains only one vehicle, set its speed to NORMAL.
		if (this.hasOnlyOneCar())
			this.cars.get(0).setSpeed(Speed.NORMAL);
		if (join==null)
			return;
		//If the train to be joined has only one vehicle, set its speed to NORMAL.
		if (train.hasOnlyOneCar())
			train.cars.get(0).setSpeed(Speed.NORMAL);
	}
	/**
	 * Make the current train joining the train to be joined.
	 */
	private void effectivlyJoin()
	{
		if (join==null)
			return;
		//For each vehicle in the train to be joined starting from the tail, add vehicles to the current train at the head.
		for (int i=join.cars.size()-1; i>=0; --i)
			this.cars.add(0,join.cars.get(i));
		for (Car c : join.cars)
			c.setTrain(this);
		//Notify observers of a train removing.
		for (CarObserver obs : cars.get(0).getObserver())
			obs.removeTrain(join);
	}
	/**
	 * Return the list of vehicles in the train.
	 * @return the list of vehicles in the train.
	 */
	public CopyOnWriteArrayList<Car> getCars(){
		return this.cars;
	}
	/**
	 * Return the head of the train.
	 * @return the head of the train.
	 */
	public Car getHead(){
		return this.cars.get(0);
	}
	/**
	 * Update positions of each vehicles in the train if possible.
	 * @param time The time elapsed between two updates.
	 */
	public void update(float time){
		//Check if there is a train to wait for.
		if (hasTrainPassed(wait))
		{
			this.wait = null;
			//Check if the train to be joined is not intersecting the current train (we have to wait for having space between two vehicles before joining)
			if (hasTrainPassed(join) && canJoin())
			{
				//Update each vehicle in the train.
				updatePositions(time);
				//Make trains joining each other.
				this.effectivlyJoin();
				join = null;
			}
		}
	}
	/**
	 * Return the train to be joined.
	 * @return
	 * <ul>
	 * <li>null, if there is no train to be joined.
	 * <li>the train to be joined.
	 * </ul>
	 */
	public Train getJoin() {
		return join;
	}
	/**
	 * Check if the current train is joining a train.
	 * @return
	 * <ul>
	 * <li>true, if joining a train.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean isJoining(){
		return join!=null;
	}
	/**
	 * Return the tail of the train.
	 * @return the tail of the train.
	 */
	public Car getTail(){
		return cars.get(cars.size()-1);
	}
	/**
	 * Check if the current train can join the train to be joined.
	 * @return
	 * <ul>
	 * <li>true, if it can join it.
	 * <li>false, otherwise.
	 * </ul>
	 */
	private boolean canJoin() {
		double distanceBetween=0;
		if (join==null)
			return true;
		if (join.cars.get(join.cars.size()-1).getTrain()!=join)
			return true;
		//Check if the train to be joined still exists (it could have crashed or reached its destination/dead-end).
		if(!this.getHead().getObserver().get(0).containsTrain(join))
			return true;
		/*Compute the needed space (diameter of the circumscribing circle) between two vehicles to be sure that a train can cross an other train 
		 * without crashing.*/
		double distance=Math.sqrt(Math.pow(this.cars.get(0).getImage().getWidth(),2)+Math.pow(this.cars.get(0).getImage().getHeight(),2));
		if(this.getHead().getIntermediate()==join.getTail().getIntermediate())
		{
			//If both trains have the same intermediate, test if the minimum distance is respected.
			distanceBetween = Math.sqrt(Math.pow(this.getHead().xpoints[0]-join.getTail().xpoints[0],2)+Math.pow(this.getHead().ypoints[0]-join.getTail().ypoints[0],2));//-distance;
			for(int i = 0; i < 4; ++i)
				for(int j = 0; j < 4; ++j)
					{
						distanceBetween = Math.min(distanceBetween, Math.sqrt(Math.pow(this.getHead().xpoints[j]-join.getTail().xpoints[i],2)+Math.pow(this.getHead().ypoints[j]-join.getTail().ypoints[i],2)));//-distance;
					}
			if(distanceBetween>distance)
				return true;
			return false;
		}
		if (this.getHead().getObserver().get(0).newIntermediate(this.getHead().getIntermediate(), this.getHead().getDestination())==join.getTail().getIntermediate())
		{
			/*If both trains do not have the same intermediate: compute the distance from the current train's head to the next intersection and add it
			 * to the distance from the next intersection to the tail of the train to be joined*/
			distanceBetween=Math.sqrt(Math.pow((this.getHead().xpoints[0]+(this.getHead().xpoints[1]-this.getHead().xpoints[0])/2.f)-join.getTail().getTraversedIntersection().x,2)+
					Math.pow((this.getHead().ypoints[0]+(this.getHead().ypoints[1]-this.getHead().ypoints[0])/2.f)-join.getTail().getTraversedIntersection().y,2));
			distanceBetween+=Math.sqrt(Math.pow((join.getTail().ypoints[3]+(join.getTail().ypoints[2]-join.getTail().ypoints[3])/2.f)-join.getTail().getTraversedIntersection().y,2)+
					Math.pow((join.getTail().xpoints[3]+(join.getTail().xpoints[2]-join.getTail().xpoints[3])/2.f)-join.getTail().getTraversedIntersection().x, 2));
			if (distanceBetween>distance)
				return true;
			return false;
		}
		return true;
	}
	/**
	 * Accelerate the speed of the train if it only contains one vehicle.
	 */
	public void accelerate() {
		if (this.hasOnlyOneCar())
		{
			if (this.getHead().getSpeed()==Speed.SLOW)
				this.getHead().setSpeed(Speed.NORMAL);
			else
				this.getHead().setSpeed(Speed.FAST);
		}
	}
	/**
	 * Check if the current train is joining the train passed by parameter.
	 * @param train The train to be checked if joining.
	 * @return
	 * <ul>
	 * <li>true, if joining the train.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean isJoining(Train train){
		return join==train;
	}
	/**
	 * Decelerate the train if it only contains one vehicle.
	 */
	public void decelerate() {
		if (this.hasOnlyOneCar())
		{
			if (this.getHead().getSpeed()==Speed.FAST)
				this.getHead().setSpeed(Speed.NORMAL);
			else
				this.getHead().setSpeed(Speed.SLOW);
		}
	}
	/**
	 * Check if the train passed by parameter is not intersecting the current train anymore.
	 * @param train The train to be checked if intersecting the current train.
	 * @return
	 * <ul>
	 * <li>true, if the train to be checked is not intersecting anymore the current train.
	 * <li>false, otherwise.
	 * </ul>
	 */
	private boolean hasTrainPassed(Train train){
		if (train==null)
			return true;
		for (Car c : cars)
			for (Car c2 : train.getCars())
				for (Point2D.Float p : c.getPoints())
					if (c2.contains(p))
						return false;
		return true;
	}
	/**
	 * Set the train to be waited from its move.
	 * @param train The train to be waited from its move.
	 */
	public void waitUntilTrainPasses(Train train) {
		this.wait = train;
	}
}
