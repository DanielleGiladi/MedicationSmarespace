package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.Before;

import smartspace.data.ActionEntity;

// Unit tests are tests that validates specific function/class/operation
public class RamActionDaoUnitTests {
	private RamActionDao dao;

	// Before and after for coming up possible tests
	@Before
	public void setup() {
		dao = new RamActionDao();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	// @Test
	public void unitTestCreateSingleActionAndValidateContent() throws Exception {
		// Given I created a RamelementDao

		// When we call create method with the new element
		ActionEntity element = new ActionEntity("Test", /* "Test", */ "Test", "Test", "Test", "Test", "Test",
				new Date(), null);
		element = dao.create(element);

		// AND it contains the element
		assertThat(dao.readAll()).usingElementComparator((m1, m2) -> m1.getActionType().compareTo(m2.getActionType()))
				.contains(new ActionEntity("Test", /* "Test", */ "Test", "Test", "Test", "Test", "Test", new Date(),
						null));

	}

	// @Test
	public void unitTestCreateSingleActionAndValidateKey() throws Exception {
		// Given I created a RamActionDao

		// When we call create method with the action
		ActionEntity action = new ActionEntity("Test", /* "Test", */ "Test", "Test", "Test", "Test", "Test", new Date(),
				null);
		action = dao.create(action);

		// AND the returned action has a non null ID
		assertThat(action.getKey()).isNotNull();
	}

//
	// @Test(expected=Exception.class)
	public void unitTestCreateNullAction() throws Exception {
		// Given I created a RamActionDao

		// When we call create method with null action
		dao.create(null);

		// Then the method throws exception
	}

	// @Test
	public void unitTestCreateSingleActionAndValidateTotalSize() throws Exception {
		// Given I created a RamActionDao

		// When we call create method with the new action
		ActionEntity action = new ActionEntity("Test", /* "Test", */ "Test", "Test", "Test", "Test", "Test", new Date(),
				null);

		action = dao.create(action);

		// Then the internal list length is 1
		assertThat(dao.readAll()).hasSize(1);
	}

	// @Test
	public void unitTestCreateSingleActionAndDeleteAndValidateTotalSize() throws Exception {
		// Given I created a RamActionDao

		// When we call create method with the ActionEntity
		ActionEntity action = new ActionEntity("Test", /* "Test", */ "Test", "Test", "Test", "Test", "Test", new Date(),
				null);

		action = dao.create(action);

		// Then Delete all list objects and the internal list length is 0

		dao.deleteAll();
		assertThat(dao.readAll()).hasSize(0);
	}

}
