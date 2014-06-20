package net.antiparagon.analog6.block.source;

import net.antiparagon.analog6.block.Block;

public class Sine extends Block {

	private double amplitude;
	private double frequency;
	private double phase;
	
	public Sine(String name, double amplitude, double frequency, double phase) {
		super(name);
		this.amplitude = amplitude;
		this.frequency = frequency;
		this.phase = phase;
	}
	  
	public void setAmplitude(double amplitude){ this.amplitude = amplitude; }
	public double getAmplitude(){ return amplitude; }
	
	public void setFrequency(double frequency){ this.frequency = frequency; }
	public double getFrequency(){ return frequency; }
	
	public void setPhase(double phase){ this.phase = phase; }
	public double getPhase(){ return phase; }
	
	@Override
	public void doStep() { }
	
	@Override
	public double getOutput(double time) {
		return amplitude * Math.sin(frequency * time + phase);
	}

	@Override
	public int getNumInputs() {
		return -1; // Source blocks return -1 as a 'source' flag
	}
}
