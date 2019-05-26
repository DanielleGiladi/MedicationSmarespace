package smartspace.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import smartspace.dao.AdvancedUserDao;


@Component
@Aspect
public class LogMethodAspect {
	private Log log = LogFactory.getLog(LogMethodAspect.class);

	@Autowired
	public LogMethodAspect(AdvancedUserDao<String> userDao) {
		super();
	}


	@Around("@annotation(smartspace.aop.LogMethod)")
	public Object printLogService(ProceedingJoinPoint pjp) throws Throwable{
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName(); 
		log.trace("### " + className + " : " + methodName);
		try {
			Object rv = pjp.proceed();
			return rv;
		} catch (Throwable e) {
			throw e;
		}
	}
}
