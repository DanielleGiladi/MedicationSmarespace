package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

// Unit tests are tests that validates specific function/class/operation
public class RamUserDaoUnitTests {
	private RamUserDao dao;

	// Before and after for coming up possible tests
	@Before
	public void setup() {
		dao = new RamUserDao();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	// @Test
	public void unitTestCreateSingleUserAndValidateContent() throws Exception {
		// Given I created a RamUserDao

		// When we call create method with the new User
		UserEntity user = new UserEntity("Test", "Test", "Test", "Test", UserRole.MANAGER, (long) 100);
		user = dao.create(user);

		// AND it contains the User
		assertThat(dao.readAll()).usingElementComparator((m1, m2) -> m1.getAvatar().compareTo(m2.getAvatar()))
				.contains(new UserEntity("Test", "Test", "Test", "Test", UserRole.MANAGER, (long) 100));

	}

	// @Test
	public void unitTestCreateSingleUserAndValidateKey() throws Exception {
		// Given I created a RamUserDao

		// When we call create method with the user test
		UserEntity user = new UserEntity("Test", "Test", "Test", "Test", UserRole.MANAGER, (long) 100);

		user = dao.create(user);

		// AND the returned user has a non null ID
		assertThat(user.getKey()).isNotNull();
	}

	// @Test(expected=Exception.class)
	public void unitTestCreateNullUser() throws Exception {
		// Given I created a RamUserDao

		// When we call create method with null user
		dao.create(null);

		// Then the method throws exception
	}

	// @Test
	public void unitTestCreateSingleUserAndValidateTotalSize() throws Exception {
		// Given I created a RamUserDao

		// When we call create method with the new User
		UserEntity user = new UserEntity("Test", "Test", "Test", "Test", UserRole.MANAGER, (long) 100);

		user = dao.create(user);

		// Then the internal list length is 1
		assertThat(dao.readAll()).hasSize(1);
	}

	// @Test
	public void unitTestCreateSingleUserAndDeleteAndValidateTotalSize() throws Exception {
		// Given I created a RamUserDao
		RamUserDao dao = new RamUserDao();

		// When we call create method with the UserEntity
		UserEntity user = new UserEntity("Test", "Test", "Test", "Test", UserRole.MANAGER, (long) 100);

		user = dao.create(user);

		// Then Delete all list objects and the internal list length is 0

		dao.deleteAll();
		assertThat(dao.readAll()).hasSize(0);
	}

}
