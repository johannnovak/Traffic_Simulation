package interfaces;

public interface GameObservable {
	/**
	 * Add a game observer to the list of game observers.
	 * @param obs The game observer to be added to the list of game observers.
	 */
	public void addObserver(GameObserver obs);
	/**
	 * Remove all game observers.
	 */
	public void removeObserver();
	/**
	 * Notify all game observers in the list of game observers.
	 */
	public void notifyObserver();
}
