package smartspace.dao.memory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.Before;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

// Unit tests are tests that validates specific function/class/operation
public class RamElementDaoUnitTests {
	private RamElementDao dao;

	// Before and after for coming up possible tests
	@Before
	public void setup() {
		dao = new RamElementDao();
	}

	@After
	public void teardown() {
		dao.deleteAll();
	}

	// @Test
	public void unitTestCreateSingleElementAndValidateContent() throws Exception {
		// Given I created a RamelementDao

		// When we call create method with the new element
		ElementEntity element = new ElementEntity("Testimg", new Location(10, 10), "Akamol", "Medication", new Date(),
				false, "DaniSmartSpace", "Dani@Rega.com", null);
		element = dao.create(element);

		// AND it contains the element
		assertThat(dao.readAll()).usingElementComparator((m1, m2) -> m1.getType().compareTo(m2.getType()))
				.contains(new ElementEntity("Testing", new Location(10, 10), "Fasl", "Medication", new Date(), false,
						"DaniSmartSpace", "Dani@Rega.com", null));

	}

	// @Test
	public void unitTestCreateSingleElementAndValidateKey() throws Exception {
		// Given I created a RamElementDao

		// When we call create method with the element
		ElementEntity element = new ElementEntity("Testimg", new Location(10, 10), "Akamol", "Medication", new Date(),
				false, "DaniSmartSpace", "Dani@Rega.com", null);
		element = dao.create(element);

		// AND the returned element has a non null ID
		assertThat(element.getKey()).isNotNull();
	}

//
	// @Test(expected=Exception.class)
	public void unitTestCreateNullElement() throws Exception {
		// Given I created a RamelementDao

		// When we call create method with null element
		dao.create(null);

		// Then the method throws exception
	}

//
	// @Test
	public void unitTestCreateSingleElementAndValidateTotalSize() throws Exception {
		// Given I created a RamelementDao

		// When we call create method with the new Element
		ElementEntity element = new ElementEntity("Testimg", new Location(10, 10), "Akamol", "Medication", new Date(),
				false, "DaniSmartSpace", "Dani@Rega.com", null);
		element = dao.create(element);

		// Then the internal list length is 1
		assertThat(dao.readAll()).hasSize(1);
	}

	// @Test
	public void unitTestCreateSingleElementAndDeleteAndValidateTotalSize() throws Exception {
		// Given I created a RamElementDao
		RamElementDao dao = new RamElementDao();

		// When we call create method with the ElementEntity
		ElementEntity element = new ElementEntity("Testimg", new Location(10, 10), "Akamol", "Medication", new Date(),
				false, "DaniSmartSpace", "Dani@Rega.com", null);

		element = dao.create(element);

		// Then Delete all list objects and the internal list length is 0

		dao.deleteAll();
		assertThat(dao.readAll()).hasSize(0);
	}

}
