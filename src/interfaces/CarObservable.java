package interfaces;
/**
 * This interface gives a mean to notify observers of vehicles.
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public interface CarObservable {
	/**
	 * Add an observer to the vehicle.
	 * @param obs The observer to be added to the list of observers for the vehicle
	 */
	public void addCarObserver(CarObserver obs);
	/**
	 * Remove all observers.
	 */
	public void removeObserver();
	/**
	 * Notify observers.
	 */
	public void notifyObserver();
}
