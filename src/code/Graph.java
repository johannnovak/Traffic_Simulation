package code;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the means to find the shortest path between two crossroads. It uses the Floyd Warshall algorithm.
 * 
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 * 
 * @version v0.1
 *
 */
public class Graph implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * Inner class of Graph. It allows to store two needed objects: an intermediate and a distance (between two crossroads).
	 * 
	 * @author NOVAK Johann
	 * 		johann.novak@utbm.fr
	 * @author SCHULZ Quentin
	 * 		quentin.schulz@utbm.fr
	 * 
	 * @version v0.1
	 *
	 */
	class FloydWarshallElement implements Serializable
	{
		private static final long serialVersionUID = 1L;
		private float distance;
		private CrossRoad intermediate;
		/**
		 * Initialize the FloydWarshallElement to infinity for the distance it keeps and null as an intermediate.
		 */
		public FloydWarshallElement()
		{
			distance = Float.POSITIVE_INFINITY;
			intermediate = null;
		}
		/**
		 * Return the intermediate (if it exists) between two cross roads.
		 * @return the intermediate (if it exists) between two cross roads.
		 */
		public CrossRoad getIntermediate()
		{
			return intermediate;
		}
		/**
		 * Set the intermediate between two crossroads.
		 * @param crossRoad The crossroad which is the intermediate between two crossroads.
		 */
		public void setIntermediate(CrossRoad crossRoad)
		{
			this.intermediate = crossRoad;
		}
		/**
		 * Return the distance between two crossroads.
		 * @return the distance between two crossroads.
		 */
		public float getDistance()
		{
			return distance;
		}
		/**
		 * Set the distance between two crossroads.
		 * @param distance The distance between two crossroads.
		 */
		public void setDistance(float distance)
		{
			this.distance = distance;
		}
	}
	//A matrix whose indexes are crossroads and whose values are FloydWarshallElements.
	private Map<CrossRoad,Map<CrossRoad,FloydWarshallElement>> matrix;
	/**
	 * Initialize a Graph object thanks to the whole set of crossroads and roads in the game. It will launch the FloydWarshall
	 * algorithm on the graph.
	 * @param crossRoads The whole set of crossroads in the game.
	 * @param roads The whole set of roads in the game.
	 */
	public Graph(List<CrossRoad> crossRoads, List<Road> roads)
	{
		this.initializeMatrix(crossRoads, roads);
		FloydWarshall(crossRoads, roads);
	}
	
	/**
	 * Initialize the matrix of the graph and set all diagonal FloydWarshallElement distance values to 0, and distance values
	 * between two neighbors to the distance of the road joining each crossroad.
	 * @param crossRoads The whole set of crossroads in the game.
	 * @param roads The whole set of roads in the game.
	 */
	private void initializeMatrix(List<CrossRoad> crossRoads, List<Road> roads)
	{
		matrix = new HashMap<CrossRoad, Map<CrossRoad,FloydWarshallElement>>();
		for(CrossRoad cr : crossRoads)
		{
			matrix.put(cr, new HashMap<CrossRoad,FloydWarshallElement>());
			for(CrossRoad CR : crossRoads)
			{
				matrix.get(cr).put(CR, new FloydWarshallElement());
				if(CR == cr)
					matrix.get(cr).get(CR).setDistance(0);
			}
		}
		for(Road r : roads)
			matrix.get(r.getStart()).get(r.getEnd()).setDistance(r.getDistance());
	}
	/**
	 * Set the distance and the intermediate between each crossroad thanks to the Floyd Warshall algorithm.
	 * @param crossRoads The whole set of crossroads in the game.
	 * @param roads The whole set of roads in the game.
	 */
	private void FloydWarshall(List<CrossRoad> crossRoads, List<Road> roads)
	{
		for(CrossRoad k : crossRoads)
			for(CrossRoad i : crossRoads)
				for(CrossRoad j : crossRoads)
					if(matrix.get(i).get(k).getDistance() + matrix.get(k).get(j).getDistance() < matrix.get(i).get(j).getDistance())
					{
						matrix.get(i).get(j).setDistance(matrix.get(i).get(k).getDistance() + matrix.get(k).get(j).getDistance());
						matrix.get(i).get(j).setIntermediate(k);
					}
	}
	/**
	 * Return the next intermediate between two cross roads.
	 * @param start The crossroad from which we start the trip.
	 * @param end The crossroad to which we end the trip.
	 * @return the crossroad which is the intermediate between those crossroads.
	 */
	public CrossRoad getNextIntermediate(CrossRoad start, CrossRoad end)
	{
		FloydWarshallElement temp = null;
		try{
			temp = matrix.get(start).get(end);
		}catch(NullPointerException npe){
			return null;
		}
		if(temp.getIntermediate() == null)
		{
			if(temp.getDistance() == Float.POSITIVE_INFINITY)
				return null;
			return end;
		}
		return getNextIntermediate(start, temp.getIntermediate());
	}
}
