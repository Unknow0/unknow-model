/**
 * 
 */
package unknow.model.api;

import java.util.List;

/**
 * @author unknow
 */
public interface TypeParamModel extends WithAnnotation, WithType, WithName, WithParent<ClassModel> {
	/**
	 * @return parameter bounds
	 */
	List<ClassModel> bounds();
}
