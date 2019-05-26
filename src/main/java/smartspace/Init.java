package smartspace;

import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedActionDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
@Profile("production")
public class Init implements CommandLineRunner {

	private AdvancedUserDao<String> userDao;
	private AdvancedElementDao<String> elementDao;
	private AdvancedActionDao actionDao;

	@Autowired
	public Init(AdvancedUserDao<String> userDao, AdvancedElementDao<String> elementDao, AdvancedActionDao actionDao) {
		this.userDao = userDao;
		this.elementDao = elementDao;
		this.actionDao = actionDao;
	}

	@Override
	public void run(String... args) throws Exception {
		userDao.deleteAll();
		elementDao.deleteAll();
		actionDao.deleteAll();
		UserEntity adminUser = new UserEntity("emailA@email.com", "smartspace", "admin", ":-)", UserRole.ADMIN, 1L);
		userDao.create(adminUser);
		UserEntity managerUser = new UserEntity("emailM@email.com", "smartspace", "admin", ":-)", UserRole.MANAGER, 1L);
		userDao.create(managerUser);
		UserEntity manager2User = new UserEntity("emailM2@email.com", "smartspace", "admin", ":-)", UserRole.PLAYER, 1L);
		userDao.create(manager2User);
		UserEntity playerUser = new UserEntity("emailP@email.com", "smartspace", "play", ":-)", UserRole.PLAYER, 1L);
		userDao.create(playerUser);
		ElementEntity elementEntity = new ElementEntity("smartSpace", new Location(5, 5), "avior", "type1", new Date(),
				false, "creator", "email", new HashMap<>());
		elementDao.create(elementEntity);
		ElementEntity elementEntity1 = new ElementEntity("smartSpace", new Location(5, 5), "aba", "type2", new Date(),
				true, "creator", "email", new HashMap<>());
		elementDao.create(elementEntity1);
	}

}
