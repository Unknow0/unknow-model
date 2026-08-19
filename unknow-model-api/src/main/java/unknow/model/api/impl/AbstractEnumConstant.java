package unknow.model.api.impl;

import unknow.model.api.EnumModel;
import unknow.model.api.EnumModel.EnumConstant;
import unknow.model.api.ModelLoader;

public abstract class AbstractEnumConstant extends AbstractWithAnnotation implements EnumConstant {
	private final EnumModel parent;

	/**
	 * @param loader the model loader
	 * @param parent enum declaring this constant
	 */
	protected AbstractEnumConstant(ModelLoader loader, EnumModel parent) {
		super(loader);
		this.parent = parent;
	}

	@Override
	public final EnumModel parent() {
		return parent;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + parent().name() + "." + name();
	}
}
