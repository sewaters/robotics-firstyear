import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class A_Star_Algorithm
{
	public static Deque<GridSquare> run(GridSquare[][] grid, GridSquare start, GridSquare goal)
	{
		ArrayList<GridSquare> visited = new ArrayList<>();
		ArrayList<GridSquare> openSet = new ArrayList<GridSquare>();
		openSet.add(start);
		start.setG(0);
		start.setF(costEstimate(start,goal));
		while(!openSet.isEmpty()) {
			openSet = order(openSet);
			
			GridSquare current = openSet.get(0);
			LCD.clear();
			LCD.drawString(start.getX() + " " + start.getY(), 0, 5);
			LCD.drawString(goal.getX() + " " + goal.getY(), 0, 6);
			
			if(current.getX() == goal.getX() && current.getY() == goal.getY()) {
				return findPath(current,start);
			}
			openSet.remove(current);
			visited.add(current);
			findNeighbours(current, grid);
			ArrayList<GridSquare> neighbours = current.getNeighbours();
			for(int i = 0; i < neighbours.size();i++) {
				GridSquare currentNeighbour = neighbours.get(i);
				if(!visited.contains(currentNeighbour)) {
					double tempG = current.g + 1;
					if(!openSet.contains(currentNeighbour)) {
						openSet.add(currentNeighbour);
						currentNeighbour.previous = current;
						currentNeighbour.g = tempG;
						currentNeighbour.f = currentNeighbour.g + costEstimate(currentNeighbour,goal);
					} else if(tempG <= currentNeighbour.g) {
						currentNeighbour.previous = current;
						currentNeighbour.g = tempG;
						currentNeighbour.f = currentNeighbour.g + costEstimate(currentNeighbour,goal);
					}
				}
			}
		}
		return null;
	}
	public static double costEstimate(GridSquare current, GridSquare goal) {
		double distance = Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
		return distance;
	}
	
	public static void findNeighbours(GridSquare current, GridSquare[][] grid) {
		ArrayList<GridSquare> temp = new ArrayList<>();
		if((current.getY() - 1) >= 0) {
			GridSquare North = grid[current.getX()][(current.getY())-1];
			if(North.traversable) {
				temp.add(North);
			}
		}
		if((current.getY() + 1) <= 12) {
			GridSquare South = grid[current.getX()][current.getY()+1];
			if(South.traversable) {
				temp.add(South);
			}
		}
		if((current.getX() - 1) >= 0) {
			GridSquare West = grid[current.getX()-1][current.getY()];
			if(West.traversable) {
				temp.add(West);
			}
		}
		if((current.getX() + 1) <= 18) {
			GridSquare East = grid[current.getX()+1][current.getY()];
			if(East.traversable) {
				temp.add(East);
			}
		}
		current.neighbours = temp;
	}
	public static Deque<GridSquare> findPath(GridSquare goal,GridSquare start) {
		Deque<GridSquare> path = new ArrayDeque<GridSquare>();
		GridSquare current = goal;
		path.push(current);
		while(current.previous != start) {
			path.push(current.previous);
			current = current.previous;
		}
		//RBlueTooth.sendPath(path);
		return path;
	}
	public static ArrayList<GridSquare> order(ArrayList<GridSquare> list){
		int size = list.size();
		for(int i = 0; i < size - 1;i++) {
			for(int j = 0;j < size - 1 ;j++) {
				GridSquare temp = list.get(j);
				GridSquare temp2 = list.get(j+1);
				
				if(temp.f > temp2.f) {
					list.remove(j);
					list.add(j,temp2);
					list.remove(j+1);
					list.add(j+1, temp);
				}
			}
		}
		
		return list;
	}
}