package smartspace.plugins;

public class GetAllCheckInput {
	private String operation;
	private int size;
	private int page;

	public GetAllCheckInput() {
		this.size = 10;
		this.page = 0;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
