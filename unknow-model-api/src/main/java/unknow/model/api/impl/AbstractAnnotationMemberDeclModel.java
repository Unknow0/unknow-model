package unknow.model.api.impl;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationDeclModel.AnnotationMemberDeclModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ModelLoader;

public abstract class AbstractAnnotationMemberDeclModel extends AbstractWithAnnotation implements AnnotationMemberDeclModel {
	private final AnnotationDeclModel parent;
	private final String name;
	private AnnotationValue value;

	/**
	 * @param loader the model loader
	 * @param parent annotion declaring this member
	 * @param name member name
	 */
	protected AbstractAnnotationMemberDeclModel(ModelLoader loader, AnnotationDeclModel parent, String name) {
		super(loader);
		this.parent = parent;
		this.name = name;
	}

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
