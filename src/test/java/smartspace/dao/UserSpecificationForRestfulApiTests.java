package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.NewUserForm;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class UserSpecificationForRestfulApiTests {

	private int port;
	private String baseUrl;
	private RestTemplate rest;
	private AdvancedUserDao<String> userDao;

	@Autowired
	public void setUserDao(AdvancedUserDao<String> userDao) {
		this.userDao = userDao;
	}

	// cleanup User Dao after each test
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/users";
		this.rest = new RestTemplate();
		UserEntity adminUser = new UserEntity("Jane@gmail.com", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		userDao.create(adminUser);

	}

	@Test
	public void testNewUserForm() throws Exception {
		// GIVEN the database is clean
		// WHEN I post a new element
		NewUserForm user = new NewUserForm();
		user.setEmail("email@email.com");
		user.setUsername("Avior");
		user.setAvatar(":-*");
		user.setRole("PLAYER");
		this.rest.postForObject(this.baseUrl, user, NewUserForm.class);

		// THEN the database contains two users (one existing by init() and one created
		// by post)
		assertThat(this.userDao.readAll()).hasSize(2);
	}

	@Test
	public void testLoginUser() throws Exception {
		// GIVEN the database contains 1 user

		// WHEN i use get method
		UserBoundary actual = this.rest.getForObject(this.baseUrl + "/login/{userSmartspace}/{userEmail}",
				UserBoundary.class, "2019b.danielle.giladi", "Jane@gmail.com");
		// THEN the db can get the specific user from login
		assertThat(this.userDao.readAll()).hasSize(1).usingElementComparatorOnFields("key")
				.containsExactly(actual.toEntity());
	}

	@Test
	public void testUpdateUser() throws Exception {
		// GIVEN the database contains player user with avior
		UserEntity user = new UserEntity();
		user.setUserEmail("email@email.com");
		user.setUsername("Avior");
		user.setAvatar(":-*");
		user.setRole(UserRole.PLAYER);
		user.setPoints(1L);
		userDao.create(user);

		// WHEN we update the UserRole to be MANAGER

		UserBoundary boundary = new UserBoundary();
		boundary.setUsername("Avior");
		boundary.setRole("MANAGER");
		boundary.setKey(new UserKey("email@email.com", "2019b.danielle.giladi"));
		this.rest.put(this.baseUrl + "/login/{userSmartspace}/{userEmail}", boundary, "2019b.danielle.giladi",
				"email@email.com");

		// THEN the database contains the same user with updated role MANAGER
		assertThat(this.userDao.readById("2019b.danielle.giladi#email@email.com").get().getRole()
				.equals(UserRole.MANAGER));
	}

}
