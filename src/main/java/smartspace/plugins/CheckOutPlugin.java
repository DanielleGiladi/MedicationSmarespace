package smartspace.plugins;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.dao.AdvancedUserDao;
import smartspace.dao.rdb.ActionCrud;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;

@Component
public class CheckOutPlugin implements Plugin {
	private AdvancedUserDao<String> userDao;
	private ActionCrud actionCrud;
	private AdvancedElementDao<String> elementDao;
	private UpdateProperties up;

	@Autowired
	public CheckOutPlugin(AdvancedUserDao<String> userDao, ActionCrud actionCrud, AdvancedElementDao<String> elementDao,
			UpdateProperties up) {
		super();
		this.userDao = userDao;
		this.actionCrud = actionCrud;
		this.elementDao = elementDao;
		this.up = up;
	}

	@Override
	public Object doSomething(ActionEntity action) throws Exception {
		List<ActionEntity> checkOutList = this.actionCrud
				.findAllByActionTypeAndElementSmartspaceAndElementIdAndPlayerSmartspaceAndPlayerEmail(
						action.getActionType(), action.getElementSmartspace(), action.getElementId(),
						action.getPlayerSmartspace(), action.getPlayerEmail());

		List<ActionEntity> checkInList = this.actionCrud
				.findAllByActionTypeAndElementSmartspaceAndElementIdAndPlayerSmartspaceAndPlayerEmail("CheckIn",
						action.getElementSmartspace(), action.getElementId(), action.getPlayerSmartspace(),
						action.getPlayerEmail());

		if (!checkInList.isEmpty() && ((checkInList.size() - 1) == checkOutList.size())) {
			UserEntity user = this.userDao.readById(action.getPlayerSmartspace() + "#" + action.getPlayerEmail()).get();
			this.userDao.updatePoints(user, -35L);
			ElementEntity element = this.elementDao
					.readById(action.getElementSmartspace() + "#" + action.getElementId()).get();

			up.addHistory(element, action.getActionType(), action.getCreationTimestamp().toString());

			return new StringActionResponse("Checked Out !");
		} else
			// throw new ElementNotFoundException("Already Checked In !");
			return new StringActionResponse("Unable do the action !");

	}

}
