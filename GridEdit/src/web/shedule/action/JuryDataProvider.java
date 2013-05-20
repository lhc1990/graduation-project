/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package web.shedule.action;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

import web.shedule.dao.JuryDao;
import web.shedule.dao.JuryInfo;
import web.shedule.dao.ProfessorsDao;
import web.shedule.dao.StudentDao;
import web.shedule.model.Jury;
import web.shedule.model.Professors;
import web.shedule.model.Students;

import com.jgeppert.struts2.jquery.showcase.GridDataProvider;
import com.opensymphony.xwork2.ActionSupport;

@Result(name = "success", type = "json")
public class JuryDataProvider extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 5078264277068533593L;
	private static final Log log = LogFactory.getLog(GridDataProvider.class);

	// Your result List
	private List<JuryInfo> gridModel;
	private Map<String, Object> session;
	// All Records
	private Integer records = 0;

	private ProfessorsDao professorsDao = new ProfessorsDao();
	private JuryDao juryDao = new JuryDao();
	private StudentDao studentDao = new StudentDao();
	List<Professors> listProfessors;
    List<String> listProfessorNames;
	@SuppressWarnings("unchecked")
	public String execute() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Professors.class);
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		listProfessors = professorsDao.getAll();
		List<Jury> listJuries = juryDao.getListJuryByIdSet(6);
		List<Students> listStudents = studentDao.getAll();
		gridModel = new LinkedList<JuryInfo>();
		listProfessorNames=new LinkedList<String>();
		for(Professors professors:listProfessors)
		{
			listProfessorNames.add(professors.getName());
		}
		for (Jury jury : listJuries) {
			Students student = findStudent(jury.getIdsv(), listStudents);
			Professors supervisor = findProfessor(jury.getSupervisor(),
					listProfessors);
			Professors examiner1 = findProfessor(jury.getExaminer1(),
					listProfessors);
			Professors examiner2 = findProfessor(jury.getExaminer2(),
					listProfessors);
			Professors president = findProfessor(jury.getPresident(),
					listProfessors);
			Professors secretary = findProfessor(jury.getSecretary(),
					listProfessors);
			Professors additionalMember = findProfessor(
					jury.getAdditionalmember(), listProfessors);

			if (supervisor != null && examiner1 != null && examiner2 != null
					&& president != null && secretary != null
					&& additionalMember != null) {

				JuryInfo juryInfo = new JuryInfo(jury.getIdju(),
						student.getName(), student.getTitle(),
						supervisor.getName(), examiner1.getName(),
						examiner2.getName(), president.getName(),
						secretary.getName(), additionalMember.getName(),
						jury.getIdset());
				juryInfo.setSupervisorId(supervisor.getId());
				juryInfo.setAdditionalMemberId(additionalMember.getId());
				juryInfo.setPresidentId(president.getId());
				juryInfo.setSecretaryId(secretary.getId());
				juryInfo.setExaminerId1(examiner1.getId());
				juryInfo.setExaminerId2(examiner2.getId());
				gridModel.add(juryInfo);
			}
		}

		return SUCCESS;
	}
    
	private Professors findProfessor(int proId, List<Professors> listProfessors) {
		for (Professors p : listProfessors) {
			if (p.getId() == proId) {
				return p;
			}
		}
		return null;
	}

	private Students findStudent(int idstu, List<Students> listStudent) {
		for (Students p : listStudent) {
			if (p.getId() == idstu) {
				return p;
			}
		}
		return null;
	}

	public String getJSON() {
		return execute();
	}

	public Integer getRecords() {
		return records;
	}

	/**
	 * @return an collection that contains the actual data
	 */
	public List<JuryInfo> getGridModel() {
		return gridModel;
	}

	/**
	 * @param gridModel
	 *            an collection that contains the actual data
	 */
	public void setGridModel(List<JuryInfo> gridModel) {
		this.gridModel = gridModel;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setListProfessors(List<Professors> listProfessors) {
		this.listProfessors = listProfessors;
	}

	public List<Professors> getListProfessors() {
		return listProfessors;
	}
	public void setListProfessorNames(List<String> listProfessorNames) {
		this.listProfessorNames = listProfessorNames;
	}
	public List<String> getListProfessorNames() {
		return listProfessorNames;
	}
}
