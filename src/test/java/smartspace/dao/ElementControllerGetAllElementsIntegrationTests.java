package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
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

import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.Factory;
import smartspace.layout.ElementBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.profiles.active=default")
public class ElementControllerGetAllElementsIntegrationTests {
	private AdvancedUserDao<String> users;

	private int port;

	private String baseUrl;

	private RestTemplate rest;

	private Factory factory;

	private AdvancedElementDao<String> elements;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

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
	public void testGetElementsWithExistingAdmin() throws Exception {
		// GIVEN the database contains 3 elements
		String jane = "Jane"; // Admin!
		List<String> janeKeys = IntStream.range(1, 4)
				.mapToObj(i -> this.factory.createNewElement("name", "type", new Location(), new Date(),
						"creatorEmail" + i, "creatorSmartspace", false, null))
				.map(e -> this.elements.create(e)).map(element -> element.getKey()).collect(Collectors.toList());

		// WHEN I get all elements
		ElementBoundary[] elementsFromRest = this.rest.getForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}",
				ElementBoundary[].class, "2019b.danielle.giladi", jane);

		// THEN I get only 3 elements
		// AND they are similar to the elements
		assertThat(Stream.of(elementsFromRest).map(ElementBoundary::getKey)
				.map(e -> e.getSmartspace() + "#" + e.getId()).map(i -> i.toString()).collect(Collectors.toList()))
						.hasSize(3).containsExactlyInAnyOrderElementsOf(janeKeys);
	}

	@Test(expected = Exception.class)
	public void testGetElementsWithNotExistingAdmin() throws Exception {
		// GIVEN the database contains 3 elements
		String java = "Java"; // Not Admin!
		IntStream.range(1, 4)
				.mapToObj(i -> this.factory.createNewElement("name", "type", new Location(), new Date(),
						"creatorEmail" + i, "creatorSmartspace", false, null))
				.map(e -> this.elements.create(e)).map(element -> element.getKey()).collect(Collectors.toList());

		// WHEN I get all elements
		this.rest.getForObject(this.baseUrl + "/{adminSmartspace}/{adminEmail}",
				ElementBoundary[].class, "2019b.danielle.giladi", java);

		// THEN the rest throws as exception
	}
}
