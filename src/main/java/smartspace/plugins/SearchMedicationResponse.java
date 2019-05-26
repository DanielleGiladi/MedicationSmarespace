package smartspace.plugins;

import smartspace.data.Location;

public class SearchMedicationResponse {
	private Location result;

	public SearchMedicationResponse() {
	}

	public SearchMedicationResponse(Location result) {
		super();
		this.result = result;
	}

	public Location getResult() {
		return result;
	}

	public void setResult(Location result) {
		this.result = result;
	}

}
