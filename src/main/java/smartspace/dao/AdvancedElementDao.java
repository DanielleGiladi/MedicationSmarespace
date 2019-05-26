package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ElementEntity;

public interface AdvancedElementDao<KEY> extends ElementDao<KEY> {
	public ElementEntity createImport(ElementEntity elementEntity);

	public List<ElementEntity> readAll(int size, int page);

	public List<ElementEntity> readAll(int size, int page, String sortAttr);

	public List<ElementEntity> readByTimestamps(Date from, Date to, int size, int page, String sortAttr);

	public List<ElementEntity> filterNotExpired(int page, int size, String sortAttr);

	public List<ElementEntity> filterName(int page, int size, String sortAttr, String name, boolean expired);

	public List<ElementEntity> filterType(int page, int size, String sortAttr, String type, boolean expired);
	
	public List<ElementEntity> filterNearLocation (int page, int size, String sortAttr,double minX, 
			double maxX, double minY,double maxY,boolean expired);

}