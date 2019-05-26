package smartspace.plugins;
import smartspace.data.ActionEntity;


public interface Plugin {

	public Object doSomething(ActionEntity entity) throws Exception;

}
