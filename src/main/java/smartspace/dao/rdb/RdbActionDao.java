package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedActionDao;
import smartspace.data.ActionEntity;

@Repository
public class RdbActionDao implements AdvancedActionDao {

	private ActionCrud actionCrud;
	private IdCreatorCrud idCreatorCrud;

	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	public RdbActionDao() {
	}

	@Autowired
	public RdbActionDao(ActionCrud actionCrud, IdCreatorCrud idCreatorCrud) {
		this.actionCrud = actionCrud;
		// this.nextId = new AtomicLong(1L);
		this.idCreatorCrud = idCreatorCrud;

	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		actionEntity.setKey(smartspace + "#" + idCreator.getId());
		// SQL: INSERT
		if (!this.actionCrud.existsById(actionEntity.getKey())) {
			return this.actionCrud.save(actionEntity);
		} else {
			throw new RuntimeException("action already exists with id: " + actionEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		// SQL: SELECT
		List<ActionEntity> rv = new ArrayList<>();
		this.actionCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll(int size, int page, String sortAttr) {
		return this.actionCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readByTimestamps(Date from, Date to, int size, int page, String sortAttr) {
		return this.actionCrud.findAllBycreationTimestampBetween(from, to,
				PageRequest.of(page, size, Direction.ASC, sortAttr));
	}

	@Override
	@Transactional
	public ActionEntity createImport(ActionEntity actionEntity) {
//		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		actionEntity.setKey(actionEntity.getActionSmartspace() + "#" + actionEntity.getActionId());
		// SQL: INSERT
		return this.actionCrud.save(actionEntity);
	}
}
