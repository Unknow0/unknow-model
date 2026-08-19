/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractParamModel;

/**
 * @author unknow
 */
public class JvmParam extends AbstractParamModel implements ParamModel {
	private final Parameter p;

	/**
	 * create new JvmParam
	 * 
	 * @param loader the loader
	 * @param m the owner of the param
	 * @param p the param
	 * @param index the parameter index
	 */
	public JvmParam(ModelLoader loader, MethodModel m, Parameter p, int index) {
		super(loader, m, index);
		this.p = p;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(p.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public String name() {
		return p.getName();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(p.getParameterizedType().getTypeName(), parent().parent().parameters());
	}
}