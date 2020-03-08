package org.matsim.analysis;

import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;

public final class DiversityModule extends AbstractModule{

	@Override
	public void install() {
		
		DiversityConfigGroup diversityConfigGroup = ConfigUtils.addOrGetModule( getConfig(), DiversityConfigGroup.class);
		
		if (diversityConfigGroup.getEnableDiversityModule()) {
			this.addControlerListenerBinding().to(DiversityControlerListener.class);
		}
		
	}

}
