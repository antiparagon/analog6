package net.antiparagon.analog6.block.math;

import net.antiparagon.analog6.block.Block;

public class Gain extends Block {

	final double gainValue;
	
	public Gain(double gainValue) {
		this.gainValue = gainValue;
	}
	
	@Override
	public void doStep() {
		setOutput(gainValue * getInput());
		// Pass thru block
		update();
	}
}
