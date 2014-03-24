package code;

import gui.frame.Displayable;
import interfaces.CarObserver;
import interfaces.GameObservable;
import interfaces.GameObserver;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**
 * This class represents the whole scene of the game. It contains all the actors in the game such as crossroads, roads, traffic lights, vehicles (and
 * trains), intersections. It also has some information like the difficulty of the game, the representative graph, the number of crashed and arrived 
 * vehicles. It is the class which handles the update of the game, the save and load from an XML file or a save.
 * It is the intermediate between the view and the model, it helps both to communicate while making abstraction of each other.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class SceneManager extends Thread implements CarObserver, GameObservable, Serializable{

	private static final long serialVersionUID = 1L;
	
	private Graph graph;
	
	private List<CrossRoad> crossRoads;
	private List<Road> roads;
	private List<Train> trains;
	private List<TrafficLights> trafficLights;
	private List<Intersection> intersections;
	private List<CrossRoad> finals;
	private List<CrossRoad> sources;
	private Difficulty difficulty;
	private int nbDeaths=0, nbReached=0;
	//Current state of the Thread.
	private volatile String state="ongoing";
	private List<GameObserver> observers = new ArrayList<GameObserver>();
	/**
	 * Create a new SceneManager from the file and difficulty passed by parameter. The boolean loading is stating whether we are loading a save.
	 * @param file The file from which wil be extracted all the needed data (save or XML file of the map).
	 * @param difficulty The difficulty of the game.
	 * @param loading The boolean stating whether it is a save (true) or not.
	 * @throws InterruptedException
	 */
	public SceneManager(File file, Difficulty difficulty, boolean loading) throws InterruptedException
	{
		super();
		this.state = "ongoing";
		this.difficulty = difficulty;
		if (!loading)
			parseXML(file);
		else
			load(file);
		//Start the thread = it calls run();
		this.start();

	}
	public synchronized void run(){
		go();
	}
	/**
	 * Loop of the game. The loop is paused if the state of the thread is "paused", stopped if "stopped" and running if "ongoing". Create vehicles 
	 * depending on the difficulty's flow.
	 */
	private synchronized void go(){
		long start= System.currentTimeMillis(), end = System.currentTimeMillis(), duration=0;
		Random r = new Random();
		//Store the duration since the last creation of a vehicle.
		long count=0;
		Car car=null;
		CrossRoad startCR, endCR;
		while(!state.equals("stopped"))
		{
			if (!state.equals("paused"))
			{
				//Time before execution.
				start = System.currentTimeMillis();
				//Check whether the duration since the last creation of a vehicle has overriden the time specified in difficulty's flow.
				if (count>difficulty.getFlow())
				{
					//Be sure the duration since the last creation is reset but keep the extra time.
					count%=difficulty.getFlow();
					do{
						//Find a crossroad which is a source and a crossroad which is final and loop while the trip from the start to the end is not possible
						startCR = this.sources.get(r.nextInt(this.sources.size()));
						endCR = this.finals.get(r.nextInt(this.finals.size()));
					}while(graph.getNextIntermediate(startCR, endCR)==null);
					car = new Car(startCR, endCR, graph);
					this.trains.add(new Train(car));
					car.addCarObserver(this);
				}
				//Update all vehicles thanks to the time elapsed since the last updateand the difficulty's speed.
				this.updatePositions(duration*difficulty.getSpeed());
				//Handle collision between all the actors
				this.checkCollision();
				//Update the game observers
				for(GameObserver obs : observers)
					obs.update();
				end = System.currentTimeMillis();
				//Update the time elapsed between two updates.
				duration = end-start;
				//Update duration since the last creation of a vehicle.
				count+=duration;
			}
		}
	}
	/**
	 * Update positions of all vehicles in the game.
	 * @param time The time elapsed between two updates.
	 */
	private void updatePositions(float time){
		for(Train train : this.trains)
			train.update(time);
	}
	/**
	 * Unpause the thread.
	 */
	public void unpause(){
		setState("ongoing");
	}
	/**
	 * Pause the thread.
	 */
	public void pause(){
		setState("paused");
	}
	/**
	 * Stop the thread.
	 */
	public void setStop(){
		setState("stopped");
	}
	/**
	 * Set state of the thread.
	 * @param s The state of the thread.
	 */
	private void setState(String s){
		this.state = s;
	}
	/**
	 * Return the current state of the thread.
	 * @return the current state of the thread.
	 */
	public String getSceneState()
	{
		return this.state;
	}
	/**
	 * Return the list of all trains in the game.
	 * @return the list of all trains in the game.
	 */
	public List<Train> getTrains(){
		return this.trains;
	}
	/**
	 * Return the list of all traffic lights in the game.
	 * @return the list of all traffic lights in the game.
	 */
	public List<TrafficLights> getTrafficLights()
	{
		return this.trafficLights;
	}
	/**
	 * Return the list of all intersections in the game.
	 * @return the list of all intersections in the game.
	 */
	public List<Intersection> getIntersections()
	{
		return this.intersections;
	}
	/**
	 * Load the save located in the file passed by parameter.
	 * @param file The file containing the save to be loaded.
	 */
	@SuppressWarnings("unchecked")
	private void load(File file){
		ObjectInputStream ois=null;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			try {
				this.graph = (Graph)ois.readObject();
				this.crossRoads = (List<CrossRoad>)ois.readObject();
				this.roads = (List<Road>)ois.readObject();
				this.finals = new ArrayList<CrossRoad>();
				this.sources = new ArrayList<CrossRoad>();
				this.intersections = new ArrayList<Intersection>();
				for (CrossRoad cr : crossRoads)
				{
					//For each found crossroad, check if it is a source or final and add it to the list.
					if (cr.isSource())
						this.sources.add(cr);
					else if (cr.isFinal())
						this.finals.add(cr);
					this.intersections.addAll(cr.getIntersections());
					for (Intersection inter : cr.getIntersections())
						inter.setCrossRoad(cr);
				}
				this.trains = (CopyOnWriteArrayList<Train>)ois.readObject();
				for (Train t : trains)
					//For each found train, we update the reference for itself in each vehicle it contains.
					for (Car c : t.getCars())
						c.setTrain(t);
			    this.trafficLights = (ArrayList<TrafficLights>)ois.readObject();
				this.difficulty = (Difficulty)ois.readObject();
				//Update the list of all actors in the game
				List<Displayable> disps = new ArrayList<Displayable>();
				disps.addAll(crossRoads);
				disps.addAll(trafficLights);
				disps.addAll(roads);
				for (Train t : trains)
					disps.addAll(t.getCars());
				//Reload images of all actors (mandatory since BufferedImage is not serializable)
				for (Displayable disp : disps)
					disp.load();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Parse the XML file of the map located in the file passed by parameter.
	 * @param file The XML file containing the map of the game.
	 */
	private void parseXML(File file)
	{ 
		//Store all data extracted from the XML file.
		MySAXParser gestionnaire=null;
		Random r = new Random();
		List<Road> temp = new ArrayList<Road>();
		Road remove;
		try{
			SAXParserFactory fabrique = SAXParserFactory.newInstance();
			SAXParser parseur = fabrique.newSAXParser();
			gestionnaire = new MySAXParser();
			parseur.parse(file, gestionnaire);
		}catch(ParserConfigurationException pce){
			System.out.println("Erreur de configuration du parseur");
			System.out.println("Lors de l'appel à newSAXParser()");
		}catch(SAXException se){
			System.out.println("Erreur de parsing");
			System.out.println("Lors de l'appel à parse()");
		}catch(IOException ioe){
			System.out.println("Erreur d'entrée/sortie");
			System.out.println("Lors de l'appel à parse()");
		}
		//If not able to parse the file, exit the game.
		if (gestionnaire==null)
			System.exit(-1);
		this.crossRoads = new ArrayList<CrossRoad>(gestionnaire.getCrossRoads());
		this.finals = new ArrayList<CrossRoad>();
		this.sources = new ArrayList<CrossRoad>();
		for (CrossRoad c : gestionnaire.getCrossRoads())
		{
			//For each found crossroad, check if it is a source or final and add it to the list.
			if (c.isFinal())
				this.finals.add(c);
			else if (c.isSource())
				this.sources.add(c);
		}
		this.roads = new ArrayList<Road>(gestionnaire.getRoads());
		this.intersections = new ArrayList<Intersection>();
		for (CrossRoad c : gestionnaire.getCrossRoads())
			this.intersections.addAll(c.getIntersections());
		this.trains = new CopyOnWriteArrayList<Train>();
		this.trafficLights = new ArrayList<TrafficLights>();
		
		//Retrieve all non-final roads (for giving them their traffic lights).
		temp.addAll(roads);
		for (Road road : roads)
		{
			if (road.getEnd().isFinal())
				temp.remove(road);
			else
				for (CrossRoad cr : crossRoads)
					if (cr.getIntersections().contains(road.getEndingPoint()))
					{
						temp.remove(road);
						break;
					}
		}
		//Add traffic lights on the roads which are not final and while the percentage of roads not receiving a traffic lights is not reached.
		int size=temp.size();
		while (temp.size()!=0 && (temp.size()/(float)size)>difficulty.getNbTrafficLights())
		{
			remove = temp.get(r.nextInt(temp.size()));
			this.trafficLights.add(new TrafficLights(remove));
			temp.remove(remove);
		}
		//Create the graph representing the map.
		createGraph(crossRoads, roads);
	}
	/**
	 * Create the graph thanks to the crossroads and roads passedby parameter.
	 * @param crossRoads The whole set of crossroads in the game.
	 * @param roads The whole set of roads in the game.
	 */
	private void createGraph(List<CrossRoad> crossRoads, List<Road> roads){
		this.graph = new Graph(crossRoads, roads);	
	}
	/**
	 * Save the current game.
	 */
	public void save(){
	    ObjectOutputStream oos;
	    //Allow the user to chose where to save the game and force the extension of the file to be ".save" and set the default directory to /save/
	    JFileChooser jc = new JFileChooser(new File(System.getProperty("user.dir")+"/save/"));
		jc.setDialogTitle("Sauvegarder");
		jc.setFileFilter(new FileNameExtensionFilter("Fichiers de type .save", "save"));
	    int result = jc.showSaveDialog(null);
	    if (result!= JFileChooser.APPROVE_OPTION)
	    	return;
	    try {
		    File f = jc.getSelectedFile(), f2 = null;
		   	if(!f.getPath().endsWith(".save"))
		   	{
			   	f2 = new File((f.getPath()+".save"));
			   	f = f2;	
		   	}
		    f.createNewFile();
		    oos = new ObjectOutputStream(
		            new BufferedOutputStream(
		              new FileOutputStream(f)));	        	
		    oos.writeObject(this.graph);
		    oos.writeObject(this.crossRoads);
		    oos.writeObject(this.roads);
		    oos.writeObject(this.trains);
		    oos.writeObject(this.trafficLights);
		    oos.writeObject(this.difficulty);
		    oos.close();
	    }catch(FileNotFoundException e){
	    	e.printStackTrace();
	    }catch (IOException e) {
			e.printStackTrace();
		}   
	}
	/**
	 * Retrieve the circumscribing rectangle of the map (i.e. the maximum distance (on X and Y) between the farthest crossroads in the game.
	 * @return The circumscribing rectangle of the map.
	 */
	public Point2D.Float getCircumscribingRectangle(){
		Point2D.Float pt = new Point2D.Float();
		float x=0, y=0;
		for (CrossRoad cr : crossRoads)
		{
			x = Math.max(x, cr.getCoordinates()[0]+cr.getImage().getWidth());
			y = Math.max(y, cr.getCoordinates()[1]+cr.getImage().getHeight());
		}
		pt.x = x;
		pt.y = y;
		return pt;
	}
	/**
	 * Handle collision between all actors in the game.
	 */
	public void checkCollision()
	{
		//Store the list of already checked vehicles.
		ArrayList<Car> temp = new ArrayList<Car>();
		for(Train train1 : this.getTrains())
		{
			for(Car c1 : train1.getCars())
			{
				temp.add(c1);
				for(Train train2 : this.getTrains())
					for(Car c2 : train2.getCars())
						if(!temp.contains(c2))
							//If both vehicles are not in the list of already checked vehicles, check intersection between both.
							c1.intersectWith(c2);
				for(Intersection inter: this.getIntersections())
					//Check intersection with an Intersection (i.e. crossing of two roads).
					c1.intersectWith(inter);
				if (trafficLights!=null)
					for(TrafficLights traff : this.getTrafficLights())
						//Check intersection with a traffic lights.
						c1.intersectWith(traff);
			}
		}
	}
	/**
	 * Return the list of all actors in the game.
	 * @return the list of all actors in the game.
	 */
	public ArrayList<Displayable> getDisplayables()
	{
		ArrayList<Displayable> list = new ArrayList<Displayable>();
		list.addAll(crossRoads);
		list.addAll(roads);
		list.addAll(trafficLights);
		for(Train train : trains)
			list.addAll(train.getCars());
		return list;
	}
	/**
	 * Handle a click on the map.
	 * @param point The point where the mouse was clicked.
	 * @return
	 * <ul>
	 * <li>null, if there is nothing found where the mouse was clicked or if it was a traffic lights.
	 * <li>the clicked vehicle.
	 * </ul>
	 */
	public Car clickOn(Point point) {
		//Find the traffic lights or vehicle clicked by the mouse.
		if (trafficLights!=null)
			for (TrafficLights disp : trafficLights)
				if(disp.contains(point))
				{
					disp.switchColor();
					return null;
				}
		for (Train t : trains)
			for (Car c : t.getCars())
				if (c.contains(point))
					return c;
		return null;
	}

	@Override
	public void addTrain(Train train)
	{
		this.trains.add(train);
	}
	public void removeTrain(Train train)
	{
		this.trains.remove(train);
	}
	@Override
	public CrossRoad newIntermediate(CrossRoad start, CrossRoad end) 
	{
		return this.graph.getNextIntermediate(start, end);
	}
	@Override
	public boolean containsTrain(Train train) {
		return this.trains.contains(train);
	}
	/**
	 * Increment the counter of vehicle's crashes.
	 */
	@Override
	public void crash() {
		++this.nbDeaths;
	}
	/**
	 * Increment the counter of vehicles which reached their destination or a dead-end.
	 */
	@Override
	public void reach() {
		++this.nbReached;
	}
	/**
	 * Return the number of crashes.
	 * @return the number of crashes.
	 */
	public int getNbOfDeaths() {
		return this.nbDeaths;
	}
	/**
	 * Return the number of vehicles which reached their destination or a dead-end.
	 * @return the number of vehicles which reached their destination or a dead-end.
	 */
	public int getNbOfReached() {
		return this.nbReached;
	}
	public void addObserver(GameObserver obs){
		this.observers.add(obs);
	}
	public void removeObserver(){
		this.observers = new ArrayList<GameObserver>();
	}
	public void notifyObserver(){
		if (observers==null || observers.size()==0)
			return;
		for (GameObserver obs : observers)
			obs.update();
	}
	public void update(){
		this.notifyObserver();
	}
}
