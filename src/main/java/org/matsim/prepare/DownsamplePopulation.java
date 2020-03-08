package org.matsim.prepare;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.population.PopulationUtils;

public class DownsamplePopulation {
	
	private static final Logger log = Logger.getLogger(DownsamplePopulation.class);
	
	public static void main (String[] args) {
		
		Population pop = PopulationUtils.readPopulation("input/berlin-v5.5-10pct.plans.xml.gz");
		PopulationUtils.sampleDown(pop, 0.1);
		PopulationUtils.writePopulation(pop, "input/Downsample.xml.gz");
		
	}

}
