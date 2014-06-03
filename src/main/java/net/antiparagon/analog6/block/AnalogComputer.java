package net.antiparagon.analog6.block;

import java.util.ArrayList;
import java.util.List;

public class AnalogComputer extends Block {
	
	private List<BlockInterface> blocks = new ArrayList<BlockInterface>();
	
	public AnalogComputer() {
		setName("Analog Computer");
	}

	public boolean hasState() {
		for(BlockInterface bi : blocks) {
			if(bi.hasState()) return true;
		}
		return false;
	}

	public void doStep() {
		for(BlockInterface bi : blocks) {
			bi.step(getCurrentTimeStep());
		}
	}
	
	@Override
	public void update() {
		for(BlockInterface bi : blocks) {
			bi.update();
		}
		super.update();
	}

}
