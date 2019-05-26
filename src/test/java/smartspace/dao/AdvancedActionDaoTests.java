package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.ActionEntity;
import smartspace.data.util.Factory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class AdvancedActionDaoTests {
	private Factory factory;
	private AdvancedActionDao dao;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setDao(AdvancedActionDao dao) {
		this.dao = dao;
	}

	@Before
	@After
	public void teardown() {
		this.dao.deleteAll();
	}
	
	

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN there are exactly 7 actions
		IntStream.range(1, 8).mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace", "actionType",
				new Date(), "playerEmail", "playerSmartspace", null)).forEach(this.dao::create);

		// WHEN I getAll results of second page of 5 results max
		List<ActionEntity> actual = this.dao.readAll(5, 1);

		// THEN I get just 2 results
		assertThat(actual).hasSize(2);
	}

	@Test
	public void testReadAllByTimestampRangeWithValidResult() throws Exception {
		// GIVEN there are exactly 70 elements
		IntStream.range(1, 71).mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace",
				"actionType", new Date(), "playerEmail", "playerSmartspace", null)).forEach(this.dao::create);

		// WHEN I readByTimestamps list of results of today
		List<ActionEntity> actual = this.dao.readByTimestamps(new Date(System.currentTimeMillis() - 3600000L),
				new Date(System.currentTimeMillis() + 3600000L), 100, 0, "creationTimestamp");

		// THEN I get exactly 70 results
		assertThat(actual).hasSize(70);
	}

	@Test
	public void testReadAllByTimestampRangeWithInValidResult() throws Exception {
		// GIVEN there are exactly 70 elements
		IntStream.range(1, 71).mapToObj(i -> this.factory.createNewAction("elementId", "elementSmartspace",
				"actionType", new Date(), "playerEmail", "playerSmartspace", null)).forEach(this.dao::create);

		// WHEN I readByTimestamps list of results of today
		List<ActionEntity> actual = this.dao.readByTimestamps(new Date(System.currentTimeMillis() + 3600000L),
				new Date(System.currentTimeMillis() + 2 * 3600000L), 100, 0, "creationTimestamp");

		// THEN I get no results
		assertThat(actual).isEmpty();
	}

}
