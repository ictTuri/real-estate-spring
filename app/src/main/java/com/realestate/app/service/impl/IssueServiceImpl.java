package com.realestate.app.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.realestate.app.converter.IssuesConverter;
import com.realestate.app.dto.IssueForUpdateDto;
import com.realestate.app.dto.IssuesDto;
import com.realestate.app.dto.IssuesForCreateDto;
import com.realestate.app.entity.IssuesEntity;
import com.realestate.app.entity.PropertyEntity;
import com.realestate.app.entity.RoleEntity;
import com.realestate.app.entity.UserEntity;
import com.realestate.app.entity.enums.IssueStatus;
import com.realestate.app.exceptions.MyExcMessages;
import com.realestate.app.repository.IssueRepository;
import com.realestate.app.repository.PropertyRepository;
import com.realestate.app.repository.TradeRepository;
import com.realestate.app.repository.UserRepository;
import com.realestate.app.service.IssueService;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

	private static final Logger logger = LogManager.getLogger(IssueServiceImpl.class);
	
	UserRepository userRepo;
	IssueRepository issueRepo;
	TradeRepository tradeRepo;
	PropertyRepository propertyRepo;

	public IssueServiceImpl(TradeRepository tradeRepo, UserRepository userRepo,
			PropertyRepository propertyRepo,IssueRepository issueRepo) {
		super();
		this.tradeRepo = tradeRepo;
		this.issueRepo = issueRepo;
		this.userRepo = userRepo;
		this.propertyRepo = propertyRepo;
	}

	// DISPLAY ALL ISSUES
	@Override
	public List<IssuesDto> allIssues() {
		return IssuesConverter.toDto(issueRepo.getAllIssues());
	}

	// DISPLAY ISSUE BY ID 
	@Override
	public IssuesDto issuesById(int id) {
		IssuesEntity issue = issueRepo.getIssueById(id);
		if (issue != null) {
			
			//LOGGING
			logger.info("Showing issue by id, issue: {}",issue);
			
			return IssuesConverter.toDto(issue);
		} else {
			throw new MyExcMessages("No such issue with given Id !");
		}
	}

	// CREATE NEW ISSUE
	@Override
	public IssuesDto addIssues(IssuesForCreateDto issue) {
		RoleEntity role = userRepo.getRoleById(3);
		if (userRepo.isClient(issue.getClient(), role)) {
			UserEntity user = userRepo.getUserByUsername(issue.getClient());
			PropertyEntity property = propertyRepo.getPropertiesById(issue.getProperty());
			if (tradeRepo.existTrade(user, property)) {
				IssuesEntity issueToAdd = IssuesConverter.toEntityForCreate(issue, user, property);
				issueToAdd.setResoulutionStatus(IssueStatus.UNCHECKED);
				issueRepo.insertIssue(issueToAdd);
				
				//LOGGING
				logger.info("Inserted new issue: {}",issueToAdd);
				
				return IssuesConverter.toDto(issueToAdd);
			} else {
				throw new MyExcMessages("Client and Property have no relation !");
			}
		} else {
			throw new MyExcMessages("Issue owner must be a client");
		}
	}

	// UPDATE EXISTING ISSUE
	@Override
	public IssuesDto updateIssues(IssueForUpdateDto issue, int id) {
		issue.setResoulutionStatus(issue.getResoulutionStatus().toUpperCase());
		IssuesEntity issueToUpdate = issueRepo.getIssueById(id);
		if (issueToUpdate != null) {
			issueToUpdate.setCategory(issue.getCategory());
			issueToUpdate.setDescription(issue.getDescription());
			issueToUpdate.setResoulutionStatus(IssueStatus.valueOf(issue.getResoulutionStatus()));
			issueRepo.updateIssue(issueToUpdate);
			
			//LOGGING
			logger.info("Updated issue: {}",issueToUpdate);
			
			return IssuesConverter.toDto(issueToUpdate);
		} else {
			throw new MyExcMessages("No issue with given id !");
		}
	}

	// DELETE ISSUE BY ID
	@Override
	public void deleteIssue(int id) {
		IssuesEntity issueToDelete = issueRepo.getIssueById(id);
		if (issueToDelete != null) {
			issueRepo.deleteIssue(issueToDelete);
			
			//LOGGING
			logger.info("Deleted issue: {}",issueToDelete);
			
		} else {
			throw new MyExcMessages("Issue with given id does not exist !");
		}
	}

}
