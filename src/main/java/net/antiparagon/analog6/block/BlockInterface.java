package net.antiparagon.analog6.block;

import java.util.List;

public interface BlockInterface {
	String getName();
	void setName(String name);
	  
	boolean hasState();
	String getType();
	  
	void initialize(double startTime);
	void step(double timeStep);
	void update();
	double getCurrentTime();
	    
	double getOutput(double time);
	  
	void addInput(BlockInterface bi);
	int getNumInputs();
	List<BlockInterface> getInputList();
	void clearInputs();
}
