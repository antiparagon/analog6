package net.antiparagon.analog6.block.integrator;

import net.antiparagon.analog6.block.Block;

public class Euler extends Block {

	public Euler(String name, double initialCondition) {
		super(name);
		setOutput(initialCondition);
		setPortOutput(initialCondition);
	}
		
	@Override
	public void doStep() {
		setOutput(getOutput(getCurrentTime()) + (getInput() * getCurrentTimeStep()));
	}
}
