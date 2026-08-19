package unknow.model.simple;

import java.util.ArrayList;
import java.util.List;

import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;

public class SimpleMethod extends SimpleWithMod implements MethodModel {
	private final ClassModel parent;
	private final String name;
	private final TypeModel type;
	private final List<ParamModel> params;

	public SimpleMethod(ClassModel parent, String name, int mod, TypeModel type) {
		super(mod);
		this.parent = parent;
		this.name = name;
		this.type = type;
		this.params = new ArrayList<>(0);
	}

	public SimpleParam withParam(String name, TypeModel type) {
		SimpleParam p = new SimpleParam(this, name, type, params.size());
		params.add(p);
		return p;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public TypeModel type() {
		return type;
	}

	@Override
	public ClassModel parent() {
		return parent;
	}

	@Override
	public List<ParamModel> parameters() {
		return params;
	}
}
