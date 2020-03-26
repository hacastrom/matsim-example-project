package org.matsim.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.matsim.analysis.DiversityConfigGroup.DiversityEvaluationMethod;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.population.routes.RouteUtils;

public class DiversityUtils {
	
	private static final Logger log = Logger.getLogger( DiversityUtils.class );
				
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
			
		}	
	}
	
	public static double getPlanScoresMean(List<? extends Plan> plans) {
		double mean = plans.stream().mapToDouble(Plan::getScore).summaryStatistics().getAverage();
		return(mean);
	}
	
	public static double calculateSimilarityforDiversity(List<Leg> legs1, List<Leg> legs2, Network network, 
			double sameModeReward, double sameRouteReward ) {
		double simil = 0. ;
		Iterator<Leg> it1 = legs1.iterator();
		Iterator<Leg> it2 = legs2.iterator();
		double totalLegDistance = 0;
		for (Leg leg: legs2) {
			totalLegDistance = leg.getRoute().getDistance() + totalLegDistance;
		}
		for ( ; it1.hasNext() && it2.hasNext(); ) {
			Leg leg1 = it1.next() ;
			Leg leg2 = it2.next() ;			
			Double distanceFactor = leg2.getRoute().getDistance()/totalLegDistance;
		if ( leg1.getMode().equals( leg2.getMode() ) ) {
				simil += sameModeReward * distanceFactor;
			} else {
				continue ;
			}
		Route route1 = leg1.getRoute() ;
			Route route2 = leg2.getRoute() ;
			// currently only for network routes:
			NetworkRoute nr1, nr2 ;
			if ( route1 instanceof NetworkRoute ) {
				nr1 = (NetworkRoute) route1 ;
			} else {
				simil += sameModeReward * distanceFactor;
				// ("no route" is interpreted as "same route".  One reason is that otherwise plans
				// with routes always receive higher penalties than plans without routes in the diversity
				// increasing plans remover, which clearly is not what one wants. kai, jul'18)
				continue ; // next leg
			}
			if ( route2 instanceof NetworkRoute ) {
				nr2 = (NetworkRoute) route2 ;
			} else {
				simil += sameModeReward * distanceFactor;
				continue ; // next leg
			}
			simil += distanceFactor * sameRouteReward * ( RouteUtils.calculateCoverage(nr1, nr2, network) + RouteUtils.calculateCoverage(nr2, nr1, network) ) / 2 ;
		}
		return simil;
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
			return diversity;	
		}
		Plan referencePlan = person.getSelectedPlan();
		if (referencePlan == null) {
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
				Typebysimilarity(person, scenario, diversityCfg);
				for (Plan plan: person.getPlans()) {
					if (referencePlan.getType().equals(plan.getType())) {
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
		int rang = persons.size()/10 ;
		if (rang == 0) rang = 1 ;
		
		for (Id<Person> ID : persons.keySet()) {
			Person person = population.getPersons().get(ID);
			population.getPersons().get(ID).getAttributes().putAttribute("diverse", evaluateDiversity(person, scenario, diversityCfg));
			if (population.getPersons().get(ID).getAttributes().getAttribute("diverse").toString() == "true") {
				aux++;
			}
			proccessedPersons++;
			if(proccessedPersons % rang == 0) {
				log.info(proccessedPersons + " have been processed");
			}
		}
		
		aux = (aux/(population.getPersons().size()))*100;
		log.info("the " + aux + "% is diverse");
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

	public static void Typebysimilarity (Person person, Scenario scenario, DiversityConfigGroup diversityCfg) {

		Plan referencePlan = person.getSelectedPlan();
		if (referencePlan == null) {
			referencePlan = person.getPlans().get(0);
		}
		int counter = 0;
		int type = 1;
		for (Plan plan: person.getPlans()) {
			if (plan.equals(referencePlan)) {
				plan.setType("T0");
			} 
			else {
				double similarity = calculateSimilarityforDiversity(PopulationUtils.getLegs(referencePlan),
					PopulationUtils.getLegs(plan),
					scenario.getNetwork(),
					0,
					1);
				similarity = similarity/((PopulationUtils.getLegs(referencePlan).size()+PopulationUtils.getLegs(referencePlan).size())/2);
				if (similarity > diversityCfg.getAllowedSimilarity()) {
					plan.setType("T0");
					continue;
				}
				if (counter == 0) {
					plan.setType("T"+type);
					type++;
					continue;
				}
				else {
					int iterator = 0;
					while(iterator <= counter) {
						similarity = calculateSimilarityforDiversity(PopulationUtils.getLegs(person.getPlans().get(iterator)),
								PopulationUtils.getLegs(plan),
								scenario.getNetwork(),
								0,
								1);			
						similarity = similarity/((PopulationUtils.getLegs(referencePlan).size()+PopulationUtils.getLegs(referencePlan).size())/2);
						if (similarity > diversityCfg.getAllowedSimilarity()) {
						plan.setType(person.getPlans().get(iterator).getType());
						break;
						}
						if (iterator == counter) {
							plan.setType("T"+type);
							type++;
						}
						iterator++;
					}
				}	
		}
			counter++;
	}
	}

	public static double GetDiversePortionofPopulation (Population population) {

		Map<Id<Person>, ? extends Person> persons = population.getPersons();
		double diversepop = 0;
		double popsize = population.getPersons().size();
		for (Id<Person> id: persons.keySet()) {
			boolean Dpop = (boolean) population.getPersons().get(id).getAttributes().getAttribute("diverse");
			if (Dpop == true) {
				log.info(diversepop);
				diversepop++;
			}
		}
		log.info("diverse persons : " + diversepop + " total persons " + popsize);
		double proportion = 0;	
		if (popsize == 0.0) {
			log.warn("no persons in population");
			return proportion;
		} else {
		proportion = diversepop/popsize;
		log.info("the precentage of persons with diverse plans is = " + proportion*100 + "%");
		return proportion;
		}	
	}
}
