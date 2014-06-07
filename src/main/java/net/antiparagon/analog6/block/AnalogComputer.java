package net.antiparagon.analog6.block;

import java.util.ArrayList;
import java.util.List;

public class AnalogComputer extends Block {
	
	private List<BlockInterface> blocks = new ArrayList<BlockInterface>();
	
	public AnalogComputer(String name) {
		super(name);
	}

	public AnalogComputer addBlock(BlockInterface block) {
		blocks.add(block);
		return this;
	}
	
	@Override
	public void initialize(double startTime) {
		super.initialize(startTime);
		for(BlockInterface bi : blocks) {
			bi.initialize(getCurrentTime());
		}
	}

	public boolean hasState() {
		for(BlockInterface bi : blocks) {
			if(bi.hasState()) return true;
		}
		return false;
	}

	public void doStep() {
		for(BlockInterface bi : blocks) {
			//System.out.println(bi.getName() + ".doStep()");
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
