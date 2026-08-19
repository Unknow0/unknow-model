package unknow.model.api.impl;

import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;

public abstract class AbstractParamModel extends AbstractWithType implements ParamModel {
	private final MethodModel parent;
	private final int index;

	protected AbstractParamModel(ModelLoader loader, MethodModel parent, int index) {
		super(loader);
		this.parent = parent;
		this.index = index;
	}

	@Override
	public final MethodModel parent() {
		return parent;
	}

	@Override
	public final int index() {
		return index;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + name();
	}
}
