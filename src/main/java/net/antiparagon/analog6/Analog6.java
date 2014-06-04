package net.antiparagon.analog6;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import net.antiparagon.analog6.block.AnalogComputer;
import net.antiparagon.analog6.block.integrator.Euler;
import net.antiparagon.analog6.block.math.Gain;
import net.antiparagon.analog6.block.source.Constant;
import net.antiparagon.analog6.block.source.Sine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Analog6 {
	private static final Logger logger = LoggerFactory.getLogger(Analog6.class);
    
	public static void main(String[] args) throws FileNotFoundException {
		logger.info("Analog 6 - SAC Evolved");
		
		AnalogComputer computer = new AnalogComputer();
		//Constant constant = new Constant(2.0);
		Sine sine = new Sine();
		Gain gain = new Gain(3.0);
		gain.addInput(sine);
		
		//gain.addInput(gain);
		Euler euler = new Euler(0.0);
		euler.addInput(gain);
		computer.addBlock(euler);
		computer.addBlock(gain);
		computer.addBlock(sine);
		
		PrintStream out = new PrintStream("test.csv");
		out.println("time, gain, integrator");
		double t = 0.0;
		out.println(t + ", " + gain.getOutput(t) + ", " + euler.getOutput(t));
		double timestep = 0.01;
		for(t = timestep; t <= 1.0; t += timestep) {
			computer.step(timestep);
			computer.update();
			out.println(t + ", " + gain.getOutput(t) + ", " + euler.getOutput(t));
		}
		out.close();
		
	}

}
