import java.io.Serializable;
import java.util.ArrayList;

public class GridSquare implements Serializable
{
	private int xCoord;
	private int yCoord;
	public ArrayList<GridSquare> neighbours;
	public boolean traversable;
	public boolean traversed;
	public double g;
	public double h;
	public double f;
	public boolean isGreen;
	public boolean isRed;
	public GridSquare previous;
	public GridSquare(int x, int y,boolean traversable) {
		this.xCoord = x;
		this.yCoord = y;
		this.isGreen = false;
		this.isRed = false;
		this.traversable = traversable;
		this.traversed = false;
		this.f = Double.POSITIVE_INFINITY;
	}
	public int getX() {
		return xCoord;
	}
	public int getY() {
		return yCoord;
	}
	public ArrayList<GridSquare> getNeighbours() {
		return neighbours;
		
	}
	public boolean getTraversable() {
		return traversable;
	}
	public void setG(int newG) {
		this.g = newG;
	}
	public void setF(double newF) {
		this.f = newF;
	}

}
