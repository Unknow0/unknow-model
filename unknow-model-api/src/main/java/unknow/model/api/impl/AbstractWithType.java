package unknow.model.api.impl;

import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.WithType;

public abstract class AbstractWithType extends AbstractWithAnnotation implements WithType {
	private TypeModel type;

	protected AbstractWithType(ModelLoader loader) {
		super(loader);
	}

	/**
	 * @param loader the model loader
	 * @return type
	 */
	protected abstract TypeModel loadType(ModelLoader loader);

	@Override
	public final TypeModel type() {
		if (type == null)
			type = loadType(loader);
		return type;
	}
}
