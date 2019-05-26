package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
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
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.GenericKey;
import smartspace.layout.LatLng;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ActionControllerGeneralIntegrationTests {
	private int port;

	private Factory factory;

	private AdvancedUserDao<String> users;

	private String baseUrl;

	private RestTemplate rest;

	private AdvancedElementDao<String> elements;
	private AdvancedActionDao actions;

	@Autowired
	public void setElements(AdvancedElementDao<String> elements) {
		this.elements = elements;
	}

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@Autowired
	public void setActions(AdvancedActionDao actions) {
		this.actions = actions;
	}

	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/actions";
		this.rest = new RestTemplate();

		UserEntity adminUser = new UserEntity("Jane", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		UserEntity playerUser = new UserEntity("Player", "Player", "player", ":-)", UserRole.PLAYER, 1L);
		users.create(playerUser);
		users.create(adminUser);
		ElementBoundary element = new ElementBoundary();
		element.setKey(new GenericKey("5", "smartspace"));
		element.setElementType("type");
		element.setExpired(false);
		element.setElementProperties(new HashMap<>());
		element.setCreator(new UserKey("Player", "Player"));
		element.setLatlng(new LatLng(7, 7));
		element.setName("Avior");
		elements.createImport(element.toEntity());
	}

	@After
	public void teardown() {
		this.elements.deleteAll();
		this.users.deleteAll();
		this.actions.deleteAll();
	}

	@Test
	public void testPublish() throws Exception {
		// GIVEN the database is clean
		String jane = "Jane";
		// WHEN I post a new action
		ActionBoundary[] newActions = new ActionBoundary[1];
		ActionBoundary action = new ActionBoundary();
		action.setActionKey(new GenericKey("5", "smartspace"));
		action.setType("type");
		action.setElement(new GenericKey("5", "smartspace"));
		action.setPlayer(new UserKey("email", "smartGuy"));
		action.setProperties(new HashMap<>());
		action.setCreated(new Date());
		newActions[0] = action;
		ActionBoundary[] actual = this.rest.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}", newActions,
				ActionBoundary[].class, "2019b.danielle.giladi", jane);

		// THEN the database contains a single new action
		// AND the element in the database is similar to the action posted
		assertThat(this.actions.readAll()).hasSize(1).usingElementComparatorOnFields("key")
				.containsExactly(actual[0].toEntity());
	}

	@Test
	public void testGetElementsWithPagination() throws Exception {
		// GIVEN the database contains 10 action
		String jane = "Jane";
		int inputSize = 10;
		IntStream
				.range(1, inputSize + 1).mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace",
						"actionType", new Date(), "playerEmail", "playerSmartspace", null))
				.forEach(this.actions::create);

		// WHEN I get all actions using page size of 3 and page 1
		int page = 1;
		int size = 3;
		ElementBoundary[] actual = this.rest.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}?page={page}&size={size}", ElementBoundary[].class,
				"2019b.danielle.giladi", jane, page, size);

		// THEN I get 3 messages
		int expectedSize = 3;
		assertThat(actual).hasSize(expectedSize);

	}
	
	//TODO
	@Test
	public void testInvokeSearchMedication() throws Exception {
		// GIVEN the database is clean
		
		// WHEN I post a new action with type ECHO
		ActionBoundary action = new ActionBoundary();
		action.setType("SearchMedication");
		action.setElement(new GenericKey("5", "smartspace"));
		action.setPlayer(new UserKey("Player", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		action.setCreated(new Date());
		this.rest.postForObject("http://localhost:" + port + "/smartspace/actions",action,
				ActionBoundary.class);

		// THEN the database contains new action 
		
		// AND the action contain key result in the properties 
		assertThat(this.actions.readAll().get(0).getMoreAttributes()).containsKey("result");
		
	}
}
