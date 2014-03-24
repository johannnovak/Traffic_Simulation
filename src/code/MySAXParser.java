package code;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class allows to parse a custom XML file used to create maps.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class MySAXParser extends DefaultHandler{
	//Results of the parsing
	private LinkedHashMap<String, CrossRoad> crossRoads;
	private List<Road> roads;
	//Temporary objects needed for list creation.
	private CrossRoad crossRoad=null;
	private Road road=null;
	//Buffer to gather data 
	private StringBuffer buffer=null;
	//Temporary variables used to create crossroads and roads.
	private String start=null, end=null, name=null;
	private float[] coordinates;
	//Set of detected (and allowed) keywords.
	private final String NAME="Name", POSX="PosX", POSY="PosY", STARTING_CROSSROAD="Starting_CrossRoad", ENDING_CROSSROAD="Ending_CrossRoad", ROAD="Road", CROSSROAD="CrossRoad", BODY="map";

	/**
	 * Initialize the XML parser by creating an empty list of roads and an empty map of crossroads.
	 */
	public MySAXParser(){
		super();
		crossRoads = new LinkedHashMap<String, CrossRoad>();
		roads = new LinkedList<Road>();
	}
	/**
	 * Return the list of crossroads created when parsing the XML file.
	 * @return the list of crossroads created when parsing the XML file.
	 */
	public List<CrossRoad> getCrossRoads()
	{
		List<CrossRoad> list = new ArrayList<CrossRoad>();
		for(CrossRoad cr : crossRoads.values())
			list.add(cr);
		return list;
	}
	/**
	 * Return the list of roads created when parsing the XML file.
	 * @return the list of roads created when parsing the XML file.
	 */
	public List<Road> getRoads()
	{
		return roads;
	}
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) throws SAXException{
		if (qName.equalsIgnoreCase(BODY)){}
		else if (qName.equalsIgnoreCase(CROSSROAD))
			//New crossroad: reset coordinates
			this.coordinates = new float[2];
		else if(qName.equalsIgnoreCase(ROAD) || qName.equalsIgnoreCase(NAME) || qName.equalsIgnoreCase(POSX) || qName.equalsIgnoreCase(POSY) || qName.equalsIgnoreCase(STARTING_CROSSROAD) || qName.equalsIgnoreCase(ENDING_CROSSROAD)){
			//New String: reset buffer
			buffer = new StringBuffer();
		}
		else{
			//Unknown tag
			throw new SAXException("Balise "+qName+" inconnue.");
		}
	}
	 
	public void endElement(String uri, String localName, String qName)
			throws SAXException{
		if (qName.equalsIgnoreCase(BODY)){
			//End of XML file, create all intersections
			for (CrossRoad c : this.crossRoads.values())
				c.fix();
		}
		else if(qName.equalsIgnoreCase(ROAD)){
			//End of a road tag, create a new road with named crossroads
			this.road = new Road(this.crossRoads.get(this.start), this.crossRoads.get(this.end));
			roads.add(road);
			this.start = this.end = null;
			buffer = null;
		}else if(qName.equalsIgnoreCase(CROSSROAD)){
			//End of crossroad tag, create a new crossroad with name and coordinates.
			this.crossRoad = new CrossRoad(this.name, this.coordinates);
			this.crossRoads.put(this.name, crossRoad);
			buffer = null;
		}
		else if(qName.equalsIgnoreCase(NAME)){
			this.name = buffer.toString();
			buffer = null;
		}else if(qName.equalsIgnoreCase(POSX)){
			this.coordinates[0] = Float.parseFloat(buffer.toString());
			buffer = null;
		}else if(qName.equalsIgnoreCase(POSY)){
			this.coordinates[1] = Float.parseFloat(buffer.toString());
			buffer = null;
		}else if(qName.equalsIgnoreCase(STARTING_CROSSROAD)){
			this.start = buffer.toString();
			buffer = null;
		}else if(qName.equalsIgnoreCase(ENDING_CROSSROAD)){
			this.end = buffer.toString();
			buffer = null;
		}else{
			//Unknown tag
			throw new SAXException("Balise "+qName+" inconnue.");
		}	          
	}
	public void characters(char[] ch,int start, int length)
			throws SAXException{
		String lecture = new String(ch,start,length);
		if(buffer != null) buffer.append(lecture);       
	}
	public void startDocument() throws SAXException {}
	public void endDocument() throws SAXException {}
	
}

