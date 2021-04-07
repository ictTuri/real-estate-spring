package com.realestate.app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.realestate.app.entity.IssuesEntity;
import com.realestate.app.entity.PropertyEntity;

@Repository
@Transactional
public class IssueRepository {
	EntityManager em;

	public IssueRepository(EntityManager em) {
		super();
		this.em = em;
	}

	private static final String GET_ALL_ISSUES = "FROM IssuesEntity";
	private static final String GET_ISSUE_BY_ID = "FROM IssuesEntity ie WHERE ie.issueId = :id";
	private static final String EXIST_ISSUE_FOR_POPERTY = "FROM IssuesEntity ie WHERE ie.property = :property";

	// RETRIEVE OPERATIONS DOWN HERE
	// TRADES
	public List<IssuesEntity> getAllIssues() {
		return em.createQuery(GET_ALL_ISSUES, IssuesEntity.class).getResultList();
	}

	public IssuesEntity getIssueById(Integer id) {
		TypedQuery<IssuesEntity> query = em.createQuery(GET_ISSUE_BY_ID, IssuesEntity.class).setParameter("id", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// INSERT OPERATIONS DOWN HERE
	public void insertIssue(IssuesEntity issue) {
		em.persist(issue);
	}

	// UPDATE OPERATIONS DOWN HERE
	public IssuesEntity updateIssue(IssuesEntity issue) {
		return em.merge(issue);
	}

	// DELETE OPERATIONS DOWN HERE
	public void deleteIssue(IssuesEntity issue) {
		em.remove(issue);
	}

	public IssuesEntity existIssueWithProperty(PropertyEntity property) {
		TypedQuery<IssuesEntity> query = em.createQuery(EXIST_ISSUE_FOR_POPERTY, IssuesEntity.class)
				.setParameter("property", property);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}