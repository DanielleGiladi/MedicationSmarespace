package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ElementEntity;

public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String> {

	public List<ElementEntity> findAllByCreateTimestampBetween(@Param("fromTimestamp") Date from,
			@Param("toTimestamp") Date to, Pageable pageable);

	public List<ElementEntity> findAllByExpired(@Param("expired") Boolean expired, Pageable pageable);

	public List<ElementEntity> findAllByName(@Param("name") String name, Pageable pageable);

	public List<ElementEntity> findAllByNameAndExpired(@Param("name") String name, @Param("expired") Boolean expired,
			Pageable pageable);

	public List<ElementEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public ElementEntity findAllByNameAndType(@Param("name") String name, @Param("type") String type);

	public List<ElementEntity> findAllByTypeAndExpired(@Param("type") String type, @Param("expired") Boolean expired,
			Pageable pageable);

	public List<ElementEntity> findAllByLocation_XBetweenAndLocation_YBetween(@Param("minX") double minX,
			@Param("maxX") double maxX, @Param("minY") double minY, @Param("maxY") double maxY, Pageable pageable);

	public List<ElementEntity> findAllByLocation_XBetweenAndLocation_YBetweenAndExpired(@Param("minX") double minX,
			@Param("maxX") double maxX, @Param("minY") double minY, @Param("maxY") double maxY,
			@Param("expired") Boolean expired, Pageable pageable);

	public List<ElementEntity> findAllByNameAndTypeAndExpired(@Param("name") String name, @Param("type") String type,
			@Param("expired") Boolean expired);

}