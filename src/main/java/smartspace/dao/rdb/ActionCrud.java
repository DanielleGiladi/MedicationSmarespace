package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ActionEntity;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String> {
	public List<ActionEntity> findAllBycreationTimestampBetween(@Param("fromTimestamp") Date from,
			@Param("toTimestamp") Date to, Pageable pageable);

	public List<ActionEntity> findAllByActionTypeAndElementSmartspaceAndElementId(@Param("actionType") String actionType
			,@Param("elementSmartspace") String elementSmartspace , @Param("elementId") String elementId, Pageable pageable);

	public List<ActionEntity> findAllByActionTypeAndElementSmartspaceAndElementIdAndPlayerSmartspaceAndPlayerEmail(
			@Param("actionType") String actionType
			,@Param("elementSmartspace") String elementSmartspace , @Param("elementId") String elementId
			,@Param("playerSmartspace") String playerSmartspace , @Param("playerEmail") String playerEmail);
}
