package com.realestate.app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.realestate.app.entity.LocationEntity;
import com.realestate.app.entity.PropertyEntity;
import com.realestate.app.entity.PropertyInfoEntity;
import com.realestate.app.entity.PropertyTypeEntity;

@Repository
public class PropertyRepository {
	EntityManager em;

	public PropertyRepository(EntityManager em) {
		super();
		this.em = em;
	}

	private static final String GET_ALL_PROPERTIES = "FROM PropertyEntity";
	private static final String GET_ALL_PROPERTY_INFOS = "FROM PropertyInfoEntity";
	private static final String GET_ALL_PROPERTY_TYPES = "FROM PropertyTypeEntity";

	private static final String GET_PROPERTY_BY_ID = "FROM PropertyEntity pe WHERE pe.propertiesId = :id";
	private static final String GET_PROPERTY_INFO_BY_ID = "FROM PropertyInfoEntity pie WHERE pie.propertyInfoId = :id";
	private static final String GET_PROPERTY_TYPE_BY_ID = "FROM PropertyTypeEntity pte WHERE pte.propertyTypeId = :id";
	private static final String CHECK_PROPERTY_INFO_TAKEN = "FROM PropertyEntity pe WHERE pe.propertiesId != :id and pe.propertyInfo = :info";
	private static final String CHECK_LOCATION_INTO_PROPERTY = "FROM PropertyEntity pe WHERE pe.propertyLocation = :id";
	private static final String CHECK_PROPERTY_TYPE_EXIST = "FROM PropertyTypeEntity pte WHERE pte.propertyTypeName = :name and pte.propertyTypeDesc = :desc";
	private static final String CHECK_PROPERTY_TYPE_IN_PROPERTIES = "FROM PropertyEntity pe WHERE pe.propertyType = :id";
	private static final String CHECK_INFO_EXIST_IN_PROPERTY = "FROM PropertyEntity pe WHERE pe.propertyInfo = :id";
	
	// RETRIEVE OPERATIONS DOWN HERE
	// PROPERTIES
	public List<PropertyEntity> getAllProperties() {
		return em.createQuery(GET_ALL_PROPERTIES, PropertyEntity.class).getResultList();
	}

	public PropertyEntity getPropertiesById(Integer id) {
		TypedQuery<PropertyEntity> query = em.createQuery(GET_PROPERTY_BY_ID, PropertyEntity.class).setParameter("id",
				id);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// PROPERTY INFO
	public List<PropertyInfoEntity> getAllPropertyInfos() {
		return em.createQuery(GET_ALL_PROPERTY_INFOS, PropertyInfoEntity.class).getResultList();
	}

	public PropertyInfoEntity getPropertyInfoById(Integer id) {
		TypedQuery<PropertyInfoEntity> query = em.createQuery(GET_PROPERTY_INFO_BY_ID, PropertyInfoEntity.class)
				.setParameter("id", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// PROPERTY INFO
	public List<PropertyTypeEntity> getAllPropertyTypes() {
		return em.createQuery(GET_ALL_PROPERTY_TYPES, PropertyTypeEntity.class).getResultList();
	}

	public PropertyTypeEntity getPropertyTypeById(Integer id) {
		TypedQuery<PropertyTypeEntity> query = em.createQuery(GET_PROPERTY_TYPE_BY_ID, PropertyTypeEntity.class)
				.setParameter("id", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// INSERT OPERATIONS DOWN HERE
	public void insertProperties(PropertyEntity property) {
		em.persist(property);
	}

	public void insertPropertyInfo(PropertyInfoEntity propertyInfo) {
		em.persist(propertyInfo);
	}

	public void insertPropertyType(PropertyTypeEntity propertyType) {
		em.persist(propertyType);
	}

	// UPDATE OPERATIONS DOWN HERE
	public PropertyEntity updateProperty(PropertyEntity property) {
		return em.merge(property);
	}

	public PropertyInfoEntity updatePropertyInfo(PropertyInfoEntity propertyInfo) {
		return em.merge(propertyInfo);
	}

	public PropertyTypeEntity updatePropertyType(PropertyTypeEntity propertyType) {
		return em.merge(propertyType);
	}

	// DELETE OPERATIONS DOWN HERE
	public void deleteProperty(PropertyEntity property) {
		em.remove(property);
	}

	public void deletePropertyInfo(PropertyInfoEntity propertyInfo) {
		em.remove(propertyInfo);
	}

	public void deletePropertyType(PropertyTypeEntity propertyType) {
		em.remove(propertyType);
	}

	// HELPING METHODS DOWN HERE
	public boolean existPropertyType(int id) {
		TypedQuery<PropertyTypeEntity> query = em.createQuery(GET_PROPERTY_TYPE_BY_ID, PropertyTypeEntity.class)
				.setParameter("id", id);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public boolean existPropertyType(String name, String desc) {
		TypedQuery<PropertyTypeEntity> query = em.createQuery(CHECK_PROPERTY_TYPE_EXIST, PropertyTypeEntity.class)
				.setParameter("name", name).setParameter("desc", desc);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean existPropertyTypeInProperties(PropertyTypeEntity id) {
		TypedQuery<PropertyEntity> query = em.createQuery(CHECK_PROPERTY_TYPE_IN_PROPERTIES, PropertyEntity.class)
				.setParameter("id",id);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean existLocationInProperty(LocationEntity id) {
		TypedQuery<PropertyEntity> query = em.createQuery(CHECK_LOCATION_INTO_PROPERTY, PropertyEntity.class)
				.setParameter("id", id);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public boolean existPropertyInfoAnotherProperty(int id, int info) {
		TypedQuery<PropertyTypeEntity> query = em.createQuery(CHECK_PROPERTY_INFO_TAKEN, PropertyTypeEntity.class)
				.setParameter("id", id).setParameter("info", info);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}

	public boolean existPropertyInfo(int id) {
		TypedQuery<PropertyInfoEntity> query = em.createQuery(GET_PROPERTY_INFO_BY_ID, PropertyInfoEntity.class)
				.setParameter("id", id);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean existRropertyWithInfo(PropertyInfoEntity id) {
		TypedQuery<PropertyEntity> query = em.createQuery(CHECK_INFO_EXIST_IN_PROPERTY, PropertyEntity.class)
				.setParameter("id", id);
		try {
			return query.getResultList().get(0) != null;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}

	}
}