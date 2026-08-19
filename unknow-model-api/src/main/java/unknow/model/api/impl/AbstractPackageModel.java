package unknow.model.api.impl;

import java.util.Collections;
import java.util.List;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;

public abstract class AbstractPackageModel extends AbstractWithAnnotation implements PackageModel {
	private final String name;

	/**
	 * @param loader the model loader
	 */
	protected AbstractPackageModel(ModelLoader loader, String name) {
		super(loader);
		this.name = name;
	}

	@Override
	public final String name() {
		return name;
	}

	public static class EmptyPackageModel extends AbstractPackageModel {
		/**
		 * @param name the package name
		 */
		public EmptyPackageModel(String name) {
			super(null, name);
		}

		@Override
		protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
			return Collections.emptyList();
		}
	}
}
