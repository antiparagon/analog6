package net.antiparagon.analog6.block.source;

import net.antiparagon.analog6.block.IBlock;
import net.antiparagon.analog6.block.Block;

public class Constant extends Block {
	
	final double constantValue;
	
	public Constant(String name, double constantValue) {
		super(name);
		this.constantValue = constantValue;
	}

	@Override
	public void doStep() { }
	
	@Override
	public double getOutput(double time) {
		return constantValue;
	}

	@Override
	public void addInput(IBlock bi) {
		throw new RuntimeException("Constant block can't take inputs");
	}

	@Override
	public int getNumInputs() {
		return -1; // Source blocks return -1 as a 'source' flag
	}
	
}
