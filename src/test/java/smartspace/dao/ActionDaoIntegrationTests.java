//Last version of tests

package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.IntStream;

import org.junit.After;

import org.springframework.beans.factory.annotation.Autowired;

import smartspace.dao.rdb.RdbActionDao;

import smartspace.data.util.EntityFactory;

//@SpringBootTest
//@RunWith(SpringRunner.class)
//@TestPropertySource(properties = {"spring.profiles.active=default"})
public class ActionDaoIntegrationTests {
	private ActionDao actionDao;
	private EntityFactory factory;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setActionDao(RdbActionDao actionDao) {
		this.actionDao = actionDao;
	}

	// cleanup Element dao after each test
	@After
	public void teardown() {
		this.actionDao.deleteAll();
	}

	// @Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Elements
		// AND I delete all Elements
		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace", "actionType", new Date(),
						"playerEmail", "playerSmartspace", null))
				.map(this.actionDao::create) // Stream of ElementEntity
				.forEach(action -> {
				});

		this.actionDao.deleteAll();

		// THEN the dao contains no Elements
		assertThat(this.actionDao.readAll()).isEmpty();
	}

}
