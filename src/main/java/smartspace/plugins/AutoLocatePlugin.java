package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.dao.rdb.ElementCrud;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.logic.MedicationCategory;

@Component
public class AutoLocatePlugin implements Plugin {

	private ElementCrud elementCrud;
	private AdvancedUserDao<String> userDao;
	private AdvancedElementDao<String> elementDao;
	private UpdateProperties up;

	@Autowired
	public AutoLocatePlugin(ElementCrud elementCrud, AdvancedUserDao<String> dao,
			AdvancedElementDao<String> elementDao , UpdateProperties up) {
		super();
		this.elementCrud = elementCrud;
		this.userDao = dao;
		this.elementDao = elementDao;
		this.up=up;
	}

	@Override
	public Object doSomething(ActionEntity action) throws Exception {
		UserEntity user = this.userDao.readById(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
		this.userDao.updatePoints(user, 15L);

		ElementEntity element = this.elementCrud.findById(action.getElementSmartspace() + "#" + action.getElementId())
				.get();
		element = autoLocate(element);
		this.elementDao.update(element);
		
		up.addHistory(element , action.getActionType(), action.getCreationTimestamp().toString());

		Location location = element.getLocation();
		return new SearchMedicationResponse(location);
	}

	public ElementEntity autoLocate(ElementEntity element) {
		String type = element.getType();
		type = type.toUpperCase();
		String name = element.getName();
		char letter = name.charAt(0);
		double firstLetter;
		double category;
		if (letter <= 'z' && letter >= 'a')
			firstLetter = letter - 96;
		else
			firstLetter = letter - 64;

		switch (type) {
		case "ANTIBIOTIC": {
			category = MedicationCategory.ANTIBIOTIC.ordinal() + 1;
			break;
		}
		case "PAINKILLER": {
			category = MedicationCategory.PAINKILLER.ordinal() + 1;
			break;
		}

		default: {
			category = MedicationCategory.GENERAL.ordinal() + 1;
			break;
		}
		}

		if (!element.isExpired())
			element.setLocation(new Location(firstLetter, category));
		else
			element.setLocation(new Location(-firstLetter, -category));

		return element;

	}

}
