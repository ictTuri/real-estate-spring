package com.realestate.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.realestate.app.converter.FullPropertyConverter;
import com.realestate.app.dto.FullPropertyDto;
import com.realestate.app.dto.PropertyDto;
import com.realestate.app.entity.IssuesEntity;
import com.realestate.app.entity.LocationEntity;
import com.realestate.app.entity.PropertyEntity;
import com.realestate.app.entity.PropertyInfoEntity;
import com.realestate.app.entity.PropertyTypeEntity;
import com.realestate.app.entity.UserEntity;
import com.realestate.app.entity.enums.PropertyCategory;
import com.realestate.app.exceptions.MyExcMessages;
import com.realestate.app.repository.InfoRepository;
import com.realestate.app.repository.IssueRepository;
import com.realestate.app.repository.LocationRepository;
import com.realestate.app.repository.PropertyRepository;
import com.realestate.app.repository.TradeRepository;
import com.realestate.app.repository.TypeRepository;
import com.realestate.app.repository.UserRepository;
import com.realestate.app.security.UserPrincipal;
import com.realestate.app.service.UserPropertyService;

@Service
@Transactional
public class UserPropertyServiceImpl implements UserPropertyService {

	private static final Logger logger = LogManager.getLogger(UserPropertyServiceImpl.class);
	
	TradeRepository tradeRepo;
	UserRepository userRepo;
	PropertyRepository propertyRepo;
	LocationRepository locationRepo;
	InfoRepository infoRepo;
	TypeRepository typeRepo;
	IssueRepository issueRepo;
	
	public UserPropertyServiceImpl(UserRepository userRepo, PropertyRepository propertyRepo,
			LocationRepository locationRepo, InfoRepository infoRepo, TypeRepository typeRepo,
			TradeRepository tradeRepo, IssueRepository issueRepo) {
		super();
		this.userRepo = userRepo;
		this.propertyRepo = propertyRepo;
		this.locationRepo = locationRepo;
		this.infoRepo = infoRepo;
		this.typeRepo = typeRepo;
		this.tradeRepo = tradeRepo;
		this.issueRepo = issueRepo;
	}

	// Get all properties filter by oner
	@Override
	public List<PropertyDto> showAllMyProperties() {
		UserPrincipal thisUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity user = userRepo.getUserByUsername(thisUser.getUsername());
		
		//LOGGING
		logger.info("Showing all properties of Owner: {}", thisUser);
		List<PropertyDto> toReturn = new ArrayList<>();
		propertyRepo.getPropertiesByOwner(user).forEach(entity -> toReturn.add(FullPropertyConverter.singleToDto(entity)));
		return toReturn;
	}
	
	// Owner insert new property
	@Override
	public PropertyDto insertMyProperty(@Valid FullPropertyDto property) {
		property.setCategory(property.getCategory().toUpperCase());
		UserPrincipal thisUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity user = userRepo.getUserByUsername(thisUser.getUsername());
		if (locationRepo.existLocation(property.getLocation())) {
			PropertyInfoEntity propertyInfoToAdd = FullPropertyConverter.toInfoEntityForCreate(property);
			if (typeRepo.existPropertyType(property.getPropertyType())) {
				LocationEntity location = locationRepo.getLocationById(property.getLocation());
				PropertyTypeEntity propertyType = typeRepo.getPropertyTypeById(property.getPropertyType());
				PropertyEntity propertyToAdd = FullPropertyConverter.toEntityForCreate(property, user,
						propertyType, location, propertyInfoToAdd);
				
				//LOGGING
				logger.info("Inserting new Property, property: {}", propertyToAdd);
				
				propertyRepo.insertProperties(propertyToAdd);
				return FullPropertyConverter.singleToDto(propertyToAdd);
			} else {
				throw new MyExcMessages("No property type with given Id !");
			}
		} else {
			throw new MyExcMessages("No location with given Id !");
		}
	}

