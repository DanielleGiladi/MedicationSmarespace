package smartspace.layout;

public class GenericKey {
	private String id;
	private String smartspace;

	public GenericKey(String id, String smartspace) {
		super();
		this.id = id;
		this.smartspace = smartspace;
	}

	public GenericKey() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSmartspace() {
		return smartspace;
	}

	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

}
