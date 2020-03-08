package org.matsim.analysis;

import java.util.Comparator;

public class SortbyScore implements Comparator<SortPlanTypingInfo> {

	@Override
	public int compare(SortPlanTypingInfo plan1, SortPlanTypingInfo plan2) {
		int compare = 0;
		if (plan2.getScore()-plan1.getScore() > 0) compare = 1;
		if (plan2.getScore()-plan1.getScore() < 0) compare = -1;
			return (compare);
	}

}
