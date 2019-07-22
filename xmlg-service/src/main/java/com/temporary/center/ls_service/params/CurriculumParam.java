package com.temporary.center.ls_service.params;

import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.EducationExperience;
import com.temporary.center.ls_service.domain.ProjectExperience;
import com.temporary.center.ls_service.domain.WorkExperience;

import java.util.List;

public class CurriculumParam{

	private CurriculumVitae curriculum;
	
	private List<EducationExperience> educationList;
	
	private List<ProjectExperience>   projectList;
	
	private List<WorkExperience> 	  workList;

	public CurriculumVitae getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(CurriculumVitae curriculum) {
		this.curriculum = curriculum;
	}

	public List<EducationExperience> getEducationList() {
		return educationList;
	}

	public void setEducationList(List<EducationExperience> educationList) {
		this.educationList = educationList;
	}

	public List<ProjectExperience> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<ProjectExperience> projectList) {
		this.projectList = projectList;
	}

	public List<WorkExperience> getWorkList() {
		return workList;
	}

	public void setWorkList(List<WorkExperience> workList) {
		this.workList = workList;
	}
	
}
