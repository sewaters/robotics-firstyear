import java.util.Deque;

import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;

public class checkFloorColourGreen implements Behavior
{
	public checkFloorColourGreen(){
		
	}
	private boolean checkFloor() {
		if(RoboticsMaze.isGreen()) {
			return true;
		} else {
			return false;
		}
			
	}
	public boolean takeControl() {
		return checkFloor();
	}
	public void suppress() {
		
	}
	public void action() {
		RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].traversable = false;
		RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].isGreen = true;
		DetectWall.mapGrid();
		
		if(RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
		{
			RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
		}
		else if(RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
		{
			RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
		}
		else if(RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
		{
			RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
		}
		else if(RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
		{
			RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
		}
		RoboticsMaze.pilot.rotate(180);
			if(!RoboticsMaze.fullTraverseStack.isEmpty()) {
				GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
				DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
			} else {
				Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid, RoboticsMaze.mazeGrid[1][11], RoboticsMaze.endSquare);
				DetectWall.takePathBack(pathToRed);
				Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid, RoboticsMaze.mazeGrid[1][11], RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				DetectWall.takePathBack(pathBack);
				Sound.beepSequence();
				System.exit(0);
			}
		
	}
	
}
