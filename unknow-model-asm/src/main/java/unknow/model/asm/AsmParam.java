package unknow.model.asm;

import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;

import unknow.model.api.AnnotationModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractParamModel;

public class AsmParam extends AbstractParamModel {
	private final MethodNode m;

	public AsmParam(ModelLoader loader, MethodModel parent, int i, MethodNode m) {
		super(loader, parent, i);
		this.m = m;
	}

	@Override
	public String name() {
		if (m.parameters == null)
			return "arg" + index();
		return m.parameters.get(index()).name;
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(Type.getArgumentTypes(m.desc)[index()].getClassName());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		int i = index();
		if (m.invisibleAnnotableParameterCount != 0)
			i = i + m.invisibleAnnotableParameterCount - m.parameters.size();
		if (i < 0)
			return Collections.emptyList();
		int total = m.parameters.size();
		;
		List<AnnotationNode> invisible = annotations(m.invisibleParameterAnnotations, m.invisibleAnnotableParameterCount, index(), total);
		List<AnnotationNode> visible = annotations(m.visibleParameterAnnotations, m.visibleAnnotableParameterCount, index(), total);
		return AsmAnnotation.from(loader, invisible, visible);
	}

	private static List<AnnotationNode> annotations(List<AnnotationNode>[] annotations, int annotableCount, int index, int totalParams) {
		if (annotations == null)
			return null;
		if (annotableCount > 0)
			index += annotableCount - totalParams;
		if (index < 0)
			return null;
		return index < 0 ? null : annotations[index];
	}
}
