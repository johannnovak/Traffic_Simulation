#Traffic Simulation

The aim is to develop an application allowing to test crossing strategies for trains. A train is composed of vehicles attached one to each other by an immaterial link. Each vehicle has a starting point and a destination.<br>
When two trains are crossing, they have to nor stop themselves neither bump into each other. The crossing has to be done thanks to spacing between vehicles.<br>
When a vehicle alone (a train which has only one vehicle) bumps into a train, the vehicle alone and the bumped vehicle in the train are both destructed.<br>
When a vehicle alone bumps into a train which has the same destination (or at least the next intermediate in its trip), it joins it.<br>
The user has four options on the simulation:

* accelerate a vehicle (only possible if in a train which only contains this vehicle).
* decelerate a vehicle (only possible if in a train which only contains this vehicle).
* quit its train as soon as possible (at the next cross road, the vehicle has to quit its train, even if it means it has to take a way which does not allow it to reach its destination).
* switch color of the traffic lights.<br>

####Authors:
Johann NOVAK (johann.novak@utbm.fr) <br>
Quentin SCHULZ (quentin.schulz@utbm.fr)

####Usage:
* Continue the project

Clone the repository and directly import it in your favorite IDE (it was developed thanks to Eclipse, so it will work also with NetBeans).

* Take a look at the project

Download the .jar and all images at the root of this project and run it on your computer (the safest way to run the project is to import it in your IDE and export it to a .jar file instead of downloading it).<br>
Next, set the .jar file executable: chmod +x TrafficSimulation.jar<br>
Then run it with: java -jar TrafficSimulation.jar<br>

#####Changelog

**v0.1 - 2014-02-23**
Initial release.<br>

#####Bonus

####How to create a new map

Create a new .xml file wherever you want and follow the guides.

* Creation of a new cross road:<br>
`<CrossRoad>`<br>
`<Name>`C1`</Name>`<br>
`<PosX>`77`</PosX>`<br>
`<PosY>`20`</PosY>`<br>
`</CrossRoad>`<br>

* Creation of a new road:<br>
`<Road>`<br>
`<Starting_CrossRoad>`C1`</Starting_CrossRoad>`<br>
`<Ending_CrossRoad>`C3`</Ending_CrossRoad>`<br>
`</Road>`<br>

* Creation of a new map:<br>
`<?xml version=”1.0“ encoding=”UTF-8“?>`<br>
`<map>`<br><br>
`<CrossRoad>`<br>
`<Name>`C1`</Name>`<br>
`<PosX>`77`</PosX>`<br>
`<PosY>`20`</PosY>`<br>
`</CrossRoad>`<br><br>
`<CrossRoad>`<br>
`<Name>`C2`</Name>`<br>
`<PosX>`98`</PosX>`<br>
`<PosY>`63`</PosY>`<br>
`</CrossRoad>`<br><br>
`<CrossRoad>`<br>
`<Name>`C3`</Name>`<br>
`<PosX>`6`</PosX>`<br>
`<PosY>`150`</PosY>`<br>
`</CrossRoad>`<br><br>
`<Road>`<br>
`<Starting_CrossRoad>`C1`</Starting_CrossRoad>`<br>
`<Ending_CrossRoad>`C3`</Ending_CrossRoad>`<br>
`</Road>`<br><br>
`<Road>`<br>
`<Starting_CrossRoad>`C3`</Starting_CrossRoad>`<br>
`<Ending_CrossRoad>`C2`</Ending_CrossRoad>`<br>
`</Road>`<br><br>
`</map>`<br>
