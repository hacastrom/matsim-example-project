package org.matsim.analysis;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;

import com.google.inject.Inject;

public class DiversityControlerListener implements IterationEndsListener{

	private static final Logger log = Logger.getLogger(DiversityControlerListener.class);
	
	@Inject
	private Scenario scenario;
	
	@Inject DiversityControlerListener(){}
	
	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		DiversityConfigGroup diversityCfg = (DiversityConfigGroup) scenario.getConfig().getModules().get(DiversityConfigGroup.GROUP_NAME);
		DiversityUtils.adressingDiversity(scenario, diversityCfg);
	}

}
