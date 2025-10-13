/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.WithParent;

/**
 * @author unknow
 * @param <T> owner of the param
 */
public class JvmParam<T extends WithParent<ClassModel>> implements ParamModel<T> {
	private final JvmModelLoader loader;
	private final T m;
	private final Parameter p;
	private final int index;
	private TypeModel type;
	private Collection<AnnotationModel> annotations;

	/**
	 * create new JvmParam
	 * 
	 * @param loader the loader
	 * @param m the owner of the param
	 * @param p the param
	 * @param index the parameter index
	 */
	public JvmParam(JvmModelLoader loader, T m, Parameter p, int index) {
		this.loader = loader;
		this.m = m;
		this.p = p;
		this.index = index;
	}

	@Override
	public int index() {
		return index;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = Arrays.stream(p.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
		return annotations;
	}

	@Override
	public String name() {
		return p.getName();
	}

	@Override
	public T parent() {
		return m;
	}

	@Override
	public TypeModel type() {
		if (type == null)
			type = loader.get(p.getParameterizedType().getTypeName(), m.parent().parameters());
		return type;
	}

	@Override
	public String toString() {
		return p.toString();
	}
}