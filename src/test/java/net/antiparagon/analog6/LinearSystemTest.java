package net.antiparagon.analog6;

import org.junit.Test;

import net.antiparagon.analog6.block.AnalogComputer;
import net.antiparagon.analog6.block.integrator.Euler;
import net.antiparagon.analog6.block.math.Gain;
import net.antiparagon.analog6.block.source.Constant;
import net.antiparagon.analog6.block.source.Sine;

public class LinearSystemTest {

	@Test
	public void SecondOrderTest() {
		Constant constant1 = new Constant("Constant1", 2.0);
		Sine sine1 = new Sine("Sine1", 1.0, 1.0, 0.0);
		Gain gain1 = new Gain("Gain1", 3.0);
		//gain1.addInput(sine1);
		gain1.addInput(constant1);
		//gain1.addInput(constant1);
		Euler euler1 = new Euler("Euler1", 0.0);
		euler1.addInput(gain1);
		
		AnalogComputer computer = new AnalogComputer("Analog Computer");
				
		//computer.addBlock(sine1);
		computer.addBlock(euler1);
		computer.addBlock(gain1);
		computer.addBlock(constant1);
	}
	
}
