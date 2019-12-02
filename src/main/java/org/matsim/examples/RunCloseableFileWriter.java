package org.matsim.examples;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitRoute;
import org.matsim.pt.transitSchedule.api.TransitRouteStop;
import org.matsim.pt.transitSchedule.api.TransitScheduleReader;
import org.matsim.pt.transitSchedule.api.TransitScheduleWriter;


public class RunCloseableFileWriter {
	public static void main(String[] args) {
		
		//Define Input and output folders
		Path transitScheduleInput = Paths.get("D:\\TUbit\\Shared\\MATSim HA2\\Line Testen\\berlin-v5.3-10pct.output_transitSchedule.xml.gz");
		Path transitScheduleOutput = Paths.get("D:\\TUbit\\Shared\\MATSim HA2\\Line Testen\\berlin-Transit.xml.gz");
		Path networkInput = Paths.get("D:\\TUbit\\Shared\\MATSim HA2\\Line Testen\\berlin-v5.3-10pct.output_network.xml.gz");
		String Stopsoutput = "D:\\TUbit\\Shared\\MATSim HA2\\Line Testen\\Stops.txt";
		
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		// Read in existing files
		new TransitScheduleReader(scenario).readFile(transitScheduleInput.toString());
		new MatsimNetworkReader(scenario.getNetwork()).readFile(networkInput.toString());
		ArrayList<String> stopsLine = new ArrayList<>();
		
		//Define lines that should be deleted 
		Set<String> linesToDelete = new HashSet<>(Arrays.asList("17326_700"));
		
		
		//In case multiple Lines should be deleted
		
		for (String lineID: linesToDelete) {	
			RunCloseableFileWriter.deleteLine(scenario, lineID, stopsLine);	
		}	
//		write new Schedule
		new TransitScheduleWriter(scenario.getTransitSchedule()).writeFile(transitScheduleOutput.toString());
//		write list with the Tranport facilities
		CloseableFileWriter fileWriter = new CloseableFileWriter();	
		fileWriter.setPath(Stopsoutput);
		LinkedHashSet<String> hashSet = new LinkedHashSet<>(stopsLine);
	    ArrayList<String> listWithoutDuplicate = new ArrayList<>(hashSet);
	    System.out.println(listWithoutDuplicate);
		fileWriter.writeFileFinalize(listWithoutDuplicate);
//		fileWriter.writeFileCloseable(listWithoutDuplicate);
		System.gc();
		}
	//Remove all the Routes from a line
		public static void deleteLine(Scenario scenario, String lineID, ArrayList<String> StopsLine) {
			
			TransitLine ptLine = scenario.getTransitSchedule().getTransitLines().get(Id.create(lineID, TransitLine.class));
			
			List<TransitRoute> toRemove = ptLine.getRoutes().values().stream().collect(Collectors.toList());
			for(TransitRoute route: toRemove) {
				List<TransitRouteStop> Stops = route.getStops(); 
				for (Object obj : Stops) {
					StopsLine.add(obj.toString());
				}
				ptLine.removeRoute(route);	
		}	
	}

}
