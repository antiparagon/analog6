package net.antiparagon.analog6.block;

import java.util.List;

public interface IBlock {
	String getName();
	void setName(String name);
	  
	boolean hasState();
	String getType();
	  
	void initialize(double startTime);
	void step(double timeStep);
	void update();
	double getCurrentTime();
	    
	double getOutput(double time);
	  
	void addInput(IBlock bi);
	int getNumInputs();
	List<IBlock> getInputList();
	void clearInputs();
}
