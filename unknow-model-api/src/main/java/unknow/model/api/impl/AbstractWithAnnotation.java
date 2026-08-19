package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.WithAnnotation;

public abstract class AbstractWithAnnotation implements WithAnnotation {
	protected final ModelLoader loader;
	private Collection<AnnotationModel> annotations;

	protected AbstractWithAnnotation(ModelLoader loader) {
		this.loader = loader;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null) {
			annotations = loadAnnotations(loader);
		}
		return annotations;
	}
}
