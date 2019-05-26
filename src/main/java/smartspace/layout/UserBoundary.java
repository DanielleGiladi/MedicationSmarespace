package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserBoundary {

	private UserKey key;
	private String username;
	private String avatar;
	private String role;
	private Long points;

	public UserBoundary() {
		this.key = new UserKey();
	}

	public UserBoundary(UserEntity entity) {

		this.key = new UserKey(entity.getUserEmail(), entity.getUserSmartspace());
		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
		this.points = entity.getPoints();

		if (entity.getRole() != null) {
			this.role = entity.getRole().name();
		} else {
			this.role = null;
		}

	}

	public UserKey getKey() {
		return key;
	}

	public void setKey(UserKey key) {
		this.key = key;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		// NOTE: in this demo application, entity default key is null

		if (this.key != null) {
			entity.setUserEmail(this.key.getEmail());
			entity.setUserSmartspace(this.key.getSmartspace());
		}

		entity.setAvatar(this.avatar);
		entity.setPoints(this.points);
		entity.setUsername(this.username);

		if (this.role != null) {
			entity.setRole(UserRole.valueOf(this.role));
		} else {
			entity.setRole(null);
		}

		return entity;
	}

}