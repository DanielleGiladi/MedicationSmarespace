package smartspace.logic;

import java.util.List;

import smartspace.data.UserEntity;
import smartspace.layout.NewUserForm;
import smartspace.layout.UserBoundary;

public interface UserService {
	public List<UserEntity> publish(List<UserEntity> userEntity, String adminSmartspace, String adminEmail);

	public List<UserEntity> getUsers(int size, int page, String adminSmartspace, String adminEmail);

	public UserEntity publishNewUserForm(NewUserForm userForm);

	public UserBoundary getUser(String userSmartspace, String userEmail);

	public void update(UserEntity entity);

}