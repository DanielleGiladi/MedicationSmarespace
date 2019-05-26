package smartspace.layout;

public class UserKey {
	private String email;
	private String smartspace;

	public UserKey(String email, String smartspace) {
		super();
		this.email = email;
		this.smartspace = smartspace;
	}

	public UserKey() {
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	public String getEmail() {
		return email;
	}

	public String getSmartspace() {
		return smartspace;
	}
}
