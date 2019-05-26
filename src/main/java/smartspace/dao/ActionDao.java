package smartspace.dao;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionDao {
	public ActionEntity create(ActionEntity actionEntity);

	public List<ActionEntity> readAll();

	public void deleteAll();

}
