package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
import smartspace.data.util.Factory;
import smartspace.layout.ActionBoundary;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ActionControllerGetAllActionsIntegrationTests {

	private AdvancedUserDao<String> users;

	private int port;

	private String baseUrl;

	private RestTemplate rest;

	private Factory factory;

	private AdvancedActionDao actions;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setUsers(AdvancedUserDao <String> users) {
		this.users = users;
	}

	@Autowired
	public void setActions(AdvancedActionDao actions) {
		this.actions = actions;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions";
		this.rest = new RestTemplate();
		UserEntity adminUser = new UserEntity("Jane", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		users.create(adminUser);

	}

	@After
	public void teardown() {
		this.actions.deleteAll();
		this.users.deleteAll();
	}

	@Test
	public void testGetElementsWithExistingAdmin() throws Exception {
		// GIVEN the database contains 3 actions
		String jane = "Jane"; // Admin!
		IntStream.range(1, 4)
				.mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace", "actionType", new Date(),
						"playerEmail", "playerSmartspace", null))
				.map(e -> this.actions.create(e)).collect(Collectors.toList());

		// WHEN I get all actions
		ActionBoundary[] actionsFromRest = this.rest.getForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}",
				ActionBoundary[].class, "2019b.danielle.giladi", jane);

		// THEN I get only 3 actions
		assertThat(Stream.of(actionsFromRest).map(ActionBoundary::getActionKey)
		.map(e -> e.getSmartspace() + "#" + e.getId()).map(i -> i.toString()).collect(Collectors.toList()))
				.hasSize(3);
	}

}
