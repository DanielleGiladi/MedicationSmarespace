package smartspace.dao;

import java.util.List;
import java.util.Optional;
import smartspace.data.UserEntity;

public interface UserDao <K>{
	 public UserEntity create (UserEntity userEntity);
	 public  Optional<UserEntity> readById (K key); 
	 public List<UserEntity> readAll();

	public void update(UserEntity userEntity);
	 public void deleteAll();
}
