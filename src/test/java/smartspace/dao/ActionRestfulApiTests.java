package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
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

import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.ActionBoundary;
import smartspace.layout.ElementBoundary;
import smartspace.layout.GenericKey;
import smartspace.layout.LatLng;
import smartspace.layout.UserKey;
import smartspace.plugins.CountSpecificMedicationResponse;
import smartspace.plugins.GetAllCheckResponse;
import smartspace.plugins.SearchMedicationResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ActionRestfulApiTests {

	private int port;

	private AdvancedUserDao<String> users;

	private String baseUrl;

	private RestTemplate rest;

	private AdvancedElementDao<String> elements;
	private AdvancedActionDao actions;

	private ElementEntity elementEntity;

	@Autowired
	public void setElements(AdvancedElementDao<String> elements) {
		this.elements = elements;
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

	public ElementEntity getElementEntity() {
		return elementEntity;
	}

	public void setElementEntity(ElementEntity elementEntity) {
		this.elementEntity = elementEntity;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions";
		this.rest = new RestTemplate();

		UserEntity playerUser = new UserEntity("Player@email.com", "Player", "player", ":-)", UserRole.PLAYER, 1L);
		users.create(playerUser);

		ElementBoundary element = new ElementBoundary();
		element.setKey(new GenericKey("5", "smartspace"));
		element.setElementType("PAINKILLER");
		element.setExpired(false);
		element.setElementProperties(new HashMap<>());
		element.setCreator(new UserKey("manger@manger", "Player"));
		element.setLatlng(new LatLng(7, 7));
		element.setName("acamol");
		this.elementEntity = elements.create(element.toEntity());
	}

	@After
	public void teardown() {
		this.elements.deleteAll();
		this.users.deleteAll();
		this.actions.deleteAll();
	}

	@Test
	public void testInvokeSearchMedication() throws Exception {
		// GIVEN the database is clean

		// WHEN I post a new action with type SearchMedication
		ActionBoundary action = new ActionBoundary();
		action.setType("SearchMedication");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());

		SearchMedicationResponse res = this.rest.postForObject(this.baseUrl, action, SearchMedicationResponse.class);

		// THEN the result from the action is (7,7)
		assertThat(res.getResult()).isEqualToComparingFieldByField(new Location(7, 7));

	}

	@Test
	public void testInvokeCheckIn() throws Exception {
		// GIVEN the database is clean

		// WHEN I post a new action with type CheckIn
		ActionBoundary action = new ActionBoundary();
		action.setType("CheckIn");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		this.rest.postForObject(this.baseUrl, action, Object.class);
		this.rest.postForObject(this.baseUrl, action, Object.class);

		// THEN the database contains one action
		assertThat(this.actions.readAll()).hasSize(1);

	}

	@Test
	public void testInvokeCheckOut() throws Exception {
		// GIVEN the database is clean

		// When i try to do checkOut before checkIn
		ActionBoundary action = new ActionBoundary();
		action.setType("CheckOut");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		this.rest.postForObject(this.baseUrl, action, Object.class);

		// THEN the database contains 0 actions

		assertThat(this.actions.readAll()).hasSize(0);

	}

	@Test
	public void testInvokeGetAllCheckIn() throws Exception {
		// GIVEN the database contains one action with type "CheckIn"
		ActionBoundary action = new ActionBoundary();
		action.setType("CheckIn");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		this.rest.postForObject(this.baseUrl, action, Object.class);
		// WHEN I post a new action with type "GetAllCheckIn

		action.setType("GetAllCheckIn");
		GetAllCheckResponse res = this.rest.postForObject(this.baseUrl, action, GetAllCheckResponse.class);

		// THEN the action returns only one result
		assertThat(res.getResult()).hasSize(1);

	}

	@Test
	public void testInvokeGetAllCheckOut() throws Exception {
		// GIVEN the database is clean

		// WHEN I post a new action with type GetAllCheckOut (without doing checkOut)
		ActionBoundary action = new ActionBoundary();
		action.setType("GetAllCheckOut");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		GetAllCheckResponse res = this.rest.postForObject(this.baseUrl, action, GetAllCheckResponse.class);

		// THEN the action return 0 results
		assertThat(res.getResult()).hasSize(0);

	}

	@Test
	public void testInvokeCountSpecificMedication() throws Exception {
		// GIVEN the database has 1 element (from init())

		// WHEN I post a new action with type CountSpecificMedication
		ActionBoundary action = new ActionBoundary();
		action.setType("CountSpecificMedication");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		CountSpecificMedicationResponse res = this.rest.postForObject(this.baseUrl, action,
				CountSpecificMedicationResponse.class);

		// THEN the database contains new action

		// AND the action result is 1
		assertThat(res.getResult() == 1);

	}

	@Test
	public void testInvokeAutoLocate() throws Exception {
		// GIVEN the database is one element with location (7,7)

		// WHEN I post a new action with type AutoLocate on this element
		ActionBoundary action = new ActionBoundary();
		action.setType("AutoLocate");
		action.setElement(new GenericKey(this.elementEntity.getElementId(), this.elementEntity.getElementSmartspace()));
		action.setPlayer(new UserKey("Player@email.com", "2019b.danielle.giladi"));
		action.setProperties(new HashMap<>());
		this.rest.postForObject(this.baseUrl, action, Object.class);

		Location location = this.elements
				.readById(this.elementEntity.getElementSmartspace() + "#" + this.elementEntity.getElementId()).get()
				.getLocation();
		// THEN the action return the new element location (1,1)
		assertThat(location.equals(new Location(1, 1)));

	}

}
