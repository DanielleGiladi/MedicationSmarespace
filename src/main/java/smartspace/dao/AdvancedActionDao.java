package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ActionEntity;

public interface AdvancedActionDao extends ActionDao {
	public ActionEntity createImport(ActionEntity actionEntity);
	public List<ActionEntity> readAll(int size, int page);

	public List<ActionEntity> readAll(int size, int page, String sortAttr);

	public List<ActionEntity> readByTimestamps(Date from, Date to, int size, int page, String sortAttr);

}
