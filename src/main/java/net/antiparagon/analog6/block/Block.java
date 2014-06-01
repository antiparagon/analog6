package net.antiparagon.analog6.block;

import java.util.ArrayList;
import java.util.List;

public abstract class Block implements BlockInterface {

	private String name = "Block";
	private List<BlockInterface> inputs = new ArrayList<BlockInterface>();
	private double blockOutput = 0.0;
	private double portOutput = 0.0;
	private double currentTime = 0.0;
	private double currentTimeStep = 0.0;
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public boolean hasState() { return false; }

	public String getType() { return getClass().getName(); }

	public void initialize(double startTime) { 
		this.currentTime = startTime;
	}

	public void step(double timeStep) { 
		currentTimeStep = timeStep;
		doStep();
	}
	
	public double getCurrentTimeStep() {
		return currentTimeStep;
	}
	
	public double getInput() {
		double input = 0.0;
		for(BlockInterface block : inputs) {
			input += block.getOutput();
		}
		return input;
	}
	
	public abstract void doStep();
	
	public void setOutput(double blockOutput) {
		this.blockOutput = blockOutput;
	}

	public void update() {
		currentTime += currentTimeStep;
		portOutput = blockOutput;
	}
	
	public double getCurrentTime() {
		return currentTime;
	}

	public double getOutput() { 
		return portOutput;
	}

	public void addInput(BlockInterface bi) {
		inputs.add(bi);
	}

	public int getNumInputs() {
		return inputs.size();
	}

	public List<BlockInterface> getInputList() {
		List<BlockInterface> inputsCopy = new ArrayList<BlockInterface>();
		inputsCopy.addAll(inputs);
		return inputsCopy;
	}

	public void clearInputs() {
		inputs.clear();
	}

}
