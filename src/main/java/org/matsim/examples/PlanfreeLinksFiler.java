package org.matsim.examples;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

public class PlanfreeLinksFiler {	
	
	public static void main (String [] args) { 
	Network network =
	NetworkUtils.readNetwork("D:/SVN-public/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-network.xml.gz");
	

	
	ArrayList<String> planfreeLinks = new ArrayList<>() ;
	int i = 0;
	
	for(Link link : network.getLinks().values()) {
		
		if (link.getAttributes().getAttribute("type") != null) {
		String value = link.getAttributes().getAttribute("type").toString().trim();
		System.out.println(value);
		if ( value.equals("motorway") || value.equals("motorway_link") ) {
			planfreeLinks.add(link.getId().toString());
			i++;
		}	
		}
	}
	System.out.print(i);
	writeIdsToFile(planfreeLinks,"D:/Descargas/planfreeLinks.txt");
	}
	
	
    static void writeIdsToFile(ArrayList<String> linkIds, String outputFile){
        BufferedWriter bw = IOUtils.getBufferedWriter(outputFile);
        try {
            for (int i = 0;i< linkIds.size();i++){
                bw.write(linkIds.get(i));
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	


