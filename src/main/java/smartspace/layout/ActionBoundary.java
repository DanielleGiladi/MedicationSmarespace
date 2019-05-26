package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {

	private GenericKey actionKey;
	private GenericKey element;
	private String type;
	private UserKey player;
	private Date created;
	private Map<String, Object> properties;

	public ActionBoundary() {
		this.actionKey = new GenericKey();
		this.element = new GenericKey();
		this.player = new UserKey();
	}

	public ActionBoundary(ActionEntity entity) {
		this.type = entity.getActionType();
		this.created = entity.getCreationTimestamp();
		this.properties = entity.getMoreAttributes();
		this.actionKey = new GenericKey(entity.getActionId(), entity.getActionSmartspace());
		this.element = new GenericKey(entity.getElementId(), entity.getElementSmartspace());
		this.player = new UserKey(entity.getPlayerEmail(), entity.getPlayerSmartspace());

	}

	public GenericKey getActionKey() {
		return actionKey;
	}

	public void setActionKey(GenericKey actionKey) {
		this.actionKey = actionKey;
	}

	public GenericKey getElement() {
		return element;
	}

	public void setElement(GenericKey element) {
		this.element = element;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UserKey getPlayer() {
		return player;
	}

	public void setPlayer(UserKey player) {
		this.player = player;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public ActionEntity toEntity() {
		ActionEntity entity = new ActionEntity();
		// NOTE: in this demo application, entity default key is null
		String keyValue = this.actionKey.getId();
		if (this.actionKey.getSmartspace() != null) {
			entity.setActionSmartspace(this.actionKey.getSmartspace());
			if (keyValue != null) {
				entity.setKey(this.actionKey.getSmartspace() + "#" + this.actionKey.getId());
			}
		}
		entity.setActionType(this.type);
		entity.setCreationTimestamp(this.getCreated());
		entity.setElementId(this.element.getId());
		entity.setElementSmartspace(this.element.getSmartspace());
		entity.setMoreAttributes(this.properties);
		entity.setPlayerEmail(this.getPlayer().getEmail());
		entity.setPlayerSmartspace(this.getPlayer().getSmartspace());

		return entity;
	}

}
