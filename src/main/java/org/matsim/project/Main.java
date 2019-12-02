package org.matsim.project;

//import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

import java.net.URL;
import java.util.List;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.ControlerConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.ControlerDefaultsModule;
import org.matsim.core.controler.Injector;
import org.matsim.core.controler.NewControlerModule;
import org.matsim.core.controler.corelisteners.ControlerDefaultCoreListenersModule;
import org.matsim.core.router.TripRouter;
import org.matsim.core.scenario.ScenarioByInstanceModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.facilities.FacilitiesUtils;
import org.matsim.facilities.Facility;
import com.google.inject.Guice;

public class Main {
	
	public static void main (String[] args) {
		
//		Module module = new AbstractModule() {
//			@Override 
//			protected void configure() {
//				bind( Abc.class).to( AbcImpl1.class);
//				bind( Helper.class).to(HelperImpl1.class)
//			}
//		};
//	
//	Injector injector = Guice.createInjector();
//	Abc abc = Injector.getInstance( Abc.class);
//	abc.doSomething();
		
//		Config config = ConfigUtils.createConfig();
//		Module module = new AbstractModule() {
//			@Override public void install() {
//				bind (Abc.class).to (Abcimpl.class);
//				if (getConfig().controler().getRoutingAlgorhithmType()== ControlerConfigGroup.RoutingAlgorithmType.Dijkstra) {
//				bind(Helper.class).to (HelperImpl2.class).in(Singleton.class);
//				}else {
//				bind(Helper.class).to(HelperImpl2.class);
//				}
//				
//			}
//		}
//		com.google.injector.Injector injector  = Injector.createInjector(config, modules);
		
		URL url = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("equil"),"config.xml");
		Config config = ConfigUtils.loadConfig(url);
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Module module =new AbstractModule(){
			@Override public void install(){
			install(new NewControlerModule());
			install(new ControlerDefaultCoreListenersModule());
			install(new ControlerDefaultsModule());
			install(new ScenarioByInstanceModule( scenario ));
			}
			};
			com.google.inject.Injector injector = Injector.createInjector( config, module );
			
			
			TripRouter tripRouter = injector.getInstance( TripRouter.class);
			String mainMode = TransportMode.car;
			Link fromLink = scenario.getNetwork().getLinks().get("2");
			Link toLink = scenario.getNetwork().getLinks().get("1");
			Facility fromFacility = FacilitiesUtils.wrapLink(fromLink);
			Facility toFacility = FacilitiesUtils.wrapLink(toLink);
			double departureTime = 8.*3600.;
			Person person = null;
			List<? extends PlanElement> result = tripRouter.calcRoute(mainMode, fromFacility, toFacility, departureTime, person);
			
			
			for (PlanElement planElement: result);
			
			
		
	
	
	}	
}
