package org.matsim.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matsim.analysis.DiversityConfigGroup.DiversityEvaluationMethod;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.routes.NetworkRoute;

public class DiversityUtils {
				
	
	public static void getTestPopulation (Population population) {
		
		Id<Person> id = Id.createPersonId("hugo-Test");
		Coord home = new Coord(454788.14,5735115.49);
		Coord work = new Coord(451947,5736160);
		
		Activity h = population.getFactory().createActivityFromCoord("h", home );
		Activity w = population.getFactory().createActivityFromCoord("w", work );
		Leg car = population.getFactory().createLeg("car");
		Leg pt = population.getFactory().createLeg("pt");
		Leg bike = population.getFactory().createLeg("bike");
		
		Plan plan1 = population.getFactory().createPlan();
		plan1.addActivity(h);
		plan1.addLeg(car);
		plan1.addActivity(w);
		plan1.addLeg(car);
		plan1.addActivity(h);
		plan1.setScore(100.0);
		Plan plan2 = population.getFactory().createPlan();
		plan2.addActivity(h);
		plan2.addLeg(pt);
		plan2.addActivity(w);
		plan2.addLeg(pt);
		plan2.addActivity(h);
		plan2.setScore(50.0);
		Plan plan3 = population.getFactory().createPlan();
		plan3.addActivity(h);
		plan3.addLeg(bike);
		plan3.addActivity(w);
		plan3.addLeg(bike);
		plan3.addActivity(h);
		Plan plan4 = population.getFactory().createPlan();
		plan4.addActivity(h);
		plan4.addLeg(pt);
		plan4.addActivity(w);
		plan4.addLeg(pt);
		plan4.addActivity(h);
		plan4.setScore(75.0);
		
		Person test = population.getFactory().createPerson(id);
		population.addPerson(test);
		population.getPersons().get(id).addPlan(plan1);
		population.getPersons().get(id).addPlan(plan2);
		population.getPersons().get(id).addPlan(plan3);
		population.getPersons().get(id).addPlan(plan4);
	}
	
	public static void setStatisticAttributesinPopulation (Population population) {
		
		Map<Id<Person>, ? extends Person> persons = population.getPersons();
		for (Id<Person> ID : persons.keySet()) {
			
			population.getPersons().get(ID).getAttributes().putAttribute("ScoreAverage", null);
			population.getPersons().get(ID).getAttributes().putAttribute("ScoreSD", null);

			double plansStandardDeviation = 0;
			double plansScoreAverage =  0;
			
			List<? extends Plan> plans = population.getPersons().get(ID).getPlans();
			int NumberofPlans = plans.size();
			for (Plan plan : plans) {
				if (plan.getScore() == null){
				NumberofPlans--;
				}
				else {
				plansScoreAverage = plansScoreAverage+plan.getScore();	
				}
			}
			plansScoreAverage = plansScoreAverage/NumberofPlans;
			for (Plan plan1 : plans) {
				if (plan1.getScore() == null);
				else {
				plansStandardDeviation =+ (plan1.getScore()-plansScoreAverage)*(plan1.getScore()-plansScoreAverage);	
				}
			}
			plansStandardDeviation = plansStandardDeviation/NumberofPlans;	
			plansStandardDeviation = Math.sqrt(plansStandardDeviation);
			
			if (NumberofPlans > 0 ) {
				population.getPersons().get(ID).getAttributes().putAttribute("ScoreAverage", plansScoreAverage);
				population.getPersons().get(ID).getAttributes().putAttribute("ScoreSD", plansStandardDeviation);
			}
			
			System.out.println("ID = "+ ID + " sd = " + plansStandardDeviation + " mw = " + plansScoreAverage);
		}	
	}
	
	public static double getPlanScoresMean(List<? extends Plan> plans) {
		double mean = plans.stream().mapToDouble(Plan::getScore).summaryStatistics().getAverage();
		return(mean);
	}
	
