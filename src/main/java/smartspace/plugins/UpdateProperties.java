package smartspace.plugins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;

@Repository
public class UpdateProperties {
	private AdvancedElementDao<String> dao;

	public UpdateProperties() {

	}

	@Autowired
	public UpdateProperties( AdvancedElementDao<String> dao) {
		super();
		this.dao = dao;
	}

	public AdvancedElementDao<String> getDao() {
		return dao;
	}

	public void setDao(AdvancedElementDao<String> dao) {
		this.dao = dao;
	}

	public void addHistory(ElementEntity element, String actionType, String timeStamp) {
		int index = element.getMoreAttributes().size();
		element.getMoreAttributes().put(index + " --> " + actionType, timeStamp);

		this.dao.update(element);
	}

}
