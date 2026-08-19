package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;

public abstract class AbstractParamModel implements ParamModel {
	private final ModelLoader loader;
	private final MethodModel parent;
	private final int index;
	private TypeModel type;
	private Collection<AnnotationModel> annotations;

	protected AbstractParamModel(ModelLoader loader, MethodModel parent, int index) {
		this.loader = loader;
		this.parent = parent;
		this.index = index;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	protected abstract TypeModel loadType(ModelLoader loader);

	@Override
	public final MethodModel parent() {
		return parent;
	}

	@Override
	public final int index() {
		return index;
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
	public final String toString() {
		return getClass().getSimpleName() + " " + name();
	}
}
