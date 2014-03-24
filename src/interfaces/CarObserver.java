package interfaces;

import code.CrossRoad;
import code.Train;
/**
 * This interface gives a mean to observe vehicles. It can add and remove trains, check if a train still exists, find the next 
 * intermediate for a trip and do action on a crash or a vehicle which reached its destination or a dead-end.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public interface CarObserver{
	/**
	 * Add train to the list of trains in the game.
	 * @param train The train to be added to the list of trains in the game.
	 */
	public void addTrain(Train train);
	/**
	 * Remove train from the list of trains in the game.
	 * @param train The train to be removed from the list of trains in the game.
	 */
	public void removeTrain(Train train);
	/**
	 * Return the next intermediate in the trip from start to end.
	 * @param start The crossroad from which starts the trip.
	 * @param end The crossroad to which ends the trip.
	 * @return
	 * <ul>
	 * <li>null, if there is no path between the start and the end.
	 * <li>a crossroad which is the next intermediate between start and end.
	 * </ul>
	 */
	public CrossRoad newIntermediate(CrossRoad start, CrossRoad end);
	/**
	 * Check whether the list of trains in the game contains the trai passed by parameter.
	 * @param The train to be checked if contained in the list of trains in the game.
	 * @return
	 * <ul>
	 * <li>true, if the train is contained in the list of trains in the game.
	 * <li>false, otherwise.
	 * </ul>
	 */
	public boolean containsTrain(Train train);
	/**
	 * Do action on crash of a vehicle.
	 */
	public void crash();
	/**
	 * Do action on vehicle reaching its destination or a dead-end.
	 */
	public void reach();
}