	public static double getLegSimilarity (Leg leg1, Leg leg2) {
		double legSimilarity = 0;
		
		if (leg1.getMode() != leg2.getMode())return legSimilarity;
		Route route1 = leg1.getRoute() ;
		Route route2 = leg2.getRoute() ;
		NetworkRoute nr1, nr2 ;
		
		if (route1 instanceof NetworkRoute) {
			nr1 = (NetworkRoute) route1;
		}else return 1;
		
		if (route1 instanceof NetworkRoute) {
			nr2 = (NetworkRoute) route2;
		}else return 1;
		
		return legSimilarity;
	}
	
	public static List<String> getModeChain (Plan plan){
		List <String> modeChain = new ArrayList<String>();
		List<Leg> legs = PopulationUtils.getLegs(plan);
		for (Leg leg : legs) {
			modeChain.add(leg.getMode().toString());
		}
		return(modeChain);
	}

	public static Set<String> getModes(Plan plan){
		Set <String> modes = new HashSet<String>();
		List<String> modeChain = getModeChain(plan);
		for (String mode: modeChain) {
			if (modes.contains(mode)) {
		}
			else {
				modes.add(mode);
			}
		}
		return(modes);
	}
	
	
	public static boolean evaluateDiversity(Person person, Scenario scenario, DiversityConfigGroup diversityCfg ) {
		boolean diversity = false;
		DiversityEvaluationMethod diversityEvaluationMethod = diversityCfg.getDiversityEvaluationMethod(); 
		int numberofPlans = person.getPlans().size();
		if (numberofPlans == 1) {
			System.out.println("Just one plan for person: " + person.getId().toString());
			return diversity;	
		}
		Plan referencePlan = person.getSelectedPlan();
		if (referencePlan == null) {
			System.out.println("Person " + person.getId().toString() + "has not a selected plan");
			referencePlan = person.getPlans().get(0);
		}
			if (diversityEvaluationMethod.equals(DiversityEvaluationMethod.BYCHAIN)) {
				for (Plan plan: person.getPlans()) {
					plan.setType(getModeChain(plan).toString());
					if (getModeChain(referencePlan).equals(getModeChain(plan))) {
					} else {
						diversity = true;
					}
				}
			}
			if (diversityEvaluationMethod.equals(DiversityEvaluationMethod.BYMODES)) {
				for (Plan plan: person.getPlans()) {
					plan.setType(getModes(plan).toString());
					if (getModes(referencePlan).equals(getModes(plan))) {
					} else {
						diversity = true;
					}
				}
			}
			if (diversityEvaluationMethod.equals(DiversityEvaluationMethod.BYROUTES)) {
				for (Plan plan: person.getPlans()) {
					if (getModes(referencePlan).equals(getModes(plan))) {
					} else {
						diversity = true;
					}
				}
			}
		
		if (diversityCfg.getExclusionCriteria()) {
			tipifybyExclusionCriteria(person, diversityCfg.getNumberofnests(), scenario);
		}
		return diversity;
	}
	
	public static void adressingDiversity(Scenario scenario, DiversityConfigGroup diversityCfg) {
		
		Population population = scenario.getPopulation();
		double aux = 0; 
		int proccessedPersons = 0;
		population.getAttributes().putAttribute("diverse", false);
		Map<Id<Person>, ? extends Person> persons = population.getPersons(); 
		int rang = persons.size()/100 ;
		if (rang == 0) rang = 1 ;
		
		for (Id<Person> ID : persons.keySet()) {
			Person person = population.getPersons().get(ID);
			population.getPersons().get(ID).getAttributes().putAttribute("diverse", evaluateDiversity(person, scenario, diversityCfg));
			if (population.getPersons().get(ID).getAttributes().getAttribute("diverse").toString() == "true") {
				aux++;
			}
			proccessedPersons++;
			if(proccessedPersons % rang == 0) {
				System.out.println(proccessedPersons + " have been processed");
			}
		}
		
		aux = (aux/(population.getPersons().size()))*100;
		System.out.println("the " + aux + "% is diverse");
	}
	
	public static void createDiverseSubpopulation (Population population) {
		String subpopulation = null;
		Map<Id<Person>, ? extends Person> persons = population.getPersons(); 
		for (Id<Person> ID : persons.keySet()) {
			Person person = population.getPersons().get(ID);
			if (population.getPersons().get(ID).getAttributes().getAttribute("diverse").toString() == "true") {
				PopulationUtils.putSubpopulation(person, subpopulation);
			}
		}
	}
	
