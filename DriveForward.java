import java.util.Deque;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

class DriveForward implements Behavior {
	private boolean _suppressed = false;
	public boolean takeControl() {
		if(Button.readButtons() != 0) {
			_suppressed = true;
			RoboticsMaze.pilot.stop();
			Button.discardEvents();
			if((Button.waitForAnyPress() & Button.ID_ESCAPE) != 0) {
				System.exit(1);
			}
			Button.waitForAnyEvent();
		}
		return true;
	}
	public void suppress() {
		_suppressed = true;
	}
	public void action() {
		_suppressed = false;
		while(!_suppressed) {
			Thread.yield();
			Delay.msDelay(500);
			if(RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0) {
				if(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY - 1].traversable && !RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY - 2].traversed) {
					RoboticsMaze.currentY -=2;
					RoboticsMaze.pilot.travel(41);
					if(!RoboticsMaze.isGreen()) {
						DetectWall.mapGrid();
					}
					RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				} else {
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
					boolean leftDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
					boolean rightDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
					if(leftDist && rightDist) {
						RoboticsMaze.pilot.rotate(180);
						if(!RoboticsMaze.fullTraverseStack.isEmpty()) {
							RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
							GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
							DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
						}else
						{
							Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.endSquare);
							DetectWall.takePathBack(pathToRed);
							Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.mazeGrid[1][11]);
							DetectWall.takePathBack(pathBack);
							Sound.beepSequence();
							System.exit(0);
						}
					} else if(leftDist) {
						RoboticsMaze.pilot.rotate(90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
					} else if(rightDist) {
						RoboticsMaze.pilot.rotate(-90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
					}
				}
			} else if(RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0) {
				if(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY + 1].traversable && !RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY + 2].traversed) {
					RoboticsMaze.currentY +=2;
					RoboticsMaze.pilot.travel(41);
					if(!RoboticsMaze.isGreen()) {
						DetectWall.mapGrid();
					}
					RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				} else {
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
					boolean leftDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
					boolean rightDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
					if(leftDist && rightDist) {
						RoboticsMaze.pilot.rotate(180);
						if(!RoboticsMaze.fullTraverseStack.isEmpty()) {
							RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
							GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
							DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
						}else
						{
							Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.endSquare);
							DetectWall.takePathBack(pathToRed);
							Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.mazeGrid[1][11]);
							DetectWall.takePathBack(pathBack);
							Sound.beepSequence();
							System.exit(0);
						}
					} else if(leftDist) {
						RoboticsMaze.pilot.rotate(90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
					} else if(rightDist) {
						RoboticsMaze.pilot.rotate(-90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
					}
				}
			}
			else if(RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0) {
				if(RoboticsMaze.mazeGrid[RoboticsMaze.currentX - 1][RoboticsMaze.currentY].traversable&& !RoboticsMaze.mazeGrid[RoboticsMaze.currentX - 2][RoboticsMaze.currentY].traversed) {
					RoboticsMaze.currentX -=2;
					RoboticsMaze.pilot.travel(41);
					if(!RoboticsMaze.isGreen()) {
						DetectWall.mapGrid();
					}
					RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				}  else {
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
					boolean leftDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
					boolean rightDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
					if(leftDist && rightDist) {
						RoboticsMaze.pilot.rotate(180);
						if(!RoboticsMaze.fullTraverseStack.isEmpty()) {

							RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
							GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
							DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
						}else
						{
							Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.endSquare);
							DetectWall.takePathBack(pathToRed);
							Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.mazeGrid[1][11]);
							DetectWall.takePathBack(pathBack);
							Sound.beepSequence();
							System.exit(0);
						}
					} else if(leftDist) {
						RoboticsMaze.pilot.rotate(90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
					} else if(rightDist) {
						RoboticsMaze.pilot.rotate(-90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
					}
				}
			}
			else if(RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0) {
				if(RoboticsMaze.mazeGrid[RoboticsMaze.currentX+ 1][RoboticsMaze.currentY].traversable && !RoboticsMaze.mazeGrid[RoboticsMaze.currentX+ 2][RoboticsMaze.currentY].traversed) {
					RoboticsMaze.currentX +=2;
					RoboticsMaze.pilot.travel(41);
					if(!RoboticsMaze.isGreen()) {
						DetectWall.mapGrid();
					}
					RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				} else {
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
					boolean leftDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.TOP_MOTOR.rotate(90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
					boolean rightDist = DetectWall.checkIndividualDistance();
					RoboticsMaze.TOP_MOTOR.rotate(-90);
					RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
					if(leftDist && rightDist) {
						RoboticsMaze.pilot.rotate(180);
						if(!RoboticsMaze.fullTraverseStack.isEmpty()) {

							RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
							GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
							DetectWall.returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY],toReturnTo);
						}else
						{
							Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.endSquare);
							DetectWall.takePathBack(pathToRed);
							Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
									RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.mazeGrid[1][11]);
							DetectWall.takePathBack(pathBack);
							Sound.beepSequence();
							System.exit(0);
						}
					} else if(leftDist) {
						RoboticsMaze.pilot.rotate(90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
					} else if(rightDist) {
						RoboticsMaze.pilot.rotate(-90);
						RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
					}
				}
			}
			RoboticsMaze.visited.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
		}
	}
}