package smartspace.plugins;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedUserDao;
import smartspace.dao.rdb.ElementCrud;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Component
public class CountSpecificMedicationPlugin implements Plugin {
	private ElementCrud elementCrud;
	private AdvancedUserDao<String> userDao;
	private UpdateProperties up;

	@Autowired
	public CountSpecificMedicationPlugin(ElementCrud elementCrud, AdvancedUserDao<String> dao, UpdateProperties up) {
		super();
		this.elementCrud = elementCrud;
		this.userDao = dao;
		this.up = up;

	}

	@Override
	public Object doSomething(ActionEntity action) throws Exception {

		UserEntity user = this.userDao.readById(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
		this.userDao.updatePoints(user, 20L);

		ElementEntity element = this.elementCrud.findById(action.getElementSmartspace() + "#" + action.getElementId())
				.get();
		List<ElementEntity> elementsList = this.elementCrud.findAllByNameAndTypeAndExpired(element.getName(),
				element.getType(), false);

		up.addHistory(element, action.getActionType(), action.getCreationTimestamp().toString());

		return new CountSpecificMedicationResponse(elementsList.size());
	}

}
