package net.antiparagon.analog6.block.integrator;

import net.antiparagon.analog6.block.Block;

public class Euler extends Block {

	public Euler(double initialCondition) {
		setOutput(initialCondition);
	}
		
	@Override
	public boolean hasState() { return true; }

	@Override
	public void doStep() {
		setOutput(getOutput(getCurrentTime()) + (getInput() * getCurrentTimeStep()));
	}
}
