/**
 * 
 */
package unknow.model.jvm;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;
import unknow.model.api.impl.AbstractEnumModel;

/**
 * @author unknow
 */
public class JvmClassOrEnum extends AbstractEnumModel implements ClassModel, JvmMod {
	/** java class */
	protected final Class<?> cl;

	/**
	 * create new JvmClass
	 * 
	 * @param loader the loader
	 * @param cl the class
	 * @param parent the package
	 * @param paramsClass the parameter
	 */
	public JvmClassOrEnum(ModelLoader loader, PackageModel parent, Class<?> cl, TypeModel[] paramsClass) {
		super(loader, parent, cl.getName(), paramsClass);
		this.cl = cl;
	}

	@Override
	public boolean isInterface() {
		return cl.isInterface();
	}

	@Override
	public boolean isEnum() {
		return cl.isEnum();
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(cl.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	protected ClassModel loadSuperType(ModelLoader loader) {
		if (cl.getGenericSuperclass() == null)
			return null;
		return loader.get(cl.getGenericSuperclass().getTypeName(), parameters()).asClass();
	}

	@Override
	protected List<ClassModel> loadInterfaces(ModelLoader loader) {
		Type[] t = cl.getGenericInterfaces();
		List<ClassModel> interfaces = new ArrayList<>(t.length);
		for (int i = 0; i < t.length; i++)
			interfaces.add(loader.get(t[i].getTypeName(), parameters()).asClass());
		return interfaces;
	}

	@Override
	protected Collection<MethodModel> loadConstructors(ModelLoader loader) {
		return Arrays.stream(cl.getDeclaredConstructors()).map(c -> new JvmConstructor(loader, this, c)).collect(Collectors.toList());
	}

	@Override
	protected Collection<FieldModel> loadFields(ModelLoader loader) {
		return Arrays.stream(cl.getDeclaredFields()).map(f -> new JvmField(loader, this, f)).collect(Collectors.toList());
	}

	@Override
	protected Collection<MethodModel> loadMethods(ModelLoader loader) {
		return Arrays.stream(cl.getDeclaredMethods()).map(m -> new JvmMethod(loader, this, m)).collect(Collectors.toList());
	}

	@Override
	protected List<TypeParamModel> loadParameters(ModelLoader loader, TypeModel[] paramsClass) {
		TypeVariable<?>[] typeParameters = cl.getTypeParameters();
		List<TypeParamModel> parameters = new ArrayList<>(typeParameters.length);
		for (int i = 0; i < typeParameters.length; i++)
			parameters.add(new JvmTypeParam(loader, this, typeParameters[i], paramsClass.length == typeParameters.length ? paramsClass[i] : null));
		return parameters;
	}

	@Override
	protected List<EnumConstant> loadEntries(ModelLoader loader) {
		return Arrays.stream(cl.getEnumConstants()).map(c -> new JvmEnumConstant(loader, this, (Enum<?>) c)).collect(Collectors.toList());
	}

	@Override
	public int mod() {
		return cl.getModifiers();
	}
}
