package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
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
import smartspace.data.util.Factory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class AdvancedElementDaoTests {
	private Factory factory;
	private AdvancedElementDao<String> dao;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setDao(AdvancedElementDao<String> dao) {
		this.dao = dao;
	}

	@After
	public void teardown() {
		this.dao.deleteAll();
	}

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN there are exactly 7 elements
		IntStream.range(1, 8).mapToObj(idString -> this.factory.createNewElement("name", "type", new Location(),
				new Date(), "creatorEmail", "creatorSmartspace", false, null)).forEach(this.dao::create);

		// WHEN I getAll results of second page of 5 results max
		List<ElementEntity> actual = this.dao.readAll(5, 1);

		// THEN I get just 2 results
		assertThat(actual).hasSize(2);
	}

//	@Test
//	public void testReadAllBySeverityWithPagination() throws Exception{
//		// GIVEN there are exactly 70 messages and only 7 of them are ERROR 
//		IntStream.range(1, 8)
//			.mapToObj(i->this.factory.createNewMessage(
//					"message #" + i, null, new Date(), SeverityEnum.ERROR, new HashMap<>()))
//			.forEach(this.dao::create);
//
//		
//		IntStream.range(8, 71)
//			.mapToObj(i->this.factory.createNewMessage(
//				"message #" + i, null, new Date(), SeverityEnum.INFO, new HashMap<>()))
//			.forEach(this.dao::create);
//
//		// WHEN I getBySeverity list of results of second page of 5 results max
//		List<MessageEntity> actual = this.dao.readBySeverity(SeverityEnum.ERROR, 5, 1);
//		
//		// THEN I get just 2 results
//		assertThat(actual)
//			.hasSize(2);
//	}

//	@Test
//	public void testReadAllBySeverityWithPaginationAndNoValidResult() throws Exception{
//		// GIVEN there are exactly 70 messages with INFO severity 
//		IntStream.range(1, 71)
//			.mapToObj(i->this.factory.createNewMessage(
//				"message #" + i, null, new Date(), SeverityEnum.INFO, new HashMap<>()))
//			.forEach(this.dao::create);
//
//		// WHEN I getBySeverity list of results of second page of 5 results max
//		List<MessageEntity> actual = this.dao.readBySeverity(SeverityEnum.ERROR, 5, 0);
//		
//		// THEN I get just 2 results
//		assertThat(actual)
//			.isEmpty();
//	}
//
//	@Test
//	public void testReadAllBySeverityAndSortedByTextWithPagination() throws Exception{
//		// GIVEN there are exactly 70 messages and only 7 of them are ERROR
//		List<MessageEntity> orderdExpectedResults = 
//			IntStream.range(1, 8)
//			.mapToObj(i->this.factory.createNewMessage(
//					"message #" + (900-i), null, new Date(), SeverityEnum.ERROR, new HashMap<>()))
//			.map(this.dao::create)
//			.collect(Collectors.toList());
//		
//		orderdExpectedResults.sort((m1, m2)->m1.getMessageText().compareTo(m2.getMessageText()));
//		
//		IntStream.range(8, 71)
//			.mapToObj(i->this.factory.createNewMessage(
//				"message #" + (900 - i), null, new Date(), SeverityEnum.INFO, new HashMap<>()))
//			.forEach(this.dao::create);
//
//		// WHEN I getBySeverity list of results of second page of 5 results max
//		List<MessageEntity> actual = this.dao
//				.readBySeveritySortedBy(SeverityEnum.ERROR, 100, 0, "messageText");
//		
//		actual
//			.stream()
//			.forEach(System.err::println);
//		System.err.println("expected:");
//		orderdExpectedResults
//			.stream()
//			.forEach(System.err::println);
//		
//		// THEN I get exactly 7 results ordered by messageText
//		assertThat(actual)
//			.usingElementComparatorOnFields("key")
//			.containsExactlyElementsOf(orderdExpectedResults);
//	}

	@Test
	public void testReadAllByTimestampRangeWithValidResult() throws Exception {
		// GIVEN there are exactly 70 elements
		IntStream.range(1, 71).mapToObj(idString -> this.factory.createNewElement("name", "type", new Location(),
				new Date(), "creatorEmail", "creatorSmartspace", false, null)).forEach(this.dao::create);

		// WHEN I readByTimestamps list of results of today
		List<ElementEntity> actual = this.dao.readByTimestamps(new Date(System.currentTimeMillis() - 3600000L),
				new Date(System.currentTimeMillis() + 3600000L), 100, 0, "createTimestamp");

		// THEN I get exactly 70 results
		assertThat(actual).hasSize(70);
	}

	@Test
	public void testReadAllByTimestampRangeWithInValidResult() throws Exception {
		// GIVEN there are exactly 70 elements
		IntStream.range(1, 71).mapToObj(idString -> this.factory.createNewElement("name", "type", new Location(),
				new Date(), "creatorEmail", "creatorSmartspace", false, null)).forEach(this.dao::create);

		// WHEN I readByTimestamps list of results of today
		List<ElementEntity> actual = this.dao.readByTimestamps(new Date(System.currentTimeMillis() + 3600000L),
				new Date(System.currentTimeMillis() + 2 * 3600000L), 100, 0, "createTimestamp");

		// THEN I get no results
		assertThat(actual).isEmpty();
	}

}
