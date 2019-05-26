package smartspace.logic;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	public List<ActionEntity> publishNewAction(List<ActionEntity> actionEntity, String adminSmartspace,
			String adminEmail);

	public List<ActionEntity> getActions(int size, int page, String adminSmartspace, String adminEmail);

	public Object invoke(ActionEntity actionEntity);
}
