package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

//@Repository
public class RamActionDao implements ActionDao {

	private List<ActionEntity> actions;
	private AtomicLong nextId;

	public RamActionDao() {
		this.actions = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1L);
	}

	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		actionEntity.setKey(this.nextId.getAndIncrement() + "");
		this.actions.add(actionEntity);
		return actionEntity;
	}

	@Override
	public List<ActionEntity> readAll() {
		return this.actions;
	}

	@Override
	public void deleteAll() {
		this.actions.clear();

	}

}
