package smartspace.dao;

import java.util.List;

import smartspace.data.UserEntity;

public interface AdvancedUserDao<KEY> extends UserDao<KEY> {
	public UserEntity createImport(UserEntity userEntity);

	public List<UserEntity> readAll(int size, int page);

	public List<UserEntity> readAll(int size, int page, String sortAttr);

	public void updatePoints(UserEntity user, Long addPoints);


}