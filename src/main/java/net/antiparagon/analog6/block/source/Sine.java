package net.antiparagon.analog6.block.source;

import net.antiparagon.analog6.block.Block;

public class Sine extends Block {

	private double amplitude = 1.0;
	private double frequency = 10.0;
	private double phase = 0.0;
	
	public Sine(String name) {
		super(name);
	}
	  
	public void setAmplitude(double amplitude){ this.amplitude = amplitude; }
	public double getAmplitude(){ return amplitude; }
	
	public void setFrequency(double frequency){ this.frequency = frequency; }
	public double getFrequency(){ return frequency; }
	
	public void setPhase(double phase){ this.phase = phase; }
	public double getPhase(){ return phase; }
	
	@Override
	public void doStep() {
		setOutput(amplitude * Math.sin(frequency * getCurrentTime() + phase));
	}

	@Override
	public int getNumInputs() {
		return -1; // Source blocks return -1 as a 'source' flag
	}
}
