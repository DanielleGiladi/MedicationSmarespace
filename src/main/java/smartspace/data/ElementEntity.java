package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import smartspace.dao.rdb.MapAttributesConverter;

@Entity
@Table(name = "ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {
	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace;
	private String createEmail;
	private Map<String, Object> moreAttributes;

	public ElementEntity() {
	}

	public ElementEntity(String elementSmartspace, Location location, String name, String type, Date creationTimeStamp,
			boolean expired, String creatorSmartspace, String createEmail, Map<String, Object> moreAttributes) {
		super();
		this.elementSmartspace = elementSmartspace;
		this.location = location;
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationTimeStamp;
		this.expired = expired;
		this.creatorSmartspace = creatorSmartspace;
		this.createEmail = createEmail;
		this.moreAttributes = moreAttributes;
	}

	@Override
	@Id
	@Column(name = "ELEMENT_KEY")
	public String getKey() {
		return this.elementSmartspace + "#" + this.elementId;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("#");
		this.elementSmartspace = parts[0];
		this.elementId = parts[1];
	}

	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	@Transient
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateTimestamp() {
		return creationTimestamp;
	}

	public void setCreateTimestamp(Date createTimestamp) {
		this.creationTimestamp = createTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreateEmail() {
		return createEmail;
	}

	public void setCreateEmail(String createEmail) {
		this.createEmail = createEmail;
	}

	@Lob
	@Convert(converter = MapAttributesConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}
}
