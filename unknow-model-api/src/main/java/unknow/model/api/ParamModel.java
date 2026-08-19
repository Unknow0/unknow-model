/**
 * 
 */
package unknow.model.api;

/**
 * method parameter
 * @author unknow
 */
public interface ParamModel extends WithAnnotation, WithType, WithName, WithParent<MethodModel> {
	/**
	 * @return index of the param
	 */
	int index();
}
