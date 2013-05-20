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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.Transaction;

import web.shedule.dao.ProfessorsDao;
import web.shedule.model.Professors;

import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.opensymphony.xwork2.ActionSupport;

@Actions({ @Action(value = "/edit-grid-professor", results = {
		@Result(location = "simpleecho.jsp", name = "success"),
		@Result(location = "simpleecho.jsp", name = "input") }) })
public class EditGridProfessor extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = -3454448309088641394L;
	private static final Log log = LogFactory.getLog(EditGridProfessor.class);

	private ProfessorsDao professorsDao = new ProfessorsDao();

	@TransactionTarget
	protected Transaction hTransaction;

	private String oper = "";
	private String id;
	private String name;
	private Map<String, Object> session;
//	private List<Professors> listProfessors;

	@SuppressWarnings("unchecked")
	public String execute() throws Exception {
		log.debug("oper :" + oper);
		log.debug("name :" + name);
		log.debug("id :" + id);
		if (oper.equals("edit")) {
			Professors professors = professorsDao.get(Integer.parseInt(id));
			if (professors != null) {
				professors.setName(name);
				professorsDao.update(professors);
			}
		} else if (oper.equals("add")) {
			log.debug("Add Professor");
			Professors professors = new Professors();
			int nextid = professorsDao.nextProfessors();
			log.debug("Id for ne Prof is " + nextid);
			professors.setId(nextid);
			professors.setName(name);
//			listProfessors.add(professors);
			professorsDao.save(professors);
		} else if (oper.equals("del")) {
			/*Professors professors = professorsDao.get(Integer.parseInt(id));
			if (professors != null) {
				listProfessors.remove(professors);
			}*/
			professorsDao.delete(Integer.parseInt(id));
		}
//		log.debug("edit grid prof:"+listProfessors.size());
//		session.put("list_professors", listProfessors);
     	hTransaction.commit();
		return SUCCESS;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}
}
