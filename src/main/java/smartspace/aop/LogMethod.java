package smartspace.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
 
//print before service method
@Retention(RUNTIME)
@Target(METHOD)
public @interface LogMethod {

}
