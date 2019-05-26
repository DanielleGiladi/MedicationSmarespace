package smartspace.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import smartspace.dao.ElementNotFoundException;
import smartspace.data.ElementEntity;
import smartspace.logic.ElementService;

@RestController
public class ElementController {

	private ElementService elementService;

	@Autowired
	public ElementController(ElementService elementService) {
		super();
		this.elementService = elementService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}",

			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ElementBoundary> publish(@RequestBody ElementBoundary[] elements,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {

		List<ElementEntity> elementsList = new ArrayList<ElementEntity>();
		for (int i = 0; i < elements.length; i++) {
			elementsList.add(elements[i].toEntity());
		}

		return this.elementService.publishNewElements(elementsList, adminSmartspace, adminEmail).stream()

				.map(ElementBoundary::new).collect(Collectors.toList());

	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/admin/elements/{adminSmartspace}/{adminEmail}", produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary[] getElements(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		return this.elementService.getElementsAdmin(size, page, adminSmartspace, adminEmail) // ElementEntity List
				.stream() // ElementEntity Stream
				.map(ElementBoundary::new) // ElementBoundary Stream
				.collect(Collectors.toList()) // ElementBoundary List
				.toArray(new ElementBoundary[0]); // ElementBoundary[]
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/elements/{managerSmartspace}/{managerEmail}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ElementBoundary createElementByManager(@RequestBody ElementBoundary element,
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail) {

		return new ElementBoundary(
				this.elementService.createNewElementByManager(element.toEntity(), managerSmartspace, managerEmail));

	}

	@RequestMapping(method = RequestMethod.PUT, path = "/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(@RequestBody ElementBoundary element,
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace, @PathVariable("elementId") String elementId) {
		ElementEntity elementEntity;
		elementEntity = element.toEntity();
		elementEntity.setKey(elementSmartspace + "#" + elementId);
		this.elementService.update(elementEntity, managerSmartspace, managerEmail);

	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}", produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary getSpecificElement(@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail, @PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") Long elementId) {

		ElementEntity entity = this.elementService.getElementById(userSmartspace, userEmail, elementSmartspace,
				elementId);
		return new ElementBoundary(entity);

	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/elements/{userSmartspace}/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementBoundary[] genericGetElements(@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "search", required = false, defaultValue = "") String search,
			@RequestParam(name = "value", required = false) String value,
			@RequestParam(name = "x", required = false, defaultValue = "0") double x,
			@RequestParam(name = "y", required = false, defaultValue = "0") double y,
			@RequestParam(name = "distance", required = false, defaultValue = "0") double distance,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		switch (search) {
		case "name": {
			return this.elementService.getElementsByName(userSmartspace, userEmail, size, page, value) // ElementEntity
																										// List
					.stream() // ElementEntity Stream
					.map(ElementBoundary::new) // ElementBoundary Stream
					.collect(Collectors.toList()) // ElementBoundary List
					.toArray(new ElementBoundary[0]); // ElementBoundary[]
		}

		case "type": {
			return this.elementService.getElementsByType(userSmartspace, userEmail, size, page, value) // ElementEntity
																										// // List
					.stream() // ElementEntity Stream
					.map(ElementBoundary::new) // ElementBoundary Stream
					.collect(Collectors.toList())// ElementBoundary List
					.toArray(new ElementBoundary[0]);// ElementBoundary[]
		}

		case "location": {
			double minX = x - distance;
			double maxX = x + distance;
			double minY = y - distance;
			double maxY = y + distance;

			return this.elementService
					.getElementsByNearLocation(userSmartspace, userEmail, size, page, minX, maxX, minY, maxY).stream()
					.map(ElementBoundary::new).collect(Collectors.toList()).toArray(new ElementBoundary[0]);
		}

		default: {
			return this.elementService.getElements(userSmartspace, userEmail, size, page) // ElementEntity List
					.stream() // ElementEntity Stream
					.map(ElementBoundary::new) // ElementBoundary Stream
					.collect(Collectors.toList()) // ElementBoundary List
					.toArray(new ElementBoundary[0]); // ElementBoundary[]

		}
		}

	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorMessage handleException(ElementNotFoundException e) {
		String message = e.getMessage();
		if (message == null || message.trim().isEmpty()) {
			message = "could not find element";
		}
		return new ErrorMessage(message);
	}

}