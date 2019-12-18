import java.util.Deque;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;

public class checkFloorRed implements Behavior
{
	public checkFloorRed(){
		
	}
	private boolean checkFloor() {
		if(RoboticsMaze.isRed()) {
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
	@Override
	public void action()
	{
		RoboticsMaze.endSquare = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
		RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].isRed = true;
		LCD.clear();
		LCD.drawString(RoboticsMaze.endSquare.getX() + " " + RoboticsMaze.endSquare.getY(), 0, 3);
		if(!RoboticsMaze.fullTraverseStack.isEmpty()) {
			GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
			DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
		} else {
			Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid, RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],RoboticsMaze.mazeGrid[1][11]);
			DetectWall.takePathBack(pathBack);
			Sound.beepSequence();
			System.exit(0);
		}
	}
}