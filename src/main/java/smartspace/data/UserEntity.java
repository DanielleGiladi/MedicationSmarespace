package smartspace.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "USERS")
public class UserEntity implements SmartspaceEntity<String> {
	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private UserRole role;
	private Long points;

	public UserEntity(String userEmail, String userSmartspace, String username, String avatar, UserRole role, Long i) {
		super();
		this.userSmartspace = userSmartspace;
		this.userEmail = userEmail;
		this.username = username;
		this.avatar = avatar;
		this.role = role;
		this.points = i;
	}

	public UserEntity() {
	}

	@Transient
	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	@Transient
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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

	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	@Override
	@Id
	@Column(name = "USER_KEY")
	public String getKey() {
		return this.userSmartspace + "#" + this.userEmail;
	}

	@Override
	public void setKey(String key) {
		String[] parts = key.split("#");
		this.userSmartspace = parts[0];
		this.userEmail = parts[1];
	}
}
