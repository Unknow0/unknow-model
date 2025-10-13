/**
 * 
 */
package unknow.model.api;

/**
 * @author unknow
 * @param <T> the parent type
 */
public interface WithParent<T> {

	/**
	 * @return owning model
	 */
	T parent();
}
