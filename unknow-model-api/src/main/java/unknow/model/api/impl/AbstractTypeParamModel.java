package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;

public abstract class AbstractTypeParamModel implements TypeParamModel {
	private final ModelLoader loader;
	private final ClassModel c;
	private final TypeModel type;
	private Collection<AnnotationModel> annotations;
	private List<ClassModel> bounds;

	protected AbstractTypeParamModel(ModelLoader loader, ClassModel c, TypeModel type) {
		this.loader = loader;
		this.c = c;
		this.type = type;
	}

	/**
	 * @param loader the model loader
	 * @return bounds
	 */
	protected abstract List<ClassModel> loadBounds(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	@Override
	public final ClassModel parent() {
		return c;
	}

	@Override
	public final TypeModel type() {
		return type;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null) {
			annotations = loadAnnotations(loader);
		}
		return annotations;
	}

	@Override
	public final List<ClassModel> bounds() {
		if (bounds == null)
			bounds = loadBounds(loader);
		return bounds;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder(name());
		if (type() != null)
			sb.append('[').append(type()).append(']');
		return sb.toString();
	}
}
