package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;

public abstract class AbstractClassModel extends AbstractWithAnnotation implements ClassModel {
	private final PackageModel parent;
	private final String name;
	private final TypeModel[] paramsClass;

	private ClassModel superType;
	private List<ClassModel> interfaces;
	private List<TypeParamModel> parameters;
	private Collection<MethodModel> constructors;
	private Collection<FieldModel> fields;
	private Collection<MethodModel> methods;

	/**
	 * create new AbstractClassModel
	 * 
	 * @param loader      the loader
	 * @param parent package
	 * @param name class binary name
	 * @param paramsClass class parameter
	 */
	protected AbstractClassModel(ModelLoader loader, PackageModel parent, String name, TypeModel[] paramsClass) {
		super(loader);
		this.parent = parent;
		this.name = name;
		this.paramsClass = paramsClass;
	}

	/**
	* @param loader the model loader
	* @return superClass binaryName
	*/
	protected abstract ClassModel loadSuperType(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return interfaces binary name
	 */
	protected abstract List<ClassModel> loadInterfaces(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @param paramsClass actual generic type
	 * @return generic parameters
	 */
	protected abstract List<TypeParamModel> loadParameters(ModelLoader loader, TypeModel[] paramsClass);

	/**
	 * @param loader the model loader
	 * @return fields
	 */
	protected abstract Collection<FieldModel> loadFields(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return constructors
	 */
	protected abstract Collection<MethodModel> loadConstructors(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return the methods
	 */
	protected abstract Collection<MethodModel> loadMethods(ModelLoader loader);

	@Override
	public final PackageModel parent() {
		return parent;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final String genericName() {
		if (parameters().isEmpty())
			return name();
		StringBuilder sb = new StringBuilder(name()).append('<');
		for (TypeParamModel t : parameters())
			sb.append(t.type().genericName()).append(',');
		sb.setCharAt(sb.length() - 1, '>');
		return sb.toString();
	}

	@Override
	public final ClassModel superType() {
		if (isInterface())
			return null;
		if (superType == null)
			superType = loadSuperType(loader);
		return superType;
	}

	@Override
	public final List<ClassModel> interfaces() {
		if (interfaces == null)
			interfaces = loadInterfaces(loader);
		return interfaces;
	}

	@Override
	public final List<TypeParamModel> parameters() {
		if (parameters == null)
			parameters = loadParameters(loader, paramsClass);
		return parameters;
	}

	@Override
	public final Collection<MethodModel> constructors() {
		if (constructors == null)
			constructors = loadConstructors(loader);
		return constructors;
	}

	@Override
	public final Collection<FieldModel> fields() {
		if (fields == null)
			fields = loadFields(loader);
		return fields;
	}

	@Override
	public final Collection<MethodModel> methods() {
		if (methods == null)
			methods = loadMethods(loader);
		return methods;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + genericName();
	}
}
