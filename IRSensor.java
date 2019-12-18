import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;

class IRSensor extends Thread {
	EV3IRSensor ir = new EV3IRSensor(SensorPort.S4);
	SampleProvider sp = ir.getDistanceMode();
	SampleProvider average = new MeanFilter(sp, 5);
	float[] sample = new float[average.sampleSize()];
	public int control = 0;
	public float distance = 70;
	IRSensor(){
		
	}
	public void run() {
		while(!Button.ESCAPE.isDown()) {
			
			sp.fetchSample(sample,0);
			distance = sample[0];
		}
		System.exit(0);
	}
}