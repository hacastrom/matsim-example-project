/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.run;

import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.ScoringFunctionFactory;

import com.google.inject.Inject;

/**
 * @author nagel
 *
 */
public class RunMatsim {

	public static void main(String[] args) {
		Gbl.assertIf(args.length >=1 && args[0]!="" );
		run(ConfigUtils.loadConfig(args[0]));
		// makes some sense to not modify the config here but in the run method to help  with regression testing.
	}
	
	static void run(Config config) {
		
		// possibly modify config here
		
		// ---
		
		Scenario scenario = ScenarioUtils.loadScenario(config) ;
		
		// possibly modify scenario here
		
		// ---
		
		
		Controler controler = new Controler( scenario ) ;
		
//		controler.addOverridingModule(new AbstractModule() {
//			@Override public void install(){
//				bind(ScoringFunctionFactory.class).to(MyScoringFunctionFactory.class);
//			}
//		}
		// possibly modify controler here
		
		// ---
		
		controler.run();
	}
	
	private static class MyScoringFunctionFactory implements ScoringFunctionFactory{
		
		@Inject private Config config;
		
		
		
//		@Override public void install() {
//		bind (ScoringFunctionFactory.class).to(MyScoringFunctionFactory.class);
//		}

		@Override
		public ScoringFunction createNewScoringFunction(Person person) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
}
