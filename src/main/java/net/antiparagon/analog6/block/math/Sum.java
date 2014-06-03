package net.antiparagon.analog6.block.math;

import net.antiparagon.analog6.block.Block;

public class Sum extends Block {

	@Override
	public void doStep() {
		setOutput(getInput());
		// Pass thru block
		update();
	}
}
