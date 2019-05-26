package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MoreUserDaoIntegrationTests {
	private UserDao<String> userDao;
	private EntityFactory factory;

	@Autowired
	public void setUserDao(UserDao<String> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	// cleanup User Dao after each test
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Users
		// AND I delete all Users
		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(idString -> this.factory.createNewUser(idString + "", "userSmartspace", "username", "avatar",
						UserRole.MANAGER, 1L))
				.map(this.userDao::create) // Stream of ElementEntity
				.forEach(user -> {
				});

		this.userDao.deleteAll();

		// THEN the dao contains no Users
		assertThat(this.userDao.readAll()).isEmpty();
	}

	@Test(expected = Exception.class)
	public void testUpdateAfterDeletion() throws Exception {
		// GIVEN nothing

		// WHEN I create a Test Users
		// AND I delete all Users
		// AND I update the new Users
		UserEntity user = this.factory.createNewUser("userEmail", "userSmartspace", "username", "avatar",
				UserRole.MANAGER, 1L);
		this.userDao.create(user);
		this.userDao.deleteAll();

		UserEntity update = this.factory.createNewUser("userEmail", "userSmartspace", "username", "avatar",
				UserRole.MANAGER, 1L);
		update.setKey(user.getKey());
		update.setAvatar("New Test");

		this.userDao.update(update);

		// THEN the Dao throws as exception
	}

	@Test
	public void testUpdateUserName() throws Exception {
		// GIVEN a user with userName Guy exists
		UserEntity existing = this.factory.createNewUser("userEmail", "userSmartspace", "username", "avatar",
				UserRole.MANAGER, 1L);
		existing.setUsername("Guy");
		existing = this.userDao.create(existing);

		// WHEN I update the userName to be Avior
		UserEntity update = this.factory.createNewUser("userEmail", "userSmartspace", "username", "avatar",
				UserRole.MANAGER, 1L);
		update.setKey(existing.getKey());
		update.setUsername("Avior");
		this.userDao.update(update);

		// THEN the database contains the userName Avior for this user
		assertThat(this.userDao.readById(existing.getKey())).isPresent().get().extracting("username")
				.containsExactly("Avior");
	}

}