	// Update Owner Property
	@Override
	public PropertyDto updateMyProperty(FullPropertyDto property, int id) {
		UserPrincipal thisUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity user = userRepo.getUserByUsername(thisUser.getUsername());
		List<PropertyEntity> list = propertyRepo.getPropertiesByOwner(user);
		for (PropertyEntity pe : list) {
			if (pe.getPropertiesId() == id) {
				PropertyEntity propertyToUpdate = propertyRepo.getPropertiesById(id);
				if (propertyToUpdate != null) {
					PropertyInfoEntity propertyInfoToUpdate = infoRepo
							.getPropertyInfoById(propertyToUpdate.getPropertyInfo().getPropertyInfoId());
					return extractedUpdateProcedure(property,user, propertyToUpdate, propertyInfoToUpdate);
				} else {
					throw new MyExcMessages("No Property with such Id / Please check again");
				}
			}else {
				throw new MyExcMessages("You have no property with given id");
			}
		}
		return null;
	}

	private PropertyDto extractedUpdateProcedure(FullPropertyDto property,UserEntity user, PropertyEntity propertyToUpdate,
			PropertyInfoEntity propertyInfoToUpdate) {
		
			if (locationRepo.existLocation(property.getLocation())) {
				propertyInfoToUpdate.setHasGarage(property.getPropertyInfo().isHasGarage());
				propertyInfoToUpdate.setHasElevator(property.getPropertyInfo().isHasElevator());
				propertyInfoToUpdate.setHasPool(property.getPropertyInfo().isHasPool());
				propertyInfoToUpdate.setArea(property.getPropertyInfo().getArea());
				propertyInfoToUpdate.setFloorNumber(property.getPropertyInfo().getFloorNumber());
				propertyInfoToUpdate.setNrBathrooms(property.getPropertyInfo().getNrBathrooms());
				propertyInfoToUpdate.setNrBedrooms(property.getPropertyInfo().getNrBedrooms());
				infoRepo.updatePropertyInfo(propertyInfoToUpdate);
				propertyToUpdate.setCategory(PropertyCategory.valueOf(property.getCategory()));
				propertyToUpdate.setDescription(property.getDescription());
				LocationEntity location = locationRepo.getLocationById(property.getLocation());
				PropertyTypeEntity propertyType = typeRepo.getPropertyTypeById(property.getPropertyType());
				propertyToUpdate.setOwner(user);
				propertyToUpdate.setPropertyLocation(location);
				propertyToUpdate.setPropertyType(propertyType);
				propertyToUpdate.setPropertyInfo(propertyInfoToUpdate);
				propertyToUpdate.setRentingPrice(property.getRentingPrice());
				propertyToUpdate.setSellingPrice(property.getSellingPrice());
				
				//LOGGING
				logger.info("Owner updateing property {} and property info: {}", propertyToUpdate,propertyInfoToUpdate);
				
				propertyRepo.updateProperty(propertyToUpdate);
				return FullPropertyConverter.singleToDto(propertyToUpdate);

			} else {
				throw new MyExcMessages("Updated Location does not exist !");
			}
	}

	@Override
	public void deleteMyProperty(int id) {
		UserPrincipal thisUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserEntity user = userRepo.getUserByUsername(thisUser.getUsername());
		List<PropertyEntity> list = propertyRepo.getPropertiesByOwner(user);
		for(PropertyEntity pe: list) {
			if(pe.getPropertiesId() == id) {
				PropertyEntity propertyToDelete = propertyRepo.getPropertiesById(id);
				extractedPropertyDeletionValidation(propertyToDelete);
			}else {
				throw new MyExcMessages("You have no property with given id");
			}
		}
	}

	private void extractedPropertyDeletionValidation(PropertyEntity propertyToDelete) {
		if (propertyToDelete != null) {
			IssuesEntity issue = issueRepo.existIssueWithProperty(propertyToDelete);
			if (issue == null) {
				if (tradeRepo.getTradeByProperty(propertyToDelete) == null) {
					propertyRepo.deleteProperty(propertyToDelete);
					
					//LOGGING
					logger.info("Owner Deleting property: {}", propertyToDelete);
					
					infoRepo.deletePropertyInfo(propertyToDelete.getPropertyInfo());
				} else {
					throw new MyExcMessages("Property is Rented or Bought. Can not delete");
				}
			} else {
				throw new MyExcMessages("Property is linked with a issue !");
			}
		} else {
			throw new MyExcMessages("Can not delete Property / Or property does not exist with given Id");
		}
	}

}
