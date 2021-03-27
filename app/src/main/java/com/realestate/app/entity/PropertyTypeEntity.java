package com.realestate.app.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROPERTY_TYPE")
@Data
@NoArgsConstructor
public class PropertyTypeEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "property_type_id")
	private Integer propertyTypeId;
	
	@Column(name = "property_type_name")
	private String propertyTypeName;
	
	@Column(name = "property_type_desc")
	private String propertyTypeDesc;

	public Integer getPropertyTypeId() {
		return propertyTypeId;
	}

	public void setPropertyTypeId(Integer propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}

	public String getPropertyTypeName() {
		return propertyTypeName;
	}

	public void setPropertyTypeName(String propertyTypeName) {
		this.propertyTypeName = propertyTypeName;
	}

	public String getPropertyTypeDesc() {
		return propertyTypeDesc;
	}

	public void setPropertyTypeDesc(String propertyTypeDesc) {
		this.propertyTypeDesc = propertyTypeDesc;
	}

}