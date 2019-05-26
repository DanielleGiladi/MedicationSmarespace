package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;

@Component
public class EchoPlugin implements Plugin {
	private AdvancedElementDao<String> elementDao;
	private UpdateProperties up;

	@Autowired
	public EchoPlugin(AdvancedElementDao<String> elementDao, UpdateProperties up) {
		this.elementDao = elementDao;
		this.up = up;
	}

	@Override
	public Object doSomething(ActionEntity action) throws Exception {
		ElementEntity element = this.elementDao.readById(action.getElementSmartspace() + "#" + action.getElementId())
				.get();
		up.addHistory(element, action.getActionType(), action.getCreationTimestamp().toString());
		return new StringActionResponse("Echo");
	}

}
