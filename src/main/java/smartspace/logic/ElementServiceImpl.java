package smartspace.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.aop.LogMethod;
import smartspace.aop.UserRoleChecker;
import smartspace.dao.AdvancedElementDao;
import smartspace.dao.ElementNotFoundException;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementService {
	private AdvancedElementDao<String> elements;
	private CheckUserRole userRole;
	@Value("${smartspace.name:Anonymous}")
	private String smartspace;

	@Autowired
	public ElementServiceImpl(AdvancedElementDao<String> elements, CheckUserRole userRole) {
		this.elements = elements;
		this.userRole = userRole;
	}

	@LogMethod
	@Override
	@Transactional
	public List<ElementEntity> publishNewElements(List<ElementEntity> elements, String adminSmartspace,
			String adminEmail) {
		if (userRole.isAdmin(adminSmartspace, adminEmail)) {
			for (int i = 0; i < elements.size(); i++) {
				if (validateElement(elements.get(i))) {
					if (elements.get(i).getMoreAttributes().isEmpty()) {
						elements.get(i).getMoreAttributes().put("valid", true);
					}
					elements.set(i, this.elements.createImport(elements.get(i)));
				} else {
					throw new RuntimeException("Illegal element input");
				}
			}
			return elements;
		} else
			throw new RuntimeException();
	}

	@LogMethod
	@Override
	@Transactional
	public ElementEntity createNewElementByManager(ElementEntity elementEntity, String managerSmartspace,
			String managerEmail) {

		if (userRole.isManager(managerSmartspace, managerEmail)) {
			elementEntity.setCreateEmail(managerEmail);
			elementEntity.setCreatorSmartspace(managerSmartspace);
			if (validateElementCreatedByManager(elementEntity)) {
				elementEntity.setCreateTimestamp(new Date());
				if (elementEntity.getMoreAttributes().isEmpty()) {
					elementEntity.getMoreAttributes().put("valid", true);
				}
				return this.elements.create(elementEntity);
			} else {
				throw new RuntimeException("Illegal element input");
			}
		} else
			throw new RuntimeException();

	}

	@LogMethod
	@Override
	public List<ElementEntity> getElementsAdmin(int size, int page, String adminSmartspace, String adminEmail) {
		if (this.userRole.isAdmin(adminSmartspace, adminEmail))
			return this.elements.readAll(size, page, "name");
		else
			throw new RuntimeException();
	}

	@UserRoleChecker
	@LogMethod
	@Override
	public List<ElementEntity> getElements(String userSmartspace, String userEmail, int size, int page) {
		String[] parts = userSmartspace.split("@");
		switch (parts[0]) {
		case "manager":
			return this.elements.readAll(size, page, "name");

		case "player": {
			return this.elements.filterNotExpired(page, size, "name");
		}
		default:
			throw new RuntimeException();
		}

	}

	@UserRoleChecker
	@LogMethod
	@Override
	public List<ElementEntity> getElementsByName(String userSmartspace, String userEmail, int size, int page,
			String theName) {
		String[] parts = userSmartspace.split("@");
		switch (parts[0]) {
		case "manager":
			return this.elements.filterName(page, size, "name", theName, true);

		case "player": {
			return this.elements.filterName(page, size, "name", theName, false);
		}
		default:
			throw new RuntimeException();
		}

	}

	@UserRoleChecker
	@LogMethod
	@Override
	public List<ElementEntity> getElementsByType(String userSmartspace, String userEmail, int size, int page,
			String theType) {

		String[] parts = userSmartspace.split("@");
		switch (parts[0]) {
		case "manager":
			return this.elements.filterType(page, size, "name", theType, true);

		case "player": {
			return this.elements.filterType(page, size, "name", theType, false);
		}
		default:
			throw new RuntimeException();
		}

	}

	@UserRoleChecker
	@LogMethod
	@Override
	public List<ElementEntity> getElementsByNearLocation(String userSmartspace, String userEmail, int size, int page,
			double minX, double maxX, double minY, double maxY) {

		String[] parts = userSmartspace.split("@");
		switch (parts[0]) {
		case "manager":
			return this.elements.filterNearLocation(page, size, "name", minX, maxX, minY, maxY, true);

		case "player": {
			return this.elements.filterNearLocation(page, size, "name", minX, maxX, minY, maxY, false);
		}
		default:
			throw new RuntimeException();
		}
	}

	@UserRoleChecker
	@LogMethod
	@Override
	public ElementEntity getElementById(String userSmartspace, String userEmail, String elementSmartspace,
			Long elementId) {
		Optional<ElementEntity> entity = this.elements.readById(elementSmartspace + "#" + elementId);
		String[] parts = userSmartspace.split("@");
		switch (parts[0]) {
		case "manager":
			return entity.get();

		case "player": {
			if (!entity.get().isExpired())
				return entity.get();
			else
				throw new ElementNotFoundException();

		}
		default:
			throw new RuntimeException();
		}
	}

	private boolean validateElement(ElementEntity elementEntity) {
		return elementEntity != null && elementEntity.getName() != null && elementEntity.getElementId() != null
				&& elementEntity.getMoreAttributes() != null && !smartspace.equals(elementEntity.getElementSmartspace())
				&& elementEntity.getType() != null && elementEntity.getCreatorSmartspace() != null
				&& elementEntity.getCreateEmail() != null && elementEntity.getLocation() != null
				&& elementEntity.getCreateTimestamp() != null;
	}

	private boolean validateElementCreatedByManager(ElementEntity elementEntity) {
		return elementEntity != null && elementEntity.getName() != null && elementEntity.getMoreAttributes() != null
				&& elementEntity.getType() != null;
	}

	@LogMethod
	@Override
	public void update(ElementEntity entity, String managerSmartspace, String managerEmail) {
		if (userRole.isManager(managerSmartspace, managerEmail))
			this.elements.update(entity);

	}

}