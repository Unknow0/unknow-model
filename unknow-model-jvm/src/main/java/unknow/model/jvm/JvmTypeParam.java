/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;
import unknow.model.api.impl.AbstractTypeParamModel;

/**
 * @author unknow
 */
public class JvmTypeParam extends AbstractTypeParamModel implements TypeParamModel {
	private final TypeVariable<?> t;

	/**
	 * create new JvmTypeParam
	 * 
	 * @param loader the loader
	 * @param c the class owning this param
	 * @param t the param variable
	 * @param type the concrete type
	 */
	public JvmTypeParam(ModelLoader loader, ClassModel c, TypeVariable<?> t, TypeModel type) {
		super(loader, c, type);
		this.t = t;
	}

	@Override
	public String name() {
		return t.getName();
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(t.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	protected List<ClassModel> loadBounds(ModelLoader loader) {
		return Arrays.stream(t.getBounds()).map(b -> loader.get(b.getTypeName(), parent().parameters()).asClass()).collect(Collectors.toList());
	}
}