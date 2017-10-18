package de.mw.mwdata.app.admin.angwebrest.service;

import java.util.List;

import de.mw.mwdata.app.admin.angwebrest.model.User;



public interface UserService {
	
	User findById(long id);
	
	User findByName(String name);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserById(long id);

	List<User> findAllUsers(); 
	
	void deleteAllUsers();
	
	public boolean isUserExist(User user);
	
}
