package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationDeclModel.AnnotationMemberDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ModelLoader;

public abstract class AbstractAnnotationMemberDeclModel implements AnnotationMemberDeclModel {
	private final ModelLoader loader;
	private final AnnotationDeclModel parent;
	private final String name;
	private Collection<AnnotationModel> annotations;
	private AnnotationValue value;

	/**
	 * @param loader the model loader
	 * @param parent annotion declaring this member
	 * @param name member name
	 */
	protected AbstractAnnotationMemberDeclModel(ModelLoader loader, AnnotationDeclModel parent, String name) {
		this.loader = loader;
		this.parent = parent;
		this.name = name;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return default value
	 */
	protected abstract AnnotationValue loadValue(ModelLoader loader);

	@Override
	public final AnnotationDeclModel parent() {
		return parent;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}

	@Override
	public AnnotationValue defaultValue() {
		if (value == null)
			value = loadValue(loader);
		return value;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + parent.name() + "." + name;
	}
}
