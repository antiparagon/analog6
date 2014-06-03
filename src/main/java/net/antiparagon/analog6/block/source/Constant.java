package net.antiparagon.analog6.block.source;

import net.antiparagon.analog6.block.Block;
import net.antiparagon.analog6.block.BlockInterface;

public class Constant extends Block {
	
	final double constantValue;
	
	public Constant(double constantValue) {
		this.constantValue = constantValue;
	}

	@Override
	public void doStep() { }
	
	@Override
	public double getOutput(double time) {
		return constantValue;
	}

	@Override
	public void addInput(BlockInterface bi) {
		throw new RuntimeException("Constant block can't take inputs");
	}

	
}
