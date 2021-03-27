package com.realestate.app.service;

import java.util.List;

import com.realestate.app.dto.UserDtoForCreate;
import com.realestate.app.entity.UserEntity;


public interface UserService {

	//FUNCTIONS TO GET DATA FROM DATABASE 
	List<UserEntity> allUsers();
	UserEntity userById(int id);
	
	//FUNCTIONS TO STORE DATA TO DATABASE 
	UserEntity addUser(UserDtoForCreate user);

	//FUNCTIONS TO UPDATE DATA ON DATABASE 
	UserEntity updateUser(UserDtoForCreate user, int id);

	//FUNCTIONS TO DELETE DATA FROM DATABASE 
	UserEntity deleteUser(int id);
	
}
















