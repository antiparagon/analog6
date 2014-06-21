package net.antiparagon.analog6.block;

import java.util.ArrayList;
import java.util.List;

public class AnalogComputer extends Block {
	
	private List<IBlock> blocks = new ArrayList<IBlock>();
	
	public AnalogComputer(String name) {
		super(name);
	}

	public AnalogComputer addBlock(IBlock block) {
		blocks.add(block);
		return this;
	}
	
	public List<IBlock> getBlocks() {
		List<IBlock> blocksCopy = new ArrayList<IBlock>();
		blocksCopy.addAll(blocks);
		return blocksCopy;
	}
	
	@Override
	public void initialize(double startTime) {
		super.initialize(startTime);
		for(IBlock bi : blocks) {
			bi.initialize(getCurrentTime());
		}
	}

	@Override
	public boolean hasState() {
		for(IBlock bi : blocks) {
			if(bi.hasState()) return true;
		}
		return false;
	}

	@Override
	public void doStep() {
		for(IBlock bi : blocks) {
			//System.out.println(bi.getName() + ".doStep()");
			bi.step(getCurrentTimeStep());
		}
	}
	
	@Override
	public void update() {
		for(IBlock bi : blocks) {
			bi.update();
		}
		super.update();
	}

}
