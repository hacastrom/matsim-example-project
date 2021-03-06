package org.matsim.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.diversity.DiversityUtils;
import org.matsim.diversity.DiversityConfigGroup.DiversityEvaluationMethod;

public class DiversityAnalysis {

	private static final Logger log = Logger.getLogger(DiversityAnalysis.class);

	public static void main (String[] args) {
		Population pop = PopulationUtils.readPopulation("plans/plans.xml.gz");
		Analyse(pop);
		ListPersonsbyAttribute(pop, "diverse", false);
		ListPersonsbyAttribute(pop, "diverse", true);
		WritetidyCSV(pop);
	}
	
	public static void ListPersonsbyAttribute(Population pop, String attribute, Object Value) {
		 BufferedWriter bw = null;
	     try {

		 File file = new File("Agents" + attribute + "Equals" + Value.toString() + ".csv");

		  if (!file.exists()) {
		     file.createNewFile();
		  }

		  FileWriter fw = new FileWriter(file);
		  bw = new BufferedWriter(fw);
		  Set<Id<Person>> persons = pop.getPersons().keySet();
		  for (Id<Person> id : persons){
			  if (pop.getPersons().get(id).getAttributes().getAttribute(attribute).equals(Value)) {
				  bw.write(id.toString());
				  bw.newLine();
			  }
		  }
	     } catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		finally
		{ 
		   try{
		      if(bw!=null)
			 bw.close();
		   }catch(Exception ex){
		       log.warn("Error in closing the BufferedWriter"+ex);
		    }
		}
		
	}
	
	public static void Analyse (Scenario scenario) {		
	Population pop = scenario.getPopulation();
	Analyse(pop);
	}
	
	public static void Analyse (Population pop) {
		Double proportion = DiversityUtils.GetDiversePortionofPopulation(pop);

		 BufferedWriter bw = null;
	     try {
		 String mycontent = "Analysis";
	        //Specify the file name and path here
		 File file = new File("Analysis.csv");

		  if (!file.exists()) {
		     file.createNewFile();
		  }

		  FileWriter fw = new FileWriter(file);
		  bw = new BufferedWriter(fw);
		  bw.write(mycontent);
		  bw.newLine();
		  bw.write("the proportion was " + proportion*100 + "%");
		  bw.newLine();
		  bw.write(pop.getPersons().size() +" plans");
	     } catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		finally
		{ 
		   try{
		      if(bw!=null)
			 bw.close();
		   }catch(Exception ex){
		       log.warn("Error in closing the BufferedWriter"+ex);
		    }
		}
	}
	
	@SuppressWarnings("null")
	public static void WritetidyCSV (Population pop) {
		 BufferedWriter bw = null;
	     try {
		 String mycontent = "Analysis";
	        //Specify the file name and path here
		 File file = new File("Personinfo.txt");

		  if (!file.exists()) {
		     file.createNewFile();
		  }
		  FileWriter fw = new FileWriter(file);
		  bw = new BufferedWriter(fw);
		  Set<Id<Person>> persons = pop.getPersons().keySet();
		  bw.write("ID;");
		  bw.write("X;Y;");
		  bw.write("Type;");
		  bw.write("Diverse");
		  bw.newLine();
		  for (Id<Person> id : persons){
			  Person person = pop.getPersons().get(id);
				  bw.write(id.toString()+";");
				  bw.write(GetCoordinatesPerson(person)+";");
				  bw.write(PopulationUtils.getFirstActivity(person.getSelectedPlan()).getType()+";");
				  bw.write(person.getAttributes().getAttribute("diverse").toString());
				  bw.newLine();
			  }
	     } catch (IOException ioe) {
		   ioe.printStackTrace();
		}
		finally
		{ 
		   try{
		      if(bw!=null)
			 bw.close();
		   }catch(Exception ex){
		       log.warn("Error in closing the BufferedWriter"+ex);
		    }
		}
	}
	
	public static String GetCoordinatesPerson(Person person) {
		String coordinates;
		double x = 	PopulationUtils.getFirstActivity(person.getSelectedPlan()).getCoord().getX();
		double y = 	PopulationUtils.getFirstActivity(person.getSelectedPlan()).getCoord().getY();
		coordinates = x + ";" + y;
		return coordinates;
	}
}
