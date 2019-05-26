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

import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class MoreActionDaoIntegrationTests {

	private ActionDao actionDao;
	private EntityFactory factory;

	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setActionDao(ActionDao actionDao) {
		this.actionDao = actionDao;
	}

	// cleanup Action Dao after each test
	@After
	public void teardown() {
		this.actionDao.deleteAll();
	}

	@Test
	public void testCreateAndDeleteAll() {
		// GIVEN nothing

		// WHEN I create 3 Actions
		// AND I delete all Actions

		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace", "actionType", new Date(),
						"playerEmail", "playerSmartspace", null))
				.map(this.actionDao::create) // Stream of ActionEntity
				.forEach(action -> {
				});

		this.actionDao.deleteAll();

		// THEN the dao contains no Actions
		assertThat(this.actionDao.readAll()).isEmpty();
	}

	@Test
	public void testCreateActionsAndCheckCreation() {
		// GIVEN nothing

		// WHEN I create 3 Actions
		// AND I delete all Actions

		IntStream.range(1, 4)
				// .mapToObj(id->""+id) // Stream of String
				.mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace", "actionType", new Date(),
						"playerEmail", "playerSmartspace", null))
				.map(this.actionDao::create) // Stream of ActionEntity
				.forEach(action -> {
				});

		// THEN the Dao contains no 3 Actions
		assertThat(this.actionDao.readAll()).hasSize(3);
	}

}
