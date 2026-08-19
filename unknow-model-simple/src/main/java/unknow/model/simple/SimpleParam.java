package unknow.model.simple;

import unknow.model.api.MethodModel;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;

public class SimpleParam extends SimpleWithAnnotation implements ParamModel {
	private final MethodModel parent;
	private final String name;
	private final TypeModel type;
	private final int index;

	public SimpleParam(MethodModel parent, String name, TypeModel type, int index) {
		this.parent = parent;
		this.name = name;
		this.type = type;
		this.index = index;
	}

	@Override
	public TypeModel type() {
		return type;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public MethodModel parent() {
		return parent;
	}

	@Override
	public int index() {
		return index;
	}
}
