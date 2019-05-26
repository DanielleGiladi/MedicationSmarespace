package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.IntStream;

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
import smartspace.data.util.Factory;
import smartspace.layout.UserBoundary;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class UserControllerGeneralIntegrationTests {
	private int port;

	private Factory factory;

	private AdvancedUserDao<String> users;

	private String baseUrl;

	private RestTemplate rest;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users";
		this.rest = new RestTemplate();
		UserEntity adminUser = new UserEntity("Jane", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		users.create(adminUser);
	}

	@After
	public void teardown() {
		this.users.deleteAll();
	}

	@Test
	public void testPublish() throws Exception {
		// GIVEN the database is clean
		String jane = "Jane";
		// WHEN I post a new element
		UserBoundary[] newUsers = new UserBoundary[1];
		UserBoundary user = new UserBoundary();
		user.setKey(new UserKey("email@email.com", "smartGuy"));
		user.setUsername("Avior");
		user.setAvatar(":-*");
		user.setRole("PLAYER");
		user.setPoints(1L);
		newUsers[0] = user;
		this.rest.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}", newUsers, UserBoundary[].class,
				"2019b.danielle.giladi", jane);

		// THEN the database contains two users (one existing by init() and one created
		// by post)
		assertThat(this.users.readAll()).hasSize(2);
	}

	@Test
	public void testGetUsersWithPagination() throws Exception {
		// GIVEN the database contains 10 users
		String jane = "Jane";
		int inputSize = 10;
		IntStream.range(1, inputSize + 1).mapToObj(idString -> this.factory.createNewUser(idString + "",
				"userSmartspace", "username", "avatar", UserRole.MANAGER, 1L)).forEach(this.users::create);

		// WHEN I get all users using page size of 3 and page 1
		int page = 1;
		int size = 3;
		UserBoundary[] actual = this.rest.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}?page={page}&size={size}", UserBoundary[].class,
				"2019b.danielle.giladi", jane, page, size);

		// THEN I get 3 users
		int expectedSize = 3;
		assertThat(actual).hasSize(expectedSize);

	}
}
