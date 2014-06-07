package net.antiparagon.analog6.block.math;

import net.antiparagon.analog6.block.StatelessBlock;

public class Gain extends StatelessBlock {

	final double gainValue;
	
	public Gain(String name, double gainValue) {
		super(name);
		this.gainValue = gainValue;
	}
	
	@Override
	public void doStep() {
		setOutput(gainValue * getInput());
	}
}
