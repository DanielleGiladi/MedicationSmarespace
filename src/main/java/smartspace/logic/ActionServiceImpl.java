package smartspace.logic;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.LogMethod;
import smartspace.dao.AdvancedActionDao;
import smartspace.dao.AdvancedElementDao;
import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.plugins.Plugin;
import smartspace.plugins.StringActionResponse;

@Service
public class ActionServiceImpl implements ActionService {
	private AdvancedActionDao actions;
	private AdvancedElementDao<String> elementDao;
	private CheckUserRole userRole;
	private ConfigurableApplicationContext ctx;

	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	@Autowired
	public ActionServiceImpl(AdvancedActionDao actions, CheckUserRole userRole, AdvancedElementDao<String> elementDao,
			ConfigurableApplicationContext ctx) {
		this.actions = actions;
		this.userRole = userRole;
		this.elementDao = elementDao;
		this.ctx = ctx;
	}

	@LogMethod
	@Override
	@Transactional
	public List<ActionEntity> publishNewAction(List<ActionEntity> actions, String adminSmartspace, String adminEmail) {

		if (userRole.isAdmin(adminSmartspace, adminEmail)) {
			for (int i = 0; i < actions.size(); i++) {
				if (validateAction(actions.get(i))) {
					if (actions.get(i).getMoreAttributes().isEmpty()) {
						actions.get(i).getMoreAttributes().put("valid", true);
					}
					actions.set(i, this.actions.createImport(actions.get(i)));
				} else {
					throw new RuntimeException("Illegal action input");
				}
			}
			return actions;
		} else
			throw new RuntimeException();
	}

	@LogMethod
	@Override
	public List<ActionEntity> getActions(int size, int page, String adminSmartspace, String adminEmail) {
		if (userRole.isAdmin(adminSmartspace, adminEmail))
			return this.actions.readAll(size, page, "creationTimestamp");
		else
			throw new RuntimeException();
	}

	private boolean validateAction(ActionEntity actionEntity) {

		return actionEntity != null && actionEntity.getMoreAttributes() != null
				&& elementDao.readById(actionEntity.getElementSmartspace() + "#" + actionEntity.getElementId())
						.isPresent()
				&& !smartspace.equals(actionEntity.getActionSmartspace()) && actionEntity.getElementSmartspace() != null
				&& actionEntity.getElementId() != null && actionEntity.getActionType() != null
				&& actionEntity.getPlayerSmartspace() != null && actionEntity.getPlayerEmail() != null
				&& actionEntity.getCreationTimestamp() != null;

	}

	public boolean isPresentElement(String id, String smartspace) {
		ElementEntity element = elementDao.readById(smartspace + "#" + id).get();
		if (element != null && !element.isExpired())
			return true;
		throw new RuntimeException();
	}

	@LogMethod
	@Override
	public Object invoke(ActionEntity actionEntity) {
		String operation = "";
		Object result = null;
		if (userRole.isPlayer(actionEntity.getPlayerSmartspace(), actionEntity.getPlayerEmail())) {
			if (isPresentElement(actionEntity.getElementId(), actionEntity.getElementSmartspace())) {
				try {
					operation = actionEntity.getActionType();
					if (!operation.trim().isEmpty()) {
						String pluginClassName = "smartspace.plugins." + operation.toUpperCase().charAt(0)
								+ operation.substring(1, operation.length()) + "Plugin";
						Class<?> pluginClass = Class.forName(pluginClassName);
						Plugin actionPlugin = (Plugin) ctx.getBean(pluginClass);
						actionEntity.setCreationTimestamp(new Date());

						result = actionPlugin.doSomething(actionEntity);
						actionEntity.getMoreAttributes().put("result", result);
						String response = null;

						if (result.getClass().equals(StringActionResponse.class)) {
							StringActionResponse newResult = (StringActionResponse) result;
							response = newResult.getAction();
							if (!response.equals("Unable do the action !")) {
								actionEntity = actions.create(actionEntity);
							}
						} else
							actionEntity = actions.create(actionEntity);

					}

				} catch (Exception e) {
					throw new RuntimeException();
				}
			}
			// return new ActionBoundary(actionEntity);
			return result;

		} else
			throw new RuntimeException();

	}
}
