/**
 * 
 */
package unknow.model.api;

import java.util.Iterator;

/**
 * @author unknow
 */
public class AncestrorIterable implements Iterable<ClassModel> {
	private final ClassModel clazz;

	/**
	 * create new AncestrorIterable
	 * 
	 * @param clazz the root class
	 */
	public AncestrorIterable(ClassModel clazz) {
		this.clazz = clazz;
	}

	@Override
	public Iterator<ClassModel> iterator() {
		return new AncestrorIterator(clazz);
	}
}
