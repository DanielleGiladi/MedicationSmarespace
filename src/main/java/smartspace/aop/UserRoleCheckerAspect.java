package smartspace.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

@Component
@Aspect
public class UserRoleCheckerAspect {
	private AdvancedUserDao<String> userDao;

	@Autowired
	public UserRoleCheckerAspect(AdvancedUserDao<String> userDao) {
		this.userDao = userDao;
	}


	@Around("@annotation(smartspace.aop.UserRoleChecker) && args(userSmartspace,userEmail,..)")
	public Object checkPlayerManger(ProceedingJoinPoint pjp, String userSmartspace, String userEmail) throws Throwable {
		UserEntity userEntity = this.userDao.readById(userSmartspace + "#" + userEmail).orElse(null);
		Object[] argObj = pjp.getArgs();
		// if userEntity Exist
		if (userEntity == null || userEntity.getRole() == UserRole.ADMIN) {
			argObj[0] = "exception@" + argObj[0];
		} else if (userEntity.getRole() == UserRole.MANAGER) {
			argObj[0] = "manager@" + argObj[0];
		} else if (userEntity.getRole() == UserRole.PLAYER) {
			argObj[0] = "player@" + argObj[0];
		}

		// invoke method
		try {
			Object rv = pjp.proceed(argObj);
			return rv;
		} catch (Throwable e) {
			throw e;
		}

	}
}
