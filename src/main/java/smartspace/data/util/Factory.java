package smartspace.data.util;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
public class Factory implements EntityFactory {
	@Value("${smartspace:Anonymous}")
	private String smartspace;

	@Override
	public UserEntity createNewUser(String UserEmail, String userSmartspace, String username, String avatar,
			UserRole role, long points) {
		return new UserEntity(UserEmail, userSmartspace, username, avatar, role, points);
	}

	@Override
	public ElementEntity createNewElement(String name, String type, Location location, Date creationTimeStamp,
			String creatorEmail, String creatorSmartspace, boolean expired, Map<String, Object> moreAttributes) {

		return new ElementEntity(smartspace, location, name, type, creationTimeStamp, expired, creatorSmartspace,
				creatorEmail, moreAttributes);
	}

	@Override
	public ActionEntity createNewAction(String elementId, String elementSmartspace, String actionType,
			Date creationTimeStamp, String playerEmail, String playerSmartspace, Map<String, Object> moreAttributes) {
		return new ActionEntity(smartspace, elementSmartspace, elementId, actionType, playerSmartspace, playerEmail,
				creationTimeStamp, moreAttributes);
	}

}
