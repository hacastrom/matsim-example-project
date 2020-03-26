package org.matsim.prepare;

import org.matsim.api.core.v01.population.Population;
import org.matsim.core.population.PopulationUtils;

public class DownSamplePopulation {

	public static void main (String [] args) {
		
		Population pop = PopulationUtils.readPopulation("plans/plans.xml.gz");
		PopulationUtils.sampleDown(pop, 0.01);
		PopulationUtils.writePopulation(pop, "downsampled.xml.gz");
		
	}
}
