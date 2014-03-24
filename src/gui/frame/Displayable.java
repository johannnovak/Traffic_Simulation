package gui.frame;

import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
/**
 * This abstract class represents all objects which can be displayed in the game. It implements Serializable to allow them to be saved, and extends from
 * Polygon to create the circumscribing polygon of the object depending on the image representing the object.<br>
 * It has the coordinates of its RELATIVE upper left corner, a representative image, a string for the path of this image (needed for unserializing the 
 * BufferedImage) and a directional vector.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public abstract class Displayable extends Polygon implements Serializable{

	protected static final long serialVersionUID = 1L;
	protected transient BufferedImage image;
	protected float[] coordinates;
	protected float[] vector = {0.f,0.f};
	protected String imagePath;
	/**
	 * Return the image representative of the object.
	 * @return the image representative of the object.
	 */
	public BufferedImage getImage() {
		return image;
	}
	/**
	 * Return the path of the image.
	 * @return the path of the image.
	 */
	public String getPath()
	{
		return this.imagePath;
	}
	/**
	 * Set the path of the image.
	 * @param s The path of the image.
	 */
	public void setImagePath(String s)
	{
		this.imagePath = new String(s);
		this.setImage(this.imagePath);
	}
	/**
	 * Reload the image thanks to the image path. (Has to be called after unserializing this object and after setImagePath()!).
	 */
	public void load(){
		this.setImage(imagePath);
	}
	/**
	 * Reload the image thanks to the image path.
	 * @param path The path of the image.
	 */
	private void setImage(String path) {
		try {
			this.image = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("No image found at "+path);
		}
	}
	/**
	 * Return the coordinates of the RELATIVE upper left corner of the object.
	 * @return the coordinates of the RELATIVE upper left corner of the object.
	 */
	public float[] getCoordinates() {
		return coordinates;
	}
	/**
	 * Set the coordinates of the RELATIVE upper left corner of the object.
	 * @param coordinates the coordinates of the RELATIVE upper left corner of the object.
	 */
	public void setCoordinates(float[] coordinates) {
		this.coordinates = coordinates;
	}
	/**
	 * Return the directional vector (orientation) of the object.
	 * @return the directional vector (orientation) of the object.
	 */
	public float[] getVector() {
		return vector;
	}
	/**
	 * Return the angle (orientation) of the object rounded to 15 decimals.
	 * @return the angle (orientation) of the object to 15 decimals.
	 */
	public double getAngle(){
		double temp;
		if (vector[0]==0)/*vertical*/
		{
			if (vector[1]<=0)
				temp = 0;
			else
				temp = Math.PI;
		}
		else if (vector[1]==0)/*horizontal*/
		{
			if (vector[0]<=0)
				temp= 3*Math.PI/2;
			else
				temp = Math.PI/2;
		}
		else
		{
			temp = -Math.atan2(vector[0],vector[1]);
			if (vector[1]!=0)
				temp += Math.PI;
		}
		//round to 15 decimals
		String s = Double.toString(temp);
		s = s.substring(0, (s.length()>=15)?15:s.length());
		temp = Double.parseDouble(s);
		return temp;
	}
	/**
	 * Return the angle modulo PI rounded to 15 decimals.
	 * @return the angle modulo PI rounded to 15 decimals.
	 */
	public double getAngleModuloPI()
	{
		double temp;
		if (vector[0]==0)/*vertical*/
		{
			if (vector[1]<=0)
				temp = 0;
			else
				temp = Math.PI;
		}
		else if (vector[1]==0)/*horizontal*/
		{
			if (vector[0]<=0)
				temp= 3*Math.PI/2;
			else
				temp = Math.PI/2;
		}
		else
		{
			temp = -Math.atan2(vector[0],vector[1]);
			if (vector[1]!=0)
				temp += Math.PI;
		}
		temp%=Math.PI;
		String s = Double.toString(temp);
		s = s.substring(0, (s.length()>=15)?15:s.length());
		temp = Double.parseDouble(s);
		return temp;
	}
}
