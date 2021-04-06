package com.realestate.app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.realestate.app.entity.RoleEntity;
import com.realestate.app.entity.UserEntity;
import com.realestate.app.exceptions.MyExcMessages;
import com.realestate.app.filter.UserFilter;

@Repository
public class UserRepository {

	EntityManager em;

	public UserRepository(EntityManager em) {
		super();
		this.em = em;
	}

	private static final String GET_ALL_USERS_BY_NAME = "FROM UserEntity ue WHERE ue.firstName = :name";

	private static final String GET_USER_BY_ID = "FROM UserEntity u WHERE u.userId = :id";
	private static final String GET_ROLE_BY_ID = "FROM RoleEntity r WHERE r.roleId = :id";
	private static final String GET_OWNER_BY_ID = "SELECT u FROM UserEntity u WHERE u.userId = :id and u.role = :role";

	private static final String CHECK_USERNAME_EXIST = "SELECT u.username FROM UserEntity u WHERE u.username = :username";
	private static final String CHECK_EMAIL_EXIST = "SELECT u.email FROM UserEntity u WHERE u.email = :email";
	private static final String USER_BY_USERNAME = "SELECT u FROM UserEntity u WHERE u.username =?1 and u.active = true";
	private static final String CHECK_BY_USERNAME = "SELECT u FROM UserEntity u WHERE u.username = :username";
	private static final String CHECK_IF_CLIENT = "FROM UserEntity u WHERE u.userId = :id and u.role = :role";

	// RETRIEVE OPERATIONS DOWN HERE
	public List<UserEntity> getAllUsers(UserFilter filter) {
		// Starting query
		String queryString = "SELECT user FROM UserEntity user WHERE 1=1 ";

		// Creating query string for all filtrabl
		queryString = extractedFilterCheck(filter, queryString);
		// setting sort field
		if (filter.getSortBy() != null && !filter.getSortBy().isEmpty()) {
			if (filter.getSortBy().equals("firstName") || filter.getSortBy().equals("lastName")
					|| filter.getSortBy().equals("username")) {
				queryString = queryString + " ORDER BY user." + filter.getSortBy();
			} else {
				throw new MyExcMessages("Sort by must be firstName , lastName or username");
			}
		}
		
		// setting order
		if (filter.getOrder() != null && !filter.getOrder().isEmpty() && filter.getSortBy() != null
				&& !filter.getSortBy().isEmpty()) {
			if (filter.getOrder().equalsIgnoreCase("ASC") || filter.getOrder().equalsIgnoreCase("DESC")) {
				queryString = queryString + " " + filter.getOrder();
			} else {
				throw new MyExcMessages("Order  must be ASC or DESC");
			}
		}

		return extractedFinalQuery(filter, queryString);
	}

	// Extracted
	private List<UserEntity> extractedFinalQuery(UserFilter filter, String queryString) {
		TypedQuery<UserEntity> query = em.createQuery(queryString, UserEntity.class);

		// Setting parameters
		if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
			query.setParameter("firstName", filter.getFirstName());
		}
		if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
			query.setParameter("lastName", filter.getLastName());
		}
		if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
			query.setParameter("username", filter.getUsername());
		}
		return query.getResultList();
	}
	// Extracted
	private String extractedFilterCheck(UserFilter filter, String queryString) {
		if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
			queryString = queryString + "and user.firstName=:firstName ";
		}
		if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
			queryString = queryString + " and user.lastName=:lastName ";
		}
		if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
			queryString = queryString + " and user.username=:username ";
		}
		return queryString;
	}

	// Get users by id
	public UserEntity getUserById(int id) {
		TypedQuery<UserEntity> query = em.createQuery(GET_USER_BY_ID, UserEntity.class).setParameter("id", id);
		try {
			return query.getResultList().get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	// ROLE
	public RoleEntity getRoleById(int id) {
		TypedQuery<RoleEntity> query = em.createQuery(GET_ROLE_BY_ID, RoleEntity.class).setParameter("id", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// INSERT OPERATIONS DOWN HERE
	public void insertUser(UserEntity user) {
		em.persist(user);
	}

	// UPDATE OPERATIONS DOWN HERE
	public UserEntity updateUser(UserEntity user) {
		return em.merge(user);
	}

	// DELETE OPERATIONS DOWN HERE
	public UserEntity deleteUser(UserEntity user) {
		UserEntity u = getUserByUsername(user.getUsername());
		if (u != null) {
			u.setActive(false);
			return em.merge(u);
		}
		return null;
	}

	// ON CONTROLLER METHODS TO FILTER BY NAME
	public List<UserEntity> getFilterByName(String name) {
		return em.createQuery(GET_ALL_USERS_BY_NAME, UserEntity.class).setParameter("name", name).getResultList();
	}

	// HELPING METHODS BELOW HERE
	public boolean existUsername(String username) {
		TypedQuery<String> query = em.createQuery(CHECK_USERNAME_EXIST, String.class).setParameter("username",
				username);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean existEmail(String email) {
		TypedQuery<String> query = em.createQuery(CHECK_EMAIL_EXIST, String.class).setParameter("email", email);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public boolean existUserById(int id, RoleEntity role) {
		TypedQuery<UserEntity> query = em.createQuery(GET_OWNER_BY_ID, UserEntity.class).setParameter("id", id)
				.setParameter("role", role);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public UserEntity getUserByUsername(String username) {
		TypedQuery<UserEntity> query = em.createQuery(USER_BY_USERNAME, UserEntity.class).setParameter(1, username);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean isActiveUser(String username) {
		TypedQuery<UserEntity> query = em.createQuery(CHECK_BY_USERNAME, UserEntity.class).setParameter("username",
				username);
		try {
			UserEntity u = query.getSingleResult();
			return u.isActive() == Boolean.TRUE;
		} catch (NoResultException e) {
			return false;
		}
	}

	public boolean isClient(int id, RoleEntity role) {
		TypedQuery<UserEntity> query = em.createQuery(CHECK_IF_CLIENT, UserEntity.class).setParameter("id", id)
				.setParameter("role", role);
		try {
			return query.getSingleResult() != null;
		} catch (NoResultException e) {
			return false;
		}
	}
}
