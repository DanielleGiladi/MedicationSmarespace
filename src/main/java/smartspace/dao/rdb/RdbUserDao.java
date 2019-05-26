package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements AdvancedUserDao<String> {
	private UserCrud userCrud;
	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		this.userCrud = userCrud;

	}

	@Override
	@Transactional
	public UserEntity create(UserEntity user) {

		user.setKey(smartspace + "#" + user.getUserEmail());

		// SQL: INSERT
		if (!this.userCrud.existsById(user.getKey())) {
			return this.userCrud.save(user);
		} else {
			throw new RuntimeException("user already exists with id: " + user.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserEntity> readById(String key) {
		// SQL: SELECT
		return this.userCrud.findById(key);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll() {
		// SQL: SELECT
		List<UserEntity> rv = new ArrayList<>();
		this.userCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void update(UserEntity userEntity) {

		UserEntity found = readById(userEntity.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid user key: " + userEntity.getKey()));
		if(userEntity.getAvatar()!= null) {
			found.setAvatar(userEntity.getAvatar());
		}
		if(userEntity.getUsername()!= null) {
			found.setUsername(userEntity.getUsername());
		}
		if(userEntity.getRole() != null) {
			found.setRole(userEntity.getRole());
		}
		
		
	
		// SQL: UPDATE
		this.userCrud.save(found);
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.userCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(int size, int page) {
		return this.userCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserEntity> readAll(int size, int page, String sortAttr) {
		return this.userCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr)).getContent();
	}

	@Override
	@Transactional
	public UserEntity createImport(UserEntity user) {

		user.setKey(user.getUserSmartspace() + "#" + user.getUserEmail());
		// SQL: INSERT
		return this.userCrud.save(user);

	}

	@Override
	public void updatePoints(UserEntity user , Long addPoints) {
		user.setPoints(user.getPoints() +addPoints);
		this.userCrud.save(user);
		
	}
}