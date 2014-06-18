package net.antiparagon.analog6.block.math;

import net.antiparagon.analog6.block.StatelessBlock;

public class Sum extends StatelessBlock {

	public Sum(String name) {
		super(name);
	}
	
	@Override
	public void doStep() {
		setOutput(getInput());
	}
}
