package org.matsim.analysis;

import org.matsim.core.config.ReflectiveConfigGroup;

public final class DiversityConfigGroup extends ReflectiveConfigGroup {
	
	public static final String GROUP_NAME = "diversity";
	
	public static final String WRITE_NEST_NUMBER = "writeNestNumber";
	
	public DiversityConfigGroup() {
		super(GROUP_NAME);
	}
	
	private boolean enableDiversityModule = false;
	private final String diversityCriteriaName = "DiversityEvaluationMethod";
	private boolean exclusionCriteria = true;
	private int numberofnests = 2;
	private DiversityEvaluationMethod diversityEvaluationMethod = DiversityEvaluationMethod.BYCHAIN; 
	private double allowedSimilarity = 0.5;
	
	public boolean getEnableDiversityModule() {
		return enableDiversityModule;
	}

	public void setEnableDiversityModule(boolean enableDiversityModule) {
		this.enableDiversityModule = enableDiversityModule;
	}

	public String getDiversityCriteriaName() {
		return diversityCriteriaName;
	}

	public int getNumberofnests() {
		return numberofnests;
	}

	public void setNumberofnests(int numberofnests) {
		this.numberofnests = numberofnests;
	}

	public boolean getExclusionCriteria() {
		return exclusionCriteria;
	}

	public void setExclusionCriteria(boolean exclusionCriteria) {
		this.exclusionCriteria = exclusionCriteria;
	}

	public DiversityEvaluationMethod getDiversityEvaluationMethod() {
		return diversityEvaluationMethod;
	}

	public void setDiversityEvaluationMethod(DiversityEvaluationMethod diversityEvaluationMethod) {
		this.diversityEvaluationMethod = diversityEvaluationMethod;
	}

	public double getAllowedSimilarity() {
		return allowedSimilarity;
	}

	public void setAllowedSimilarity(double allowedSimilarity) {
		this.allowedSimilarity = allowedSimilarity;
	}

	public enum DiversityEvaluationMethod {BYMODES,BYCHAIN,BYROUTES}

}
