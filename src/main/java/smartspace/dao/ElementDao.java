package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface ElementDao<K> {
	public ElementEntity create(ElementEntity elementEntity);

	public Optional<ElementEntity> readById(K elementKey);

	public List<ElementEntity> readAll();

	public void update(ElementEntity elementEntity);

	public void deleteByKey(K elementKey);

	public void delete(ElementEntity elementEntity);

	public void deleteAll();
}
