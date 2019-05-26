package smartspace.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.Factory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = { "spring.profiles.active=default" })
public class AdvancedUserDaoTests {
	private Factory factory;
	private AdvancedUserDao<String> dao;

	@Autowired
	public void setFactory(Factory factory) {
		this.factory = factory;
	}

	@Autowired
	public void setDao(AdvancedUserDao<String> dao) {
		this.dao = dao;
	}

	@After
	public void teardown() {
		this.dao.deleteAll();
	}

	@Test
	public void testReadAllWithPagination() throws Exception {
		// GIVEN there are exactly 7 users
		IntStream.range(1, 8).mapToObj(idString -> this.factory.createNewUser(idString + "", "userSmartspace",
				"username", "avatar", UserRole.MANAGER, 1L)).forEach(this.dao::create);

		// WHEN I getAll results of second page of 5 results max
		List<UserEntity> actual = this.dao.readAll(5, 1);

		// THEN I get just 2 results
		assertThat(actual).hasSize(2);
	}

}
