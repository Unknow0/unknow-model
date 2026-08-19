package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;

public abstract class AbstractPackageModel implements PackageModel {
	private final ModelLoader loader;
	private Collection<AnnotationModel> annotations;

	/**
	 * @param loader the model loader
	 */
	public AbstractPackageModel(ModelLoader loader) {
		this.loader = loader;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}
}
