/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractMethodModel;

/**
 * @author unknow
 */
public class JvmMethod extends AbstractMethodModel implements MethodModel, JvmMod {
	private final Method m;

	/**
	 * create new JvmField
	 * 
	 * @param parent the class owning the method
	 * @param loader the loader
	 * @param m the method
	 */
	public JvmMethod(ModelLoader loader, ClassModel parent, Method m) {
		super(loader, parent, m.getName());
		this.m = m;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(m.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public int mod() {
		return m.getModifiers();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(m.getGenericReturnType().getTypeName(), parent().parameters());
	}

	@Override
	protected List<ParamModel> loadParameters(ModelLoader loader) {
		List<ParamModel> params = new ArrayList<>();
		Parameter[] p = m.getParameters();
		for (int i = 0; i < p.length; i++)
			params.add(new JvmParam(loader, this, p[i], i));
		return params;
	}

	// XXX
//	@Override
//	public AnnotationValue defaultValue() {
//		if (defaultValue == null)
//			defaultValue = JvmAnnotation.getValue(loader, m.getDefaultValue());
//		return defaultValue;
//	}
}