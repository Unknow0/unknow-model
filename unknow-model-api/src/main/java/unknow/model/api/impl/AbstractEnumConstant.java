package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.EnumModel;
import unknow.model.api.EnumModel.EnumConstant;
import unknow.model.api.ModelLoader;

public abstract class AbstractEnumConstant implements EnumConstant {
	private final ModelLoader loader;
	private final EnumModel parent;
	private List<AnnotationModel> annotations;

	/**
	 * @param loader the model loader
	 * @param parent enum declaring this constant
	 */
	protected AbstractEnumConstant(ModelLoader loader, EnumModel parent) {
		this.loader = loader;
		this.parent = parent;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	@Override
	public final EnumModel parent() {
		return parent;
	}

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + parent().name() + "." + name();
	}
}
