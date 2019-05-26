package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.logic.MedicationCategory;

@Repository
public class RdbElementDao implements AdvancedElementDao<String> {
	private ElementCrud elementCrud;
	private IdCreatorCrud idCreatorCrud;

	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	@Autowired
	public RdbElementDao(ElementCrud elementCrud, IdCreatorCrud idCreatorCrud) {
		this.elementCrud = elementCrud;

		this.idCreatorCrud = idCreatorCrud;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity element) {
		IdCreator idCreator = this.idCreatorCrud.save(new IdCreator());
		element.setKey(smartspace + "#" + idCreator.getId());

		// SQL: INSERT
		if (!this.elementCrud.existsById(element.getKey())) {
			return this.elementCrud.save(element);
		} else {
			throw new RuntimeException("element already exists with id: " + element.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String key) {
		// SQL: SELECT
		return this.elementCrud.findById(key);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {
		// SQL: SELECT
		List<ElementEntity> rv = new ArrayList<>();
		this.elementCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void update(ElementEntity elementEntity) {

		ElementEntity found = readById(elementEntity.getKey())
				.orElseThrow(() -> new RuntimeException("Invalid element key: " + elementEntity.getKey()));

		if (elementEntity.isExpired() != found.isExpired()) {
			found.setLocation(new Location(found.getLocation().getX() * (-1), found.getLocation().getY() * (-1)));
			found.setExpired(elementEntity.isExpired());
		}
		
		if (elementEntity.getLocation() != null)
			found.setLocation(elementEntity.getLocation());

		if (elementEntity.getName() != null)
			found.setName(elementEntity.getName());
		if (elementEntity.getType() != null)
			found.setType(elementEntity.getType());
		// SQL: UPDATE
		this.elementCrud.save(found);
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.elementCrud.deleteAll();
	}

	@Override
	public void deleteByKey(String elementKey) {
		this.elementCrud.deleteById(elementKey);

	}

	@Override
	public void delete(ElementEntity elementEntity) {
		this.elementCrud.delete(elementEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(int size, int page, String sortAttr) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortAttr)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readByTimestamps(Date from, Date to, int size, int page, String sortAttr) {
		return this.elementCrud.findAllByCreateTimestampBetween(from, to,
				PageRequest.of(page, size, Direction.ASC, sortAttr));
	}

	// TODO Check
	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> filterNotExpired(int page, int size, String sortAttr) {
		// SQL: SELECT
		return this.elementCrud.findAllByExpired(false, PageRequest.of(page, size, Direction.ASC, sortAttr));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> filterName(int page, int size, String sortAttr, String name, boolean expired) {
		// SQL: SELECT
		if (expired)
			return this.elementCrud.findAllByName(name, PageRequest.of(page, size, Direction.ASC, sortAttr));
		else
			return this.elementCrud.findAllByNameAndExpired(name, expired,
					PageRequest.of(page, size, Direction.ASC, sortAttr));

	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> filterType(int page, int size, String sortAttr, String type, boolean expired) {
		// SQL: SELECT
		if (expired)
			return this.elementCrud.findAllByType(type, PageRequest.of(page, size, Direction.ASC, sortAttr));
		else
			return this.elementCrud.findAllByTypeAndExpired(type, expired,
					PageRequest.of(page, size, Direction.ASC, sortAttr));

	}

	@Override
	public List<ElementEntity> filterNearLocation(int page, int size, String sortAttr, double minX, double maxX,
			double minY, double maxY, boolean expired) {
		if (expired)
			return this.elementCrud.findAllByLocation_XBetweenAndLocation_YBetween(minX, maxX, minY, maxY,
					PageRequest.of(page, size, Direction.ASC, sortAttr));
		else
			return this.elementCrud.findAllByLocation_XBetweenAndLocation_YBetweenAndExpired(minX, maxX, minY, maxY,
					expired, PageRequest.of(page, size, Direction.ASC, sortAttr));
	}

	@Override
	@Transactional
	public ElementEntity createImport(ElementEntity element) {

		element.setKey(element.getElementSmartspace() + "#" + element.getElementId());

		// SQL: INSERT
		return this.elementCrud.save(element);
	}

	

}