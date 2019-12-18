import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

import java.util.ArrayDeque;
import java.util.Deque;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

class DetectWall implements Behavior
{
	public DetectWall()
	{

	}

	private static boolean checkDistance()
	{
		float dist = RoboticsMaze.IRSensor.distance;
		if (dist < 30)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public boolean takeControl()
	{
		return checkDistance();
	}

	public void suppress()
	{

	}

	public void action()
	{
		RoboticsMaze.TOP_MOTOR.rotate(-90);
		Delay.msDelay(100);
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
		boolean leftDist = checkIndividualDistance();
		RoboticsMaze.TOP_MOTOR.rotate(180);
		Delay.msDelay(100);
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
		boolean rightDist = checkIndividualDistance();
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
		RoboticsMaze.TOP_MOTOR.rotate(-90);
		if (!leftDist)
		{
			RoboticsMaze.pilot.rotate(-90);
			if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
			}
		}
		else if (!rightDist)
		{
			RoboticsMaze.pilot.rotate(90);
			if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
			}
		}
		else
		{
			/*RoboticsMaze.pilot.rotate(180);
			if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
			{
				RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
			}*/
			if (!RoboticsMaze.fullTraverseStack.isEmpty())
			{
				Sound.beep();
				GridSquare toReturnTo = RoboticsMaze.fullTraverseStack.pop();
				returnPath(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], toReturnTo);
			}
			else
			{
				Deque<GridSquare> pathToRed = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
						RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.endSquare);
				takePathBack(pathToRed);
				Deque<GridSquare> pathBack = A_Star_Algorithm.run(RoboticsMaze.mazeGrid,
						RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY], RoboticsMaze.mazeGrid[1][11]);
				takePathBack(pathBack);
				Sound.beepSequence();
				System.exit(0);
			}
		}

	}

	public static boolean checkIndividualDistance()
	{
		float dist = RoboticsMaze.IRSensor.distance;
		GridSquare temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
		if (dist < 30)
		{
			return true;
		}
		else
		{

			if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
			{
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.forward))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX - 2][RoboticsMaze.currentY];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.left))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY + 2];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.right))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY - 2];
				}
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
			{
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.forward))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX + 2][RoboticsMaze.currentY];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.left))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY - 2];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.right))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY + 2];
				}
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
			{
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.forward))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY - 2];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.left))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX - 2][RoboticsMaze.currentY];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.right))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX + 2][RoboticsMaze.currentY];
				}
			}
			else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
			{
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.forward))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY + 2];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.left))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX + 2][RoboticsMaze.currentY];
				}
				if (RoboticsMaze.currentTopDirection.equals(RoboticsMaze.RDirectionTop.right))
				{
					temp = RoboticsMaze.mazeGrid[RoboticsMaze.currentX - 2][RoboticsMaze.currentY];
				}
			}
			if (!temp.traversable || temp.traversed)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

	}

	public static void mapGrid()
	{
		Button.LEDPattern(1);
		int originalX = RoboticsMaze.currentX;
		int originalY = RoboticsMaze.currentY;
		GridSquare forward = null;
		GridSquare left = null;
		GridSquare right = null;
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
		boolean Dist = checkDistance();
		if (Dist)
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentY--;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentY++;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentX--;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentX++;
			}
			forward = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
			forward.traversable = false;
		}
		else
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentY -= 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentY += 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentX -= 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentX += 2;
			}
			forward = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
		}
		RoboticsMaze.currentX = originalX;
		RoboticsMaze.currentY = originalY;
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.left;
		RoboticsMaze.TOP_MOTOR.rotate(-90);
		Delay.msDelay(100);
		boolean leftDist = checkDistance();
		if (leftDist)
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentX--;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentX++;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentY++;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentY--;
			}
			left = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
			left.traversable = false;
		}
		else
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentX -= 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentX += 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentY += 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentY -= 2;
			}
			left = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
		}
		RoboticsMaze.currentX = originalX;
		RoboticsMaze.currentY = originalY;
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.right;
		RoboticsMaze.TOP_MOTOR.rotate(-90);
		RoboticsMaze.TOP_MOTOR.rotate(-90);
		Delay.msDelay(100);
		boolean rightDist = checkDistance();
		if (rightDist)
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentX++;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentX--;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentY--;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentY++;
			}
			right = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
			right.traversable = false;
		}
		else
		{
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentX += 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentX -= 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentY -= 2;
			}
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentY += 2;
			}
			right = RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY];
		}
		RoboticsMaze.currentX = originalX;
		RoboticsMaze.currentY = originalY;
		RoboticsMaze.currentTopDirection = RoboticsMaze.RDirectionTop.forward;
		RoboticsMaze.TOP_MOTOR.rotate(270);
		if(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].traversable && !RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].traversed) {
			if (right.traversable && !right.traversed && ((left.traversable && !left.traversed) || (!forward.traversed && forward.traversable)))
			{
				LCD.drawString(right.getX() + " " + right.getY(), 0, 2);
				if (!RoboticsMaze.fullTraverseStack.contains(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]))
				{
					Sound.beepSequence();
					RoboticsMaze.fullTraverseStack.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
				}
			}
			if (left.traversable && !left.traversed && !forward.traversed && forward.traversable)
			{
				if (!RoboticsMaze.fullTraverseStack.contains(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]))
				{
					RoboticsMaze.fullTraverseStack.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
					Sound.twoBeeps();
				}
			}
		}

		RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY].traversed = true;
		LCD.clear();
		final int drawSize = 5;
		final int xMod = 20;
		final int yMod = 15;
		
		for(int i = 0; i < RoboticsMaze.mazeGrid.length; i++)
		{
			for(int k = 0; k < RoboticsMaze.mazeGrid[0].length; k++)
			{
				
				if(RoboticsMaze.mazeGrid[i][k].traversable) {
					for(int y = i*drawSize; y < (i+1)*drawSize; y++)
					{
						for(int x = k*drawSize; x < (k+1)*drawSize; x++)
						{
							LCD.setPixel(y + yMod, x + xMod, 0);
						}
					}
				} else {
					for(int y = i*drawSize; y < (i+1)*drawSize; y++)
					{
						for(int x = k*drawSize; x < (k+1)*drawSize; x++)
						{
							LCD.setPixel(y + yMod, x + xMod, 1);
						}
					}

				}
			}
		}
		RBlueTooth.sendObject(RoboticsMaze.mazeGrid);

		Button.LEDPattern(2);
	}

	public static void returnPath(GridSquare current, GridSquare toReturnTo)
	{
		LCD.drawString(current.getX() + " " + current.getY(), 0, 2);
		LCD.drawString(toReturnTo.getX() + " " + toReturnTo.getY(), 0, 4);
		Delay.msDelay(1000);
		RoboticsMaze.movements.pop();
		while (RoboticsMaze.currentX != toReturnTo.getX() || RoboticsMaze.currentY != toReturnTo.getY())
		{
			Sound.beep();
			GridSquare nextMove = RoboticsMaze.movements.pop();
			LCD.drawString(nextMove.getX() + " " + nextMove.getY(), 0, 5);
			if (nextMove.getX() == RoboticsMaze.currentX - 2 && nextMove.getY() == RoboticsMaze.currentY)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
			}
			else if (nextMove.getX() == RoboticsMaze.currentX + 2 && nextMove.getY() == RoboticsMaze.currentY)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
			}
			else if (nextMove.getX() == RoboticsMaze.currentX && nextMove.getY() == RoboticsMaze.currentY - 2)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
			}
			else if (nextMove.getX() == RoboticsMaze.currentX && nextMove.getY() == RoboticsMaze.currentY + 2)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
			}
			RoboticsMaze.pilot.travel(41);
			if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.up) == 0)
			{
				RoboticsMaze.currentY -= 2;

			}
			else if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.down) == 0)
			{
				RoboticsMaze.currentY += 2;

			}
			else if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.left) == 0)
			{
				RoboticsMaze.currentX -= 2;

			}
			else if (RoboticsMaze.currentDirection.compareTo(RoboticsMaze.RDirection.right) == 0)
			{
				RoboticsMaze.currentX += 2;

			}

		}
		RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
		Delay.msDelay(50);
		mapGrid();
		LCD.drawString(RoboticsMaze.currentX + " " + RoboticsMaze.currentY, 0, 3);
	}

	public static void takePathBack(Deque<GridSquare> path)
	{
		int count = 0;
		while (!path.isEmpty())
		{
			GridSquare nextMove = path.pop();
			if (nextMove.getX() == RoboticsMaze.currentX - 1 && nextMove.getY() == RoboticsMaze.currentY)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.left;
				}
				
			}
			else if (nextMove.getX() == RoboticsMaze.currentX + 1 && nextMove.getY() == RoboticsMaze.currentY)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.right;
				}
			}
			else if (nextMove.getX() == RoboticsMaze.currentX && nextMove.getY() == RoboticsMaze.currentY - 1)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.down))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.up;
				}
			}
			else if (nextMove.getX() == RoboticsMaze.currentX && nextMove.getY() == RoboticsMaze.currentY + 1)
			{
				if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.right))
				{
					RoboticsMaze.pilot.rotate(90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.left))
				{
					RoboticsMaze.pilot.rotate(-90);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
				else if (RoboticsMaze.currentDirection.equals(RoboticsMaze.RDirection.up))
				{
					RoboticsMaze.pilot.rotate(180);
					RoboticsMaze.currentDirection = RoboticsMaze.RDirection.down;
				}
			}
			if (count % 2 == 0)
			{
				RoboticsMaze.pilot.travel(41);
			}
			count++;
			
			RoboticsMaze.currentX = nextMove.getX();
			RoboticsMaze.currentY = nextMove.getY();
		}
	}
}