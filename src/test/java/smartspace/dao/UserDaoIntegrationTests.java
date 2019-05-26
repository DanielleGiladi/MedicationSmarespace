//Last version of tests
package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.After;

import org.springframework.beans.factory.annotation.Autowired;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"spring.profiles.active=default"})

public class UserDaoIntegrationTests {
	private UserDao<String> userDao;
	private EntityFactory factory;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setUserDao(UserDao<String> userDao) {
		this.userDao = userDao;
	}

	// cleanup Element dao after each test
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	// @Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Elements
		// AND I delete all Elements
		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(idString -> this.factory.createNewUser(idString + "", "userSmartspace", "username", "avatar",
						UserRole.MANAGER, 1L))
				.map(this.userDao::create) // Stream of ElementEntity
				.forEach(user -> {
				});

		this.userDao.deleteAll();

		// THEN the dao contains no Elements
		assertThat(this.userDao.readAll()).isEmpty();
	}

	// @Test(expected=Exception.class)
	public void testUpdateAfterDeletion() throws Exception {
		// GIVEN nothing

		// WHEN I create a Test Element
		// AND I delete all Elements
		// AND I update the new Element
		UserEntity user = this.factory.createNewUser("userEmail", "userSmartspace", "username", "avatar",
				UserRole.MANAGER, 1L);
		this.userDao.deleteAll();

		UserEntity update = new UserEntity();
		update.setKey(user.getKey());
		update.setAvatar("New Test");

		this.userDao.update(update);

		// THEN the dao throws as exception
	}

}
