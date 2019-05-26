package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
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
import smartspace.layout.ElementBoundary;
import smartspace.layout.LatLng;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ElementSpecificationForRestfulApiTests {

	private AdvancedUserDao<String> users;
	private int port;
	private String baseUrl;
	private RestTemplate rest;
	private AdvancedElementDao<String> elements;

	@Autowired
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}

	@Autowired
	public void setElements(AdvancedElementDao<String> elements) {
		this.elements = elements;
	}

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/elements";
		this.rest = new RestTemplate();
		UserEntity managerUser = new UserEntity("emailM@email.com", "Jane", "admin", ":-)", UserRole.MANAGER, 1L);
		UserEntity playerUser = new UserEntity("emailP@email.com", "Jane", "admin", ":-)", UserRole.PLAYER, 1L);
		users.create(managerUser);
		users.create(playerUser);
	}

	@After
	public void teardown() {
		this.elements.deleteAll();
		this.users.deleteAll();
	}

	@Test
	public void testUpdateElementByManager() throws Exception {
		// GIVEN the database contains manager user with name avior
		ElementEntity element = new ElementEntity();
		element.setCreateTimestamp(new Date());
		element.setMoreAttributes(new HashMap<>());
		element.setLocation(new Location(2.2, 2.2));
		element.setName("AviorIsAlive");
		element.setExpired(false);
		element.setType("Type");
		element.setCreateEmail("email@email.com");
		element.setCreatorSmartspace("2019b.danielle.giladi");
		element = elements.create(element);
		String elementId = element.getElementId();
		// WHEN we update the name to be tests

		ElementBoundary boundary = new ElementBoundary();
		boundary.setCreated(new Date());
		boundary.setElementProperties(new HashMap<>());
		boundary.setLatlng(new LatLng(2.2, 2.2));
		boundary.setName("tests");
		boundary.setExpired(false);
		boundary.setElementType("Type");
		boundary.setCreator(new UserKey("email@email.com", "2019b.danielle.giladi"));
		this.rest.put(this.baseUrl + "/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", boundary,
				"2019b.danielle.giladi", "emailM@email.com", "2019b.danielle.giladi", elementId);
		// THEN the database contains the same element with name tests

		assertThat(this.elements.readById("2019b.danielle.giladi#" + elementId).get().getName().equals("tests"));
	}

	@Test
	public void testCreateNewElementWithManager() throws Exception {
		// GIVEN noting
		// WHEN I get ElementBoundary
		ElementBoundary element = new ElementBoundary();
		element.setCreated(new Date());
		element.setElementProperties(new HashMap<>());
		element.setLatlng(new LatLng(2.2, 2.2));
		element.setName("AviorIsAlive");
		element.setExpired(false);
		element.setElementType("Type");
		element.setCreator(new UserKey("email@email.com", "2019b.danielle.giladi"));

		this.rest.postForObject(this.baseUrl + "/{managerSmartspace}/{managerEmail}", element, ElementBoundary.class,
				"2019b.danielle.giladi", "emailM@email.com");
		// THEN I get only 3 elements
		// AND they are similar to the elements
		assertThat(elements.readAll()).hasSize(1);
	}

	@Test
	public void testGetSpeificElementWithManager() throws Exception {
		// GIVEN the database contains player user with name avior
		ElementEntity element = new ElementEntity();
		element.setCreateTimestamp(new Date());
		element.setMoreAttributes(new HashMap<>());
		element.setLocation(new Location(2.2, 2.2));
		element.setName("AviorIsAlive");
		element.setExpired(true);
		element.setType("Type");
		element.setCreateEmail("email@email.com");
		element.setCreatorSmartspace("2019b.danielle.giladi");
		element = elements.create(element);
		String elementId = element.getElementId();

		// WHEN we try to get specific element by MANAGER
		ElementBoundary getElement = this.rest.getForObject(
				this.baseUrl + "/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}", ElementBoundary.class,
				"2019b.danielle.giladi", "emailM@email.com", "2019b.danielle.giladi", elementId);

		// THEN getElement is not null and contains the same elementId we ask
		assertThat(getElement != null && getElement.getKey().getId().contentEquals(elementId));
	}

	@Test(expected = Exception.class)
	public void testGetSpeificElementWithPlayer() throws Exception {
		// GIVEN the database contains player user and element with expired =true
		ElementEntity element = new ElementEntity();
		element.setCreateTimestamp(new Date());
		element.setMoreAttributes(new HashMap<>());
		element.setLocation(new Location(2.2, 2.2));
		element.setName("AviorIsAlive");
		element.setExpired(true);
		element.setType("Type");
		element.setCreateEmail("email@email.com");
		element.setCreatorSmartspace("2019b.danielle.giladi");
		element = elements.create(element);
		String elementId = element.getElementId();

		// WHEN we try to get specific element by PLAYER
		this.rest.getForObject(this.baseUrl + "/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class, "2019b.danielle.giladi", "emailP@email.com", "2019b.danielle.giladi", elementId);

		// THEN the test throw exception
	}

	@Test
	public void testGetAllElementsWithManager() throws Exception {
		// GIVEN the database contains two elements - one with expired = true and the
		// other one with expired=false
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		ElementEntity element2 = new ElementEntity();
		element2.setCreateTimestamp(new Date());
		element2.setMoreAttributes(new HashMap<>());
		element2.setLocation(new Location(2.2, 2.2));
		element2.setName("Avior");
		element2.setExpired(false);
		element2.setType("Type");
		element2.setCreateEmail("email@email.com");
		element2.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element2);

		// WHEN we try to get elements by MANAGER
		ElementBoundary[] getElements = this.rest.getForObject(this.baseUrl + "/{userSmartspace}/{userEmail}",
				ElementBoundary[].class, "2019b.danielle.giladi", "emailM@email.com");

		// THEN getElements has size 2
		assertThat(getElements).hasSize(2);
	}

	@Test
	public void testGetAllElementsWithPlayer() throws Exception {
		// GIVEN the database contains two elements - one with expired = true and the
		// other one with expired=false
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		ElementEntity element2 = new ElementEntity();
		element2.setCreateTimestamp(new Date());
		element2.setMoreAttributes(new HashMap<>());
		element2.setLocation(new Location(2.2, 2.2));
		element2.setName("Avior");
		element2.setExpired(false);
		element2.setType("Type");
		element2.setCreateEmail("email@email.com");
		element2.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element2);

		// WHEN we try to get elements by MANAGER
		ElementBoundary[] getElements = this.rest.getForObject(this.baseUrl + "/{userSmartspace}/{userEmail}",
				ElementBoundary[].class, "2019b.danielle.giladi", "emailP@email.com");

		// THEN getElements has size 1
		assertThat(getElements).hasSize(1);
	}

	@Test
	public void testGetAllElementsByNameWithPlayer() throws Exception {
		// GIVEN the database contains one element with expired = true
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		// WHEN we try to get elements by PLAYER
		ElementBoundary[] getElements = this.rest.getForObject(
				this.baseUrl + "/{userSmartspace}/{userEmail}?search=name&value={name}", ElementBoundary[].class,
				"2019b.danielle.giladi", "emailP@email.com", "Guy");

		// THEN getElements has size 0
		assertThat(getElements).hasSize(0);
	}

	@Test
	public void testGetAllElementsByNameWithManager() throws Exception {
		// GIVEN the database contains one element with expired = true
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		// WHEN we try to get elements by MANAGER
		ElementBoundary[] getElements = this.rest.getForObject(
				this.baseUrl + "/{userSmartspace}/{userEmail}?search=name&value={name}", ElementBoundary[].class,
				"2019b.danielle.giladi", "emailM@email.com", "Guy");

		// THEN getElements has size 1
		assertThat(getElements).hasSize(1);
	}

	@Test
	public void testGetAllElementsByTypeWithManager() throws Exception {
		// GIVEN the database contains two elements one with type= "Type1" and the other
		// one with type="Type2"
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type1");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		ElementEntity element2 = new ElementEntity();
		element2.setCreateTimestamp(new Date());
		element2.setMoreAttributes(new HashMap<>());
		element2.setLocation(new Location(2.2, 2.2));
		element2.setName("Avior");
		element2.setExpired(false);
		element2.setType("Type2");
		element2.setCreateEmail("email@email.com");
		element2.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element2);

		// WHEN we try to get elements by MANAGER
		ElementBoundary[] getElements = this.rest.getForObject(
				this.baseUrl + "/{userSmartspace}/{userEmail}?search=type&value={type}", ElementBoundary[].class,
				"2019b.danielle.giladi", "emailM@email.com", "Type1");

		// THEN getElements has size 1
		assertThat(getElements).hasSize(1);
	}

	@Test
	public void testGetAllElementsBylocationWithManager() throws Exception {
		// GIVEN the database contains two elements one with type= "Type1" and the other
		// one with type="Type2"
		ElementEntity element1 = new ElementEntity();
		element1.setCreateTimestamp(new Date());
		element1.setMoreAttributes(new HashMap<>());
		element1.setLocation(new Location(2.2, 2.2));
		element1.setName("Guy");
		element1.setExpired(true);
		element1.setType("Type1");
		element1.setCreateEmail("email@email.com");
		element1.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element1);

		ElementEntity element2 = new ElementEntity();
		element2.setCreateTimestamp(new Date());
		element2.setMoreAttributes(new HashMap<>());
		element2.setLocation(new Location(5, 5));
		element2.setName("Avior");
		element2.setExpired(false);
		element2.setType("Type2");
		element2.setCreateEmail("email@email.com");
		element2.setCreatorSmartspace("2019b.danielle.giladi");
		elements.create(element2);

		// WHEN we try to get elements by MANAGER
		ElementBoundary[] getElements = this.rest.getForObject(
				this.baseUrl + "/{userSmartspace}/{userEmail}?search=location&x={x}&y={y}&distance={distance}",
				ElementBoundary[].class, "2019b.danielle.giladi", "emailM@email.com", 0, 0, 4);

		// THEN getElements has size 1
		assertThat(getElements).hasSize(1);
	}

}
