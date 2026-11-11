package unknow.model.asm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.impl.AbstractAnnotationModel;

public class AsmAnnotation extends AbstractAnnotationModel {
	private final AnnotationNode a;

	protected AsmAnnotation(ModelLoader loader, AnnotationNode a) {
		super(loader, Type.getType(a.desc).getClassName());
		this.a = a;
	}

	@Override
	protected Collection<AnnotationMemberModel> loadMembers(ModelLoader loader) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void convert(ModelLoader loader, Collection<AnnotationNode> annotations, Collection<AnnotationModel> list) {
		if (annotations == null)
			return;
		for (AnnotationNode a : annotations)
			list.add(new AsmAnnotation(loader, a));
	}

	public static List<AnnotationModel> from(ModelLoader loader, Collection<AnnotationNode> visible, Collection<AnnotationNode> invisible) {
		if (invisible == null & visible == null)
			return Collections.emptyList();
		List<AnnotationModel> list = new ArrayList<>();
		AsmAnnotation.convert(loader, invisible, list);
		AsmAnnotation.convert(loader, visible, list);
		return list;
	}
}
