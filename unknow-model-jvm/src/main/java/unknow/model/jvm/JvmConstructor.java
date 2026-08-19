/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Constructor;
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
public class JvmConstructor extends AbstractMethodModel implements MethodModel, JvmMod {
	private final Constructor<?> c;

	/**
	 * create new JvmField
	 * 
	 * @param parent the owner
	 * @param loader the loader
	 * @param c the constructor
	 */
	public JvmConstructor(ModelLoader loader, ClassModel parent, Constructor<?> c) {
		super(loader, parent, "<init>");
		this.c = c;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(c.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public int mod() {
		return c.getModifiers();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return null;
	}

	@Override
	protected List<ParamModel> loadParameters(ModelLoader loader) {
		List<ParamModel> params = new ArrayList<>();
		Parameter[] p = c.getParameters();
		for (int i = 0; i < p.length; i++)
			params.add(new JvmParam(loader, this, p[i], i));
		return params;
	}
}