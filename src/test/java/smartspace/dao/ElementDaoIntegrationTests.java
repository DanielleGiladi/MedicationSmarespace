//Last version of tests

package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.IntStream;

import org.junit.After;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"spring.profiles.active=default"})
public class ElementDaoIntegrationTests {
	private ElementDao<String> ElementDao;
	private EntityFactory factory;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setElementDao(ElementDao<String> ElementDao) {
		this.ElementDao = ElementDao;
	}

	// cleanup Element dao after each test
	@After
	public void teardown() {
		this.ElementDao.deleteAll();
	}

	// @Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Elements
		// AND I delete all Elements
		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(idString -> this.factory.createNewElement("name", "type", new Location(), new Date(),
						"creatorEmail", "creatorSmartspace", false, null))
				.map(this.ElementDao::create) // Stream of ElementEntity
				.forEach(element -> {
				});
		this.ElementDao.deleteAll();

		// THEN the dao contains no Elements
		assertThat(this.ElementDao.readAll()).isEmpty();
	}

//	
	// @Test(expected=Exception.class)
	public void testUpdateAfterDeletion() throws Exception {
		// GIVEN nothing

		// WHEN I create a Test Element
		// AND I delete all Elements
		// AND I update the new Element
		ElementEntity Element = this.factory.createNewElement("name", "type", new Location(), new Date(),
				"creatorEmail", "creatorSmartspace", false, null);
		this.ElementDao.deleteAll();

		ElementEntity update = new ElementEntity();
		update.setKey(Element.getKey());
		update.setCreateEmail("New Test");

		this.ElementDao.update(update);

		// THEN the dao throws as exception
	}

}
