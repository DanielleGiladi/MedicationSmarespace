package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MoreElementDaoIntegrationTest {
	private ElementDao<String> elementDao;
	private EntityFactory factory;

	@Autowired
	public void setElementDao(ElementDao<String> ElementDao) {
		this.elementDao = ElementDao;
	}

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	// cleanup Element Dao after each test
	@After
	public void teardown() {
		this.elementDao.deleteAll();
	}

	@Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Elements
		// AND I delete all Elements
		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(idString -> this.factory.createNewElement("name", "type", new Location(), new Date(),
						"creatorEmail", "creatorSmartspace", false, null))
				// Stream of ElementEntity
				.map(this.elementDao::create) // Stream of ElementEntity
				.forEach(element -> {
				});
		this.elementDao.deleteAll();

		// THEN the dao contains no Elements
		assertThat(this.elementDao.readAll()).isEmpty();
	}

	@Test(expected = Exception.class)
	public void testUpdateAfterDeletion() throws Exception {
		// GIVEN nothing

		// WHEN I create a Test Element with email "creatorEmail"
		// AND I delete all Elements
		// AND I update the new Element
		ElementEntity element = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.elementDao.create(element);
		this.elementDao.deleteAll();

		ElementEntity update = this.factory.createNewElement("name", "type", new Location(), new Date(), "creatorEmail",
				"creatorSmartspace", false, null);

		update.setKey(element.getKey());
		update.setCreateEmail("New Test");

		this.elementDao.update(update);

		// THEN the dao throws as exception
	}

	@Test
	public void testUpdateElementName() throws Exception {
		// GIVEN a user with name Guy exists
		ElementEntity existing = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		existing.setName("Guy");
		existing = this.elementDao.create(existing);

		// WHEN I update the name to be Avior
		ElementEntity update = this.factory.createNewElement("name", "type", new Location(), new Date(), "creatorEmail",
				"creatorSmartspace", false, null);
		update.setKey(existing.getKey());
		update.setName("Avior");
		this.elementDao.update(update);

		// THEN the database contains the name Avior for this user
		assertThat(this.elementDao.readById(existing.getKey())).isPresent().get().extracting("name")
				.containsExactly("Avior");
	}

	@Test
	public void testCreateAndDeleteOne() {
		// GIVEN nothing

		// WHEN I create 2 Elements
		// AND I delete one Element
		ElementEntity element1 = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.elementDao.create(element1);
		ElementEntity element2 = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.elementDao.create(element2);

		this.elementDao.delete(element2);

		// THEN the dao contains one Element
		assertThat(this.elementDao.readAll()).hasSize(1);
	}

	@Test
	public void testCreateAndDeleteByKey() {
		// GIVEN nothing

		// WHEN I create 2 Elements
		// AND I delete one Element
		ElementEntity element1 = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.elementDao.create(element1);
		ElementEntity element2 = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.elementDao.create(element2);

		this.elementDao.deleteByKey(element2.getKey());

		// THEN the dao contains one Element
		assertThat(this.elementDao.readAll()).hasSize(1);
	}

}
