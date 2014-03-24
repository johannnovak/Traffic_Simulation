package code;
import java.io.Serializable;
/**
 * This class represents the different difficulties allowed for the game. A difficulty defines the duration between creations
 * of two vehicles, the percentage of roads which have NOT a traffic lights on it and the speed of the vehicle.
 * You have five different difficulties :
 * <ul>
 * <li>NOOB which creates a vehicle each 6 seconds, which forces 100% of roads to have a traffic lights and the speed set to 1.
 * <li>EASY which creates a vehicle each 5 seconds, which forces 75% of roads to have a traffic lights and the speed set to 1.25.
 * <li>MEDIUM which creates a vehicle each 3 seconds, which forces 50% of roads to have a traffic lights and the speed set to 1.5.
 * <li>HARD which creates a vehicle each 2 seconds, which forces 25% of roads to have a traffic lights and the speed set to 2.
 * <li>HARDCORE which creates a vehicle each 1 seconds, which forces 0% of roads to have a traffic lights and the speed set to 3.
 * </ul>
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public enum Difficulty implements Serializable
{
	/**
	 * NOOB mode which creates a vehicle each 6 seconds, which forces 100% of roads to have a traffic lights and the speed set to 1.
	 */
	NOOB(6000,0,1),
	/**
	 * EASY mode which creates a vehicle each 5 seconds, which forces 75% of roads to have a traffic lights and the speed set to 1.25.
	 */
	EASY(5000,0.25f,1.25f),
	/**
	 * MEDIUM mode which creates a vehicle each 3 seconds, which forces 50% of roads to have a traffic lights and the speed set to 1.5.
	 */
	MEDIUM(3000,0.5f,1.5f),
	/**
	 * HARD mode which creates a vehicle each 2 seconds, which forces 25% of roads to have a traffic lights and the speed set to 2.
	 */
	HARD(2000,0.75f,2),
	/**
	 * HARDCORE mode which creates a vehicle each 1 seconds, which forces 0% of roads to have a traffic lights and the speed set to 3.
	 */
	HARDCORE(1000,1,3);
	private final float flow, nbOfTrafficLights, speed;
	/**
	 * Create a new Difficulty enumeration with a specified flow (creation of a vehicle each [flow] ms), a specified percentage
	 * of roads NOT receiving a traffic lights and the relative speed of vehicles (NOOB value is the reference : 1)
	 * @param flow The number of ms between two creations of vehicles.
	 * @param nbOfTrafficLights The percentage of roads NOT receiving a traffic lights.
	 * @param speed The relative speed of vehicles (NOOB value is the reference : 1).
	 */
	private Difficulty(float flow, float nbOfTrafficLights, float speed){
		this.flow = flow;
		this.nbOfTrafficLights= nbOfTrafficLights;
		this.speed= speed;
	}
	/**
	 * Return the number of ms between two creations of vehicles.
	 * @return the number of ms between two creations of vehicles.
	 */
	public float getFlow(){
		return this.flow;
	}
	/**
	 * Return the percentage of roads NOT receiving a traffic lights.
	 * @return the percentage of roads NOT receiving a traffic lights.
	 */
	public float getNbTrafficLights(){
		return this.nbOfTrafficLights;
	}
	/**
	 * Return the relative speed of vehicles (NOOB value is the reference : 1).
	 * @return the relative speed of vehicles (NOOB value is the reference : 1).
	 */
	public float getSpeed(){
		return this.speed;
	}		
}
