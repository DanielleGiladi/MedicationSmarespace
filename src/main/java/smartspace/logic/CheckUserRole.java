package smartspace.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Repository
public class CheckUserRole {
	private AdvancedUserDao<String> userDao;

	public CheckUserRole() {
	}

	@Autowired
	public CheckUserRole(AdvancedUserDao<String> dao) {
		this.userDao = dao;
	}

	public AdvancedUserDao<String> getUserDao() {
		return userDao;
	}

	public void setUserDao(AdvancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	public boolean isAdmin(String smartspace, String email) {
		UserEntity user = userDao.readById(smartspace + "#" + email).get();
		if (user != null && user.getRole() == UserRole.ADMIN)
			return true;
		throw new RuntimeException();
	}
	
	public boolean isManager(String smartspace, String email) {
		UserEntity user = userDao.readById(smartspace + "#" + email).get();
		if (user != null && user.getRole() == UserRole.MANAGER)
			return true;
		throw new RuntimeException();
	}
	
	public boolean isPlayer(String smartspace, String email) {
		UserEntity user = userDao.readById(smartspace + "#" + email).get();
		if (user != null && user.getRole() == UserRole.PLAYER)
			return true;
		throw new RuntimeException();
	}
}
