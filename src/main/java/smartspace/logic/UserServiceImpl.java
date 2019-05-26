package smartspace.logic;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.LogMethod;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.layout.NewUserForm;
import smartspace.layout.UserBoundary;

@Service
public class UserServiceImpl implements UserService {
	private AdvancedUserDao<String> users;
	private CheckUserRole userRole;
	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	@Autowired
	public UserServiceImpl(AdvancedUserDao<String> users, CheckUserRole userRole) {
		this.users = users;
		this.userRole = userRole;
	}

	@LogMethod
	@Override
	@Transactional
	public List<UserEntity> publish(List<UserEntity> users, String adminSmartspace, String adminEmail) {
		if (userRole.isAdmin(adminSmartspace, adminEmail)) {
			for (int i = 0; i < users.size(); i++) {
				if (validateUser(users.get(i))) {
					users.set(i, this.users.createImport(users.get(i)));
				} else {
					throw new RuntimeException("Illegal user input");
				}
			}
			return users;
		} else
			throw new RuntimeException();
	}


	@LogMethod
	@Override
	public List<UserEntity> getUsers(int size, int page, String adminSmartspace, String adminEmail) {
		if (userRole.isAdmin(adminSmartspace, adminEmail))
			return this.users.readAll(size, page, "username");
		else
			throw new RuntimeException();
	}

	@LogMethod
	@Override
	public UserBoundary getUser(String userSmartspace, String userEmail) {
		return new UserBoundary(this.users.readById(userSmartspace + "#" + userEmail).get());
	}

	@LogMethod
	@Override
	public void update(UserEntity entity) {
		this.users.update(entity);
	}
	
	@LogMethod
	@Override
	@Transactional
	public UserEntity publishNewUserForm(NewUserForm userForm) {
		if (getValidateNewUserForm(userForm)) {
			return this.users.create(userForm.toEntity());
		} else {
			throw new RuntimeException("Illegal user input");
		}
	}

	private boolean validateUser(UserEntity userEntity) {
		return userEntity != null && userEntity.getUserSmartspace() != null && userEntity.getUserEmail() != null
				&& checkEmail(userEntity.getUserEmail()) && !smartspace.equals(userEntity.getUserSmartspace())
				&& userEntity.getUsername() != null && userEntity.getAvatar() != null && userEntity.getRole() != null
				&& userEntity.getPoints() >= 0;
	}

	public boolean getValidateNewUserForm(NewUserForm userForm) {
		return userForm != null && userForm.getEmail() != null && userForm.getUsername() != null
				&& checkEmail(userForm.getEmail()) && userForm.getAvatar() != null && userForm.getRole() != null;
	}

	

	private boolean checkEmail(String userEmail) {
		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher mat = pattern.matcher(userEmail);
		return mat.matches();
	}

}