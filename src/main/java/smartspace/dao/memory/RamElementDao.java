package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;

//@Repository
public class RamElementDao implements ElementDao<String> {
	private List<ElementEntity> elements;
	private AtomicLong nextId;

	public RamElementDao() {
		this.elements = Collections.synchronizedList(new ArrayList<>());
		this.nextId = new AtomicLong(1L);
	}

	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		elementEntity.setKey(this.nextId.getAndIncrement() + "");
		this.elements.add(elementEntity);
		return elementEntity;
	}

	@Override
	public Optional<ElementEntity> readById(String elementKey) {
		return this.elements.stream().filter(message -> message.getKey().equals(elementKey)).findFirst();
	}

	@Override
	public List<ElementEntity> readAll() {
		return this.elements;
	}

	@Override
	public void update(ElementEntity elementEntity) {
		ElementEntity found = readById(elementEntity.getKey())
				.orElseThrow(() -> new RuntimeException("Invalide element key : " + elementEntity.getKey()));
		found.setExpired(elementEntity.isExpired());
		if (elementEntity.getLocation() != null)
			found.setLocation(elementEntity.getLocation());
		if (elementEntity.getName() != null)
			found.setName(elementEntity.getName());
		if (elementEntity.getType() != null)
			found.setType(elementEntity.getType());
	}

	@Override
	public void deleteByKey(String elementKey) {
		this.elements.removeIf(element -> element.getKey().equals(elementKey));
	}

	@Override
	public void delete(ElementEntity elementEntity) {
		this.elements.remove(elementEntity);
	}

	@Override
	public void deleteAll() {
		this.elements.clear();
	}

	public AtomicLong getNextId() {
		return nextId;
	}

}
