import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.Sound;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.internal.ev3.EV3LED;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.filter.MeanFilter;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class RoboticsMaze{
	//Initialises the sensors and motors used on the brick, setting what is connected to each port
	static EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A);
	static EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.D);
	static EV3ColorSensor COLOUR_SENSOR = new EV3ColorSensor(SensorPort.S1);
	static IRSensor IRSensor;
	static EV3MediumRegulatedMotor TOP_MOTOR = new EV3MediumRegulatedMotor(MotorPort.C);
	static EV3LED led = new EV3LED();
	//Initialises the move pilot, setting the measurements of the robot
	static Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.6).offset(-6.7);
	static Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.6).offset(6.7);
	static RBlueTooth Rbluetooth;
	static Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 }, WheeledChassis.TYPE_DIFFERENTIAL);
	static MovePilot pilot = new MovePilot(chassis);
	static GridSquare[][] mazeGrid = new GridSquare[19][13];
	static Deque<GridSquare> fullTraverseStack = new ArrayDeque<GridSquare>();
	static Deque<GridSquare> visited = new ArrayDeque<GridSquare>();
	static Deque<GridSquare> movements = new ArrayDeque<GridSquare>();
	static enum RDirection { up, down, left, right};
	static enum RDirectionTop {forward,left,right};
	static RDirectionTop currentTopDirection = RDirectionTop.forward;
	static RDirection currentDirection = RDirection.up;
	static int currentX = 1;
	static int currentY = 11;
	static GridSquare endSquare = mazeGrid[13][9];
	static GridSquare startSquare = mazeGrid[currentX][currentY];
	public static void main(String[] args) {

		EV3 ev3brick = (EV3) BrickFinder.getLocal();
		Keys buttons = ev3brick.getKeys();
		buttons.waitForAnyPress();
		
		final int xMod = 20;
		final int yMod = 15;
		final int drawSize = 5;
		
		for(int i = 0; i < RoboticsMaze.mazeGrid.length; i++)
		{
			for(int k = 0; k < RoboticsMaze.mazeGrid[0].length; k++)
			{
				mazeGrid[i][k] = new GridSquare(i,k,true);
				if(i == 0 || i == 18 || k == 0 || k == 12)
				{
					mazeGrid[i][k].traversable = false;
					
				}
				if(i%2 == 0 && k%2 == 0) {
					mazeGrid[i][k].traversable = false;
				}
				
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
		
		pilot.setLinearSpeed(15);
		pilot.setAngularSpeed(30);
		buttons.getButtons();
		COLOUR_SENSOR.setCurrentMode("RGB");
		SampleProvider leftCol = COLOUR_SENSOR.getRGBMode();
		RoboticsMaze.movements.push(RoboticsMaze.mazeGrid[RoboticsMaze.currentX][RoboticsMaze.currentY]);
		Rbluetooth = new RBlueTooth();
		Rbluetooth.createConnection();
		IRSensor = new IRSensor();
		IRSensor.setDaemon(true);
		IRSensor.start();
		Behavior b1 = new DriveForward();
		Behavior b2 = new DetectWall();
		Behavior b3 = new checkFloorColourGreen();
		Behavior b4 = new checkFloorRed();
		Behavior[] behaviourList = {b1,b2,b3,b4};
		Arbitrator arbitrator = new Arbitrator(behaviourList);
		LCD.clear();
		DetectWall.mapGrid();
		visited.push(mazeGrid[currentX][currentY]);
		arbitrator.go();
		
		

	}
	
	public static void whiteSeen() {
		//both counts are set to zero to show it has not turned during this iteration
		Button.LEDPattern(1);
	}


	public static boolean isGreen() {
		COLOUR_SENSOR.setCurrentMode("RGB");
		SampleProvider leftCol = COLOUR_SENSOR.getRGBMode();
		float[] sample = new float[3];
		leftCol.fetchSample(sample, 0);
		if (sample[1] / (sample[0]+sample[1]+ sample[2]) >= 0.3) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isRed() {
		COLOUR_SENSOR.setCurrentMode("RGB");
		SampleProvider leftCol = COLOUR_SENSOR.getRGBMode();
		float[] sample = new float[3];
		leftCol.fetchSample(sample, 0);
		if (sample[0] / (sample[0]+sample[1]+ sample[2]) >= 0.5) {
			return true;
		} else {
			return false;
		}
	}
}
