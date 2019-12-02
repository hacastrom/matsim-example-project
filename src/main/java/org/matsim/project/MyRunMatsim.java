package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

public class MyRunMatsim {
	
	public static void main (String[] args) {
		
		Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");
				
		Scenario scenario = ScenarioUtils.createScenario(config);
		
		Controler controler = new Controler (scenario);
		
		config.controler().setLastIteration(1);
		
		OverwriteFileSetting abc = OverwriteFileSetting.deleteDirectoryIfExists;
		config.controler().setOverwriteFileSetting(abc);
		
		
		controler.run();
		
	}

}
