package unknow.model.simple;

import java.util.ArrayList;
import java.util.List;

import unknow.model.api.AnnotationValue;
import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.jvm.JvmMod;

public class SimpleMethod extends SimpleWithAnnotation implements MethodModel, JvmMod {
	private final ClassModel parent;
	private final String name;
	private final int mod;
	private final TypeModel type;
	private final List<ParamModel<MethodModel>> params;

	public SimpleMethod(ClassModel parent, String name, int mod, TypeModel type) {
		this.parent = parent;
		this.name = name;
		this.mod = mod;
		this.type = type;
		this.params = new ArrayList<>(0);
	}

	public SimpleParam<MethodModel> withParam(String name, TypeModel type) {
		SimpleParam<MethodModel> p = new SimpleParam<>(this, name, type, params.size());
		params.add(p);
		return p;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int mod() {
		return mod;
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
	public List<ParamModel<MethodModel>> parameters() {
		return params;
	}

	@Override
	public AnnotationValue defaultValue() {
		return null;
	}
}
