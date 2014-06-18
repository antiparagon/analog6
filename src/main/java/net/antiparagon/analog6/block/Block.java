package net.antiparagon.analog6.block;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Block implements BlockInterface {
	private static final Logger logger = LoggerFactory.getLogger(Block.class);

	private String name = "Block";
	private List<BlockInterface> inputs = new ArrayList<BlockInterface>();
	private double blockOutput = 0.0;
	private double portOutput = 0.0;
	private double currentTime = 0.0;
	private double currentTimeStep = 0.0;
	
	public Block(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public boolean hasState() { return true; }

	public String getType() { return getClass().getName(); }

	public void initialize(double startTime) { 
		currentTime = startTime;
	}

	public void step(double timeStep) { 
		currentTimeStep = timeStep;
		currentTime += currentTimeStep;
		doStep();
	}
	
	public double getCurrentTimeStep() {
		return currentTimeStep;
	}
	
	public double getInput() {
		if(getNumInputs() == 0) {
			logger.warn(getName() + " has no inputs. Input of 0 to block.");
		}
		double input = 0.0;
		for(BlockInterface block : inputs) {
			input += block.getOutput(getCurrentTime());
		}
		return input;
	}
	
	public abstract void doStep();
	
	public void setOutput(double blockOutput) {
		this.blockOutput = blockOutput;
	}

	public void update() {
		portOutput = blockOutput;
	}
	
	public double getCurrentTime() {
		return currentTime;
	}

	public double getOutput(double time) {
		if(getCurrentTime() != time) {
			throw new RuntimeException("Block time (" + getCurrentTime() + ") not equal to requested output time (" + time + ")");
		}
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
