package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import smartspace.dao.rdb.MapAttributesConverter;

@Entity
@Table(name = "ACTIONS")
public class ActionEntity implements SmartspaceEntity<String> {
	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String actionType;
	private String playerSmartspace;
	private String playerEmail;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;

	public ActionEntity() {
	}

	public ActionEntity(String actionSmartspace, /* String actionId, */ String elementSmartspace, String elementId,
			String actionType, String playerSmartspace, String playerEmail, Date creationTimestamp,
			Map<String, Object> moreAttributes) {
		this.actionSmartspace = actionSmartspace;
		this.elementSmartspace = elementSmartspace;
		this.elementId = elementId;
		this.actionType = actionType;
		this.playerSmartspace = playerSmartspace;
		this.playerEmail = playerEmail;
		this.creationTimestamp = creationTimestamp;
		this.moreAttributes = moreAttributes;
	}

	@Transient
	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	@Transient
	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Lob
	@Convert(converter = MapAttributesConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("#");
		this.actionSmartspace = parts[0];
		this.actionId = parts[1];
	}

	@Override
	@Id
	@Column(name = "ACTION_KEY")
	public String getKey() {
		return this.actionSmartspace + "#" + this.actionId;
	}

}