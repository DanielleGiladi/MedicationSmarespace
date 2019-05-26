package smartspace.layout;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ActionEntity;
import smartspace.logic.ActionService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ActionController {
	private ActionService actionService;

	@Autowired
	public ActionController(ActionService actionService) {
		super();
		this.actionService = actionService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}",

			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ActionBoundary> publish(@RequestBody ActionBoundary[] actions,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		List<ActionEntity> actionsList = new ArrayList<ActionEntity>();
		for (int i = 0; i < actions.length; i++) {
			actionsList.add(actions[i].toEntity());
		}

		return this.actionService.publishNewAction(actionsList, adminSmartspace, adminEmail).stream() // ElementEntity
				.map(ActionBoundary::new).collect(Collectors.toList());

	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/admin/actions/{adminSmartspace}/{adminEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActions(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		return this.actionService.getActions(size, page, adminSmartspace, adminEmail) // ActionEntity List
				.stream() // ActionEntity Stream
				.map(ActionBoundary::new) // ActionBoundary Stream
				.collect(Collectors.toList()) // ActionBoundary List
				.toArray(new ActionBoundary[0]); // ActionBoundary[]

	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/actions",

			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object invokeAction(@RequestBody ActionBoundary action) {
		ActionEntity actionEntity = action.toEntity();
		return this.actionService.invoke(actionEntity);

	}

}
