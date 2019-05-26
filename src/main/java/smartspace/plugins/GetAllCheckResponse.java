package smartspace.plugins;

import java.util.List;

public class GetAllCheckResponse {
	private List<UserCheckAction> result;

	public GetAllCheckResponse() {
	}

	public GetAllCheckResponse(List<UserCheckAction> result) {
		super();
		this.result = result;
	}

	public List<UserCheckAction> getResult() {
		return result;
	}

	public void setResult(List<UserCheckAction> result) {
		this.result = result;
	}

}
