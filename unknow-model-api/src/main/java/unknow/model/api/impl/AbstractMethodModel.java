package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;

public abstract class AbstractMethodModel implements MethodModel {
	private final ModelLoader loader;
	private final ClassModel parent;
	private final String name;
	private TypeModel type;
	private Collection<AnnotationModel> annotations;
	private List<ParamModel> params;

	protected AbstractMethodModel(ModelLoader loader, ClassModel parent, String name) {
		this.loader = loader;
		this.parent = parent;
		this.name = name;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract TypeModel loadType(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return parameters
	 */
	protected abstract List<ParamModel> loadParameters(ModelLoader loader);

	@Override
	public final ClassModel parent() {
		return parent;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final TypeModel type() {
		if (type == null)
			type = loadType(loader);
		return type;
	}

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}

	@Override
	public final List<ParamModel> parameters() {
		if (params == null)
			params = loadParameters(loader);
		return params;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + parent.name() + "." + signature();
	}
}
