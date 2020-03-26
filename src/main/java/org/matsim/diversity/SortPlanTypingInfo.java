package org.matsim.diversity;

import org.matsim.api.core.v01.population.Person;

public class SortPlanTypingInfo implements Comparable<SortPlanTypingInfo>{
	
	private String Rawtype;
	private Double Score;
	private int Rank;
	private int Position;
	private org.matsim.api.core.v01.Id<Person> Id;
	private boolean Nested = false;
	
	private SortPlanTypingInfo(String rawtype,
			Double score,
			int rank, 
			int position,
			org.matsim.api.core.v01.Id<Person> Id) {
		this.Rawtype = rawtype;
		this.Rank = rank;
		this.Score = score;
		this.Position = position;
		this.setId(Id);
	}
	
	public static SortPlanTypingInfo createTypingInfo(String rawtype,
			Double score,
			int rank,
			int position,
			Person person) {
		SortPlanTypingInfo typingInfo = new SortPlanTypingInfo(rawtype, score, rank, position, person.getId());
		if (typingInfo.getScore() == null) typingInfo.setScore(0.);
		return typingInfo;
	}
	
	public void setRawtype (String rawType) {
		this.Rawtype = rawType;
	}
	
	public String getRawtype() {
		return Rawtype;
	}
	
	public void setScore (Double score) {
		if (score == null) this.Score = 0.;
		else {this.Score = score.doubleValue();
		}
	}
	
	public Double getScore() {
		return Score;
	}
	
	public void setRank (int rank) {
		this.Rank = rank;
	}
	
	public  int getRank() {
		return Rank;
	}
	
	public void setPosition (int position) {
		this.Position = position;
	}
	
	public  int getPosition() {
		return Position;
	}
	
	@Override
	public int compareTo(SortPlanTypingInfo o) {
		// TODO Auto-generated method stub
		return this.Score.compareTo(o.Score);
	}

	public boolean getNested() {
		return Nested;
	}

	public void setNested(boolean nested) {
		Nested = nested;
	}

	public org.matsim.api.core.v01.Id<Person> getId() {
		return Id;
	}

	public void setId(org.matsim.api.core.v01.Id<Person> id) {
		Id = id;
	}

}