	public static void tipifybyExclusionCriteria (Person person, int numberofNests, Scenario scenario) {
		
		List<? extends Plan> plans = person.getPlans();		
		
		if (plans.size() <= numberofNests);
		else {
		List<SortPlanTypingInfo> typingInfo = generatePlanTypingInfo(person);
	
		for (int nestReference = 0;nestReference < numberofNests; nestReference++) {
			if (allNested(typingInfo)) {
				System.out.println("the number of nests are less for the person: " + person.getId().toString() + " than " + numberofNests);
				break;
			}
			int nestPosition = firstnotnested(typingInfo);
			settingNest(typingInfo, nestPosition, nestReference, scenario);	
		}
		for (int nestposition = 0; nestposition < plans.size(); nestposition++) {
			if (allNested(typingInfo)) break;
			nestposition = firstnotnested(typingInfo);
			Id<Person> id = typingInfo.get(nestposition).getId();
			scenario.getPopulation().getPersons().get(id).getPlans().get(typingInfo.get(nestposition).getPosition()).setType("nest1");
			typingInfo.get(nestposition).setNested(true);
		}

		}
		
	}

	public static int firstnotnested (List<SortPlanTypingInfo> typingInfo) {
		int firstnotnested = 0;
		boolean nested = typingInfo.get(firstnotnested).getNested();
		while (nested == true) {
			firstnotnested++;
			nested = nested && typingInfo.get(firstnotnested).getNested();
		}
		return firstnotnested;
	}
	
	public static void settingNest (List<SortPlanTypingInfo> typingInfo, int nestPosition, int nestReference, Scenario scenario) {
		Population population = scenario.getPopulation();
		SortPlanTypingInfo nestPlanTypingInfo = typingInfo.get(nestPosition);
		String referenceTyp = nestPlanTypingInfo.getRawtype();
		List<? extends Plan> plans = scenario.getPopulation().getPersons().get(nestPlanTypingInfo.getId()).getPlans();
		
		for (SortPlanTypingInfo planTypingInfo: typingInfo) {
			if (planTypingInfo.getRawtype().equals(referenceTyp)) {
				planTypingInfo.setNested(true);
			}
		}
		
		int iterator = 0;
		for (Plan plan: plans) {
			if (plan.getType().equals(referenceTyp)) {
				plan.setType("nest" + (nestReference+1));	
				population.getPersons().get(nestPlanTypingInfo.getId()).getPlans().get(iterator).setType(plan.getType());;
			}
			iterator++;
		}
	}
	
	public static boolean checkPersoninTypingInfo(List<SortPlanTypingInfo> typingInfo) {
		boolean check = true;
		Id<Person> id = typingInfo.get(0).getId();
		for(SortPlanTypingInfo planTypingInfo: typingInfo) {
			Id<Person> id1 = planTypingInfo.getId();
			if (id1.equals(id)); 
			else {
				check = false;
			}
		}
		return check; 
	}
	
	public static List<SortPlanTypingInfo> generatePlanTypingInfo (Person person){
		
		List<SortPlanTypingInfo> typingInfo = new ArrayList<>();
		List<? extends Plan> plans = person.getPlans();
		int position = 0;
		int rank = 0;
		
		for (Plan plan: plans) {
			String rawtype = plan.getType();
			Double score = plan.getScore();
			SortPlanTypingInfo planTypingInfo = SortPlanTypingInfo.createTypingInfo(rawtype, score, rank, position, plan.getPerson());
			position++;
			typingInfo.add(planTypingInfo);
		}
		
		Collections.sort(typingInfo, new SortbyScore());;
		
		for (SortPlanTypingInfo planTypingInfo : typingInfo) {
			planTypingInfo.setRank(rank);
			rank++;
		}
		
		return typingInfo;
	}
	
	public static boolean allNested (List<SortPlanTypingInfo> typingInfo) {
		boolean allNested = true;
		
		for (SortPlanTypingInfo planTypingInfo: typingInfo) {
			allNested =  allNested && planTypingInfo.getNested();
		}
		
		return allNested;
				
	}

}
