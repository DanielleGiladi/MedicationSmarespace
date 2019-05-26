package smartspace.layout;

import org.springframework.beans.factory.annotation.Value;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class NewUserForm {

	private String username;
	private String avatar;
	private String role;
	private String email;
	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	public NewUserForm() {
	}

	public NewUserForm(UserEntity entity) {

		this.username = entity.getUsername();
		this.avatar = entity.getAvatar();
		this.email = entity.getUserEmail();
		if (entity.getRole() != null) {
			this.role = entity.getRole().name();
		} else {
			this.role = null;
		}

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

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		entity.setKey(smartspace + "#" + getEmail());
		entity.setAvatar(this.avatar);
		entity.setUsername(this.username);
		entity.setUserEmail(email);
		entity.setPoints(100L);
		if (this.role != null) {
			entity.setRole(UserRole.valueOf(this.role));
		} else {
			entity.setRole(null);
		}
		return entity;
	}

}
