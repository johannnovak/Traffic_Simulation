package gui.frame;

import java.awt.Rectangle;
/**
 * This class provides a mean to change which components can be displayed in the view.
 * @author NOVAK Johann
 * 		johann.novak@utbm.fr
 * @author SCHULZ Quentin
 * 		quentin.schulz@utbm.fr
 *
 * @version v0.1
 */
public class Camera extends Rectangle{
	private static final long serialVersionUID = 1L;
	
	//Size of the whole map.
	private int xMax, yMax;
	
	/**
	 * Create a new camera whose width and height are passed by parameter. The map will be the size of the camera if the xMax and yMax are smaller than
	 * the width and height of the camera. It also respects the ratio given by the width and height ratio.
	 * @param width The width of the camera.
	 * @param height The height of the camera.
	 * @param xMax The width of the map.
	 * @param yMax The height of the map.
	 */
	public Camera(int width, int height, int xMax, int yMax) {
		float ratio = (float)width/height;
		this.x=0;
		this.y=0;
		this.width=width;
		this.height=height;
		//The map has to have at least the size of the camera.
		this.xMax = (xMax>this.width)?xMax:this.width;
		this.yMax = (yMax>this.height)?yMax:this.height;
		//Respect the ratio given by the camera.
		if (this.xMax>this.yMax*ratio)
			this.yMax = (int) (this.xMax/ratio);
		if (this.yMax>this.xMax/ratio)
			this.xMax = (int) (this.yMax*ratio);
	}
	/**
	 * Return the width of the map.
	 * @return the width of the map.
	 */
	public int getxMax() {
		return xMax;
	}
	/**
	 * Return the width of the map.
	 * @return the width of the map.
	 */
	public int getyMax() {
		return yMax;
	}
	/**
	 * Set the coordinate on X of the upper left corner of the camera while being sure it is still in the map.
	 * @param x The coordinate on X of the future position of the upper left corner of the camera.
	 */
	public synchronized void setX(int x)
	{
		if (x<0)
			this.x=0;
		else if (x>xMax-this.width)
			this.x=xMax-this.width;
		else
			this.x=x;
	}
	/**
	 * Set the coordinate on Y of the upper left corner of the camera while being sure it is still in the map.
	 * @param x The coordinate on Y of the future position of the upper left corner of the camera.
	 */
	public synchronized void setY(int y){
		if (y<0)
			this.y=0;
		else if (y>=yMax-this.height)
			this.y = yMax-this.height;
		else
			this.y=y;
	}
	
}
