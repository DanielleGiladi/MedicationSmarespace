package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smartspace.data.ElementEntity;
import smartspace.dao.AdvancedUserDao;
import smartspace.dao.rdb.ElementCrud;
import smartspace.data.ActionEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;

@Component
public class SearchMedicationPlugin implements Plugin {
	private ElementCrud elementCrud;
	private AdvancedUserDao<String> dao;
	private UpdateProperties up;

	@Autowired
	public SearchMedicationPlugin(ElementCrud elementCrud, AdvancedUserDao<String> dao, UpdateProperties up) {
		super();
		this.elementCrud = elementCrud;
		this.dao = dao;
		this.up = up;
	}

	@Override
	public Object doSomething(ActionEntity action) throws Exception {
		UserEntity user = this.dao.readById(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
		this.dao.updatePoints(user, 10L);

		ElementEntity element = this.elementCrud.findById(action.getElementSmartspace() + "#" + action.getElementId())
				.get();
		up.addHistory(element, action.getActionType(), action.getCreationTimestamp().toString());
		Location location = element.getLocation();
		return new SearchMedicationResponse(location);
	}

}
