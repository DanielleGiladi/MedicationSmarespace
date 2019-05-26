package smartspace.plugins;

import java.util.Date;

public class UserCheckAction {
	private String email;
	private String smartspace;
	private Date timeStamp;

	public UserCheckAction(String email, String smartspace, Date timeStamp) {
		super();
		this.email = email;
		this.smartspace = smartspace;
		this.timeStamp = timeStamp;
	}

	public UserCheckAction() {
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
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
