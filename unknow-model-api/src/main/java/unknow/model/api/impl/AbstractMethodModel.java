package unknow.model.api.impl;

import java.util.List;

import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;

public abstract class AbstractMethodModel extends AbstractWithType implements MethodModel {
	private final ClassModel parent;
	private final String name;
	private List<ParamModel> params;

	protected AbstractMethodModel(ModelLoader loader, ClassModel parent, String name) {
		super(loader);
		this.parent = parent;
		this.name = name;
	}

	/**
	 * @param loader the model loader
	 * @return parameters
	 */
	protected abstract List<ParamModel> loadParameters(ModelLoader loader);

	@Override
	public final ClassModel parent() {
		return parent;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final List<ParamModel> parameters() {
		if (params == null)
			params = loadParameters(loader);
		return params;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + parent.name() + "." + signature();
	}
}
