/**
 * 
 */
package unknow.model.api;

/**
 * @author unknow
 * @param <T> owner model
 */
public interface ParamModel<T extends WithParent<ClassModel>> extends WithAnnotation, WithType, WithName, WithParent<T> {
	/**
	 * @return index of the param
	 */
	int index();
}
