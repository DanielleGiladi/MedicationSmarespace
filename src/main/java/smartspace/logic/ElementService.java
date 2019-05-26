package smartspace.logic;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public List<ElementEntity> publishNewElements(List<ElementEntity> elementEntity, String adminSmartspace,
			String adminEmail);

	public List<ElementEntity> getElementsAdmin(int size, int page, String adminSmartspace, String adminEmail);

	public List<ElementEntity> getElements(String userSmartspace, String userEmail, int size, int page);

	public void update(ElementEntity entity, String managerSmartspace, String managerEmail);

	public ElementEntity createNewElementByManager(ElementEntity elementEntity, String managerSmartspace,
			String managerEmail);

	public List<ElementEntity> getElementsByName(String userSmartspace, String userEmail, int size, int page,
			String name);

	public List<ElementEntity> getElementsByType(String userSmartspace, String userEmail, int size, int page,
			String type);

	public List<ElementEntity> getElementsByNearLocation(String userSmartspace, String userEmail, int size, int page,
			double minX, double maxX, double minY, double maxY);

	public ElementEntity getElementById(String userSmartspace, String userEmail, String elementSmartspace,
			Long elementId);

}
