package smartspace.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.UserEntity;
import smartspace.logic.UserService;

@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/admin/users/{adminSmartspace}/{adminEmail}",

			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<UserBoundary> publish(@RequestBody UserBoundary[] users,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {
		List<UserEntity> usersList = new ArrayList<UserEntity>();
		for (int i = 0; i < users.length; i++) {
			usersList.add(users[i].toEntity());
		}

		return this.userService.publish(usersList, adminSmartspace, adminEmail).stream().map(UserBoundary::new)
				.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/admin/users/{adminSmartspace}/{adminEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("adminSmartspace") String adminSmartspace, @PathVariable("adminEmail") String adminEmail) {

		return this.userService.getUsers(size, page, adminSmartspace, adminEmail) // UserEntity List

				.stream() // UserEntity Stream
				.map(UserBoundary::new) // UserBoundary Stream
				.collect(Collectors.toList()) // UserBoundary List
				.toArray(new UserBoundary[0]); // UserBoundary[]

	}

	@RequestMapping(method = RequestMethod.POST, path = "/smartspace/users",

			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createUser(@RequestBody NewUserForm user) {

		UserEntity userEntity = this.userService.publishNewUserForm(user);
		return new UserBoundary(userEntity);

	}

	@RequestMapping(method = RequestMethod.GET, path = "/smartspace/users/login/{userSmartspace}/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary login(@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail) {
		return this.userService.getUser(userSmartspace, userEmail);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "/smartspace/users/login/{userSmartspace}/{userEmail}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@RequestBody UserBoundary user, @PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail) {
		user.setKey(new UserKey(userEmail, userSmartspace));
		this.userService.update(user.toEntity());

	}
	



}