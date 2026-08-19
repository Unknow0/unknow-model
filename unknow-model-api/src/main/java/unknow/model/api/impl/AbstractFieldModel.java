package unknow.model.api.impl;

import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.ModelLoader;

public abstract class AbstractFieldModel extends AbstractWithType implements FieldModel {
	private final ClassModel cl;

	protected AbstractFieldModel(ModelLoader loader, ClassModel cl) {
		super(loader);
		this.cl = cl;
	}

	@Override
	public final ClassModel parent() {
		return cl;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + type() + "." + name();
	}

}
