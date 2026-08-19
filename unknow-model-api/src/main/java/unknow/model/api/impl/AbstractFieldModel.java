package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;

public abstract class AbstractFieldModel implements FieldModel {
	private final ModelLoader loader;
	private final ClassModel cl;
	private Collection<AnnotationModel> annotations;
	private TypeModel type;

	protected AbstractFieldModel(ModelLoader loader, ClassModel cl) {
		this.loader = loader;
		this.cl = cl;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return type
	 */
	protected abstract TypeModel loadType(ModelLoader loader);

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}

	@Override
	public final ClassModel parent() {
		return cl;
	}

	@Override
	public final TypeModel type() {
		if (type == null)
			type = loadType(loader);
		return type;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + type() + "." + name();
	}

}
