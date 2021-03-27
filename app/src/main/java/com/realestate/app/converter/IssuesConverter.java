package com.realestate.app.converter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.realestate.app.dto.IssuesDto;
import com.realestate.app.dto.IssuesDtoForCreate;
import com.realestate.app.entity.IssuesEntity;
import com.realestate.app.entity.PropertyEntity;
import com.realestate.app.entity.UserEntity;

public class IssuesConverter {
	
	private IssuesConverter() {
	}

	public static IssuesDto toDto(IssuesEntity issue) {
		IssuesDto issueToReturn = new IssuesDto();
		issueToReturn.setClient(UserConverter.toDto(issue.getClient()));
		issueToReturn.setProperty(PropertyConverter.toDto(issue.getProperty()));
		issueToReturn.setCategory(issue.getCategory());
		issueToReturn.setCreatedDate(issue.getCreatedDate());
		issueToReturn.setResoulutionStatus(issue.getResoulutionStatus());
		issueToReturn.setDescription(issue.getDescription());
		return issueToReturn;
	}

	public static List<IssuesDto> toDto(List<IssuesEntity> issues) {
		List<IssuesDto> toReturn = new ArrayList<>();
		for(IssuesEntity ue : issues) {
			toReturn.add(toDto(ue));
		}
		return toReturn;
	}
	
	public static IssuesEntity toEntity(IssuesDto issue) {
		IssuesEntity issueToReturn = new IssuesEntity();
		issueToReturn.setClient(UserConverter.toEntity(issue.getClient()));
		issueToReturn.setProperty(PropertyConverter.toEntity(issue.getProperty()));
		issueToReturn.setCategory(issue.getCategory());
		issueToReturn.setCreatedDate(LocalDateTime.now());
		issueToReturn.setResoulutionStatus(issue.getResoulutionStatus());
		issueToReturn.setDescription(issue.getDescription());
		return issueToReturn;
	}
	
	public static IssuesEntity toEntityForCreate(IssuesDtoForCreate dto, UserEntity client, PropertyEntity property) {
		IssuesEntity toReturn=new IssuesEntity();
		toReturn.setCategory(dto.getCategory());
		toReturn.setClient(client);
		toReturn.setCreatedDate(LocalDateTime.now());
		toReturn.setDescription(dto.getDescription());
		toReturn.setProperty(property);
		toReturn.setIssueId(null);
		return toReturn;
	}
	
}