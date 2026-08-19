/**
 * 
 */
package unknow.model.api;

/**
 * @author unknow
 */
public interface FieldModel extends WithAnnotation, WithType, WithMod, WithName, WithParent<ClassModel> {
	/**
	 * @param loader the model loader
	 * @return value for annotation
	 */
	AnnotationValue value(ModelLoader loader);
}
