package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

//@Repository
public class RamUserDao implements UserDao<String> {
	private List<UserEntity> users;
	private AtomicLong nextId;

	public RamUserDao() {
		this.users = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1L);
	}

	@Override
	public UserEntity create(UserEntity userEntity) {
		userEntity.setKey(this.nextId.getAndIncrement() + "");
		this.users.add(userEntity);
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(String key) {
		return this.users.stream().filter(user -> user.getKey().equals(key)).findFirst();
	}

	@Override
	public List<UserEntity> readAll() {
		return this.users;
	}

	@Override
	public void update(UserEntity userEntity) {
		UserEntity found = readById(userEntity.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid user key: " + userEntity.getKey()));
		found.setAvatar(userEntity.getAvatar());
		found.setPoints(userEntity.getPoints());
		found.setUsername(userEntity.getUsername());

	}

	@Override
	public void deleteAll() {
		this.users.clear();

	}

}
