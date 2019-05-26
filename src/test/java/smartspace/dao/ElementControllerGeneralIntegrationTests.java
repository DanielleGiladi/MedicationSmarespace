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

import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.Factory;
import smartspace.layout.ElementBoundary;
import smartspace.layout.GenericKey;
import smartspace.layout.LatLng;
import smartspace.layout.UserKey;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ElementControllerGeneralIntegrationTests {
	private int port;

	private Factory factory;

	private AdvancedUserDao<String> users;

	private String baseUrl;

	private RestTemplate rest;

	private AdvancedElementDao<String> elements;

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
	public void setUsers(AdvancedUserDao<String> users) {
		this.users = users;
	}

	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/admin/elements";
		this.rest = new RestTemplate();
		UserEntity adminUser = new UserEntity("Jane", "Jane", "admin", ":-)", UserRole.ADMIN, 1L);
		users.create(adminUser);
	}

	@After
	public void teardown() {
		this.elements.deleteAll();
		this.users.deleteAll();
	}

	@Test
	public void testPublish() throws Exception {
		// GIVEN the database is clean
		String jane = "Jane";
		// WHEN I post a new element
		ElementBoundary[] newElements = new ElementBoundary[1];
		ElementBoundary element = new ElementBoundary();
		element.setKey(new GenericKey("5", "smartspace"));
		element.setElementType("type");
		element.setElementProperties(new HashMap<>());
		element.setCreator(new UserKey("email", "smartGuy"));
		element.setLatlng(new LatLng(7, 7));
		element.setName("Avior");
		element.setCreated(new Date());
		newElements[0] = element;
		ElementBoundary[] actual = this.rest.postForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}",
				newElements, ElementBoundary[].class, "2019b.danielle.giladi", jane);

		// THEN the database contains a single new element
		// AND the element in the database is similar to the element posted
		assertThat(this.elements.readAll()).hasSize(1).usingElementComparatorOnFields("key")
				.containsExactly(actual[0].toEntity());
	}

	@Test
	public void testGetElementsWithPagination() throws Exception {
		// GIVEN the database contains 10 elements
		String jane = "Jane";
		int inputSize = 10;
		IntStream
				.range(1, inputSize + 1).mapToObj(idString -> this.factory.createNewElement("name", "type",
						new Location(), new Date(), "creatorEmail", "creatorSmartspace", false, null))
				.forEach(this.elements::create);

		// WHEN I get all elements using page size of 3 and page 1
		int page = 1;
		int size = 3;
		ElementBoundary[] actual = this.rest.getForObject(
				this.baseUrl + "/{adminSmartspace}/{adminEmail}?page={page}&size={size}", ElementBoundary[].class,
				"2019b.danielle.giladi", jane, page, size);

		// THEN I get 3 messages
		int expectedSize = 3;
		assertThat(actual).hasSize(expectedSize);

	}
}
