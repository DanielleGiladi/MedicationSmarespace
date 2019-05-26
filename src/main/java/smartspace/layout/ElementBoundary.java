package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementBoundary {
	private GenericKey key;
	private UserKey creator;
	private String elementType;
	private String name;
	private Date created;
	private LatLng latlng;
	private boolean expired;
	private Map<String, Object> elementProperties;

	public ElementBoundary() {
		this.key= new GenericKey();
		this.creator = new UserKey();
		this.latlng = new LatLng();
	}

	public ElementBoundary(ElementEntity entity) {
		this.creator = new UserKey(entity.getCreateEmail(), entity.getCreatorSmartspace());
		this.key = new GenericKey(entity.getElementId(),entity.getElementSmartspace());
		this.elementProperties = entity.getMoreAttributes();
		this.elementType = entity.getType();
		this.name= entity.getName();
		this.created= entity.getCreateTimestamp();
		this.expired=entity.isExpired();
		this.latlng = new LatLng(entity.getLocation().getY(),entity.getLocation().getX());
	}


	public GenericKey getKey() {
		return key;
	}

	public void setKey(GenericKey key) {
		this.key = key;
	}

	public UserKey getCreator() {
		return creator;
	}

	public void setCreator(UserKey creator) {
		this.creator = creator;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public LatLng getLatlng() {
		return latlng;
	}

	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public Map<String, Object> getElementProperties() {
		return elementProperties;
	}

	public void setElementProperties(Map<String, Object> elementProperties) {
		this.elementProperties = elementProperties;
	}

	public ElementEntity toEntity() {

		ElementEntity entity = new ElementEntity();
		entity.setCreateEmail(this.creator.getEmail());
		entity.setCreatorSmartspace(this.creator.getSmartspace());
		entity.setCreateTimestamp(this.created);
		entity.setElementId(this.key.getId());
		entity.setElementSmartspace(this.key.getSmartspace());
		entity.setExpired(this.expired);
		entity.setMoreAttributes(this.elementProperties);
		entity.setName(this.name);
		entity.setLocation(new Location(this.latlng.getLng(),this.latlng.getLat()));
		entity.setType(this.elementType);
		
		String keyValue = this.key.getId();
		if (this.key.getSmartspace() != null) {
			entity.setElementSmartspace(this.key.getSmartspace());
		if (keyValue != null) {
			entity.setKey(this.key.getSmartspace() + "#" + this.key.getId());
		}
	}
		return entity;
	}

}