/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractFieldModel;

/**
 * @author unknow
 */
public class JvmField extends AbstractFieldModel implements FieldModel, JvmMod {
	private final Field f;

	/**
	 * create new JvmField
	 * 
	 * @param loader the loader
	 * @param cl the class owning the field
	 * @param f the field
	 */
	public JvmField(ModelLoader loader, ClassModel cl, Field f) {
		super(loader, cl);
		this.f = f;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(f.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public int mod() {
		return f.getModifiers();
	}

	@Override
	public String name() {
		return f.getName();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(f.getGenericType().getTypeName(), parent().parameters());
	}

	@Override
	public AnnotationValue value(ModelLoader loader) {
		try {
			return JvmAnnotation.getValue(loader, f.get(null));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}
}