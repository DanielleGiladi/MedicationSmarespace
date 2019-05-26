package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;

import java.util.stream.Stream;

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
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class UserControllerGetAllUsersIntegrationTests {

	private int port;

	private String baseUrl;

	private RestTemplate rest;

	private AdvancedUserDao<String> users;

	@Autowired
	public void setUserDao(AdvancedUserDao<String> users) {
		this.users = users;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/users";
		this.rest = new RestTemplate();
		UserEntity adminUser = new UserEntity("Jane@email.com", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		users.create(adminUser);
	}

	@After
	public void teardown() {
		this.users.deleteAll();
	}

	@Test
	public void testGetElementsWithExistingAdmin() throws Exception {
		// GIVEN the database contains 1 user
		String jane = "Jane@email.com"; // Admin!

		// WHEN I get all users
		UserBoundary[] usersFromRest = this.rest.getForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}",
				UserBoundary[].class, "2019b.danielle.giladi", jane);

		// THEN I get only 1 user
		assertThat(Stream.of(usersFromRest).map(UserBoundary::getKey).collect(Collectors.toList())).hasSize(1);
	}

}
