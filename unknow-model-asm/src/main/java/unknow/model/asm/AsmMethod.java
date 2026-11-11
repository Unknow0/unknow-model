package unknow.model.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractMethodModel;

public class AsmMethod extends AbstractMethodModel implements AsmAccess {
	private final MethodNode m;

	public AsmMethod(ModelLoader loader, ClassModel parent, MethodNode m) {
		super(loader, parent, m.name);
		this.m = m;
	}

	@Override
	public int access() {
		return m.access;
	}

	@Override
	protected List<ParamModel> loadParameters(ModelLoader loader) {
		Type[] paramTypes = Type.getArgumentTypes(m.desc);
		if (paramTypes.length == 0)
			return Collections.emptyList();
		List<ParamModel> list = new ArrayList<>(paramTypes.length);
		for (int i = 0; i < paramTypes.length; i++)
			list.add(new AsmParam(loader, this, i, m));
		return null;
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(Type.getReturnType(m.desc).getClassName());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return AsmAnnotation.from(loader, m.invisibleAnnotations, m.visibleAnnotations);
	}
}
