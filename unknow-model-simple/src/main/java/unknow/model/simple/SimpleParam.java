package unknow.model.simple;

import unknow.model.api.ClassModel;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.WithParent;

public class SimpleParam<T extends WithParent<ClassModel>> extends SimpleWithAnnotation implements ParamModel<T> {
	private final T parent;
	private final String name;
	private final TypeModel type;
	private final int index;

	public SimpleParam(T parent, String name, TypeModel type, int index) {
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
	public T parent() {
		return parent;
	}

	@Override
	public int index() {
		return index;
	}
}
