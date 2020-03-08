package org.matsim.analysis;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class RunDiversityExample {
	
	public static void main (String[] Args) {
		
		String Plans =  "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/cottbus/cottbus-tutorial-2016/output/cb01/ITERS/it.100/100.plans.xml.gz";
		
		Config config = ConfigUtils.createConfig();
		config.plans().setInputFile(Plans);
//		config.plans().setInputFile(null);
		config.global().setCoordinateSystem("EPSG:31468");
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Population population = scenario.getPopulation();
		DiversityUtils.getTestPopulation(population);
		
		DiversityUtils.setStatisticAttributesinPopulation(population);
//		DiversityUtils.adressingDiversity(scenario, );
		PopulationUtils.writePopulation(population, "scenarios/plansoutput.xml.gz");
		}

}
