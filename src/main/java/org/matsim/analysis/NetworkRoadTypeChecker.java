package org.matsim.analysis;

import java.util.ArrayList;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;

public class NetworkRoadTypeChecker {
	
	public static void main (String [] args) {
	Network network = NetworkUtils.readNetwork("D:\\SVN-public\\matsim\\scenarios\\countries\\de\\berlin\\berlin-v5.5-10pct\\input\\berlin-v5.5-network-with-bvwp-accidents-attributes.xml.gz");

	for (Id<Link> linkId: network.getLinks().keySet()) {
		String bvwpRoadTypeString = network.getLinks().get(linkId).getAttributes().getAttribute("bvwpRoadType").toString();
		ArrayList<Integer> bvwpRoadType = new ArrayList<>();
		bvwpRoadType.add(0, Integer.valueOf(bvwpRoadTypeString.split(",")[0]));
		bvwpRoadType.add(1, Integer.valueOf(bvwpRoadTypeString.split(",")[1]));
		bvwpRoadType.add(2, Integer.valueOf(bvwpRoadTypeString.split(",")[2]));
	if (bvwpRoadType.get(0).intValue() > 2 || bvwpRoadType.get(0).intValue() < 0 ) {
		System.out.println("first value wrong in " + linkId.toString() );
	}
	if (bvwpRoadType.get(0).intValue() > 3 || bvwpRoadType.get(0).intValue() < 0 ) {
		System.out.println("second value wrong in " + linkId.toString() );
	}
	if (bvwpRoadType.get(1).intValue() > 3 || bvwpRoadType.get(1).intValue() < 0 ) {
		System.out.println("second value wrong in " + linkId.toString() );
	}
	if (bvwpRoadType.get(2).intValue() -1 > 3 || bvwpRoadType.get(2).intValue()-1 < 0 ) {
		double numberofLanes = network.getLinks().get(linkId).getNumberOfLanes();
		System.out.println("third value wrong in " + linkId.toString() + " number of lanes = " + numberofLanes + " and the attribute was = " + bvwpRoadType.get(2).intValue());
		network.getLinks().get(linkId).getAttributes().putAttribute("bvwpRoadType", bvwpRoadType.get(0) + "," + bvwpRoadType.get(1) + "," + bvwpRoadType.get(2)+1);
	}
	}
	NetworkUtils.writeNetwork(network, "D:\\SVN-public\\matsim\\scenarios\\countries\\de\\berlin\\berlin-v5.5-10pct\\input\\berlin-v5.5-network-with-bvwp-accidents-attributes1.xml.gz");
	}
	
}
