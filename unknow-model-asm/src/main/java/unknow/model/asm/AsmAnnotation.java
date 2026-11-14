package unknow.model.asm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.AnnotationValue.AnnotationValueAnnotation;
import unknow.model.api.AnnotationValue.AnnotationValueArray;
import unknow.model.api.AnnotationValue.AnnotationValueClass;
import unknow.model.api.AnnotationValue.AnnotationValueLiteral;
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
		if (a.values == null || a.values.isEmpty())
			return Collections.emptyList();
		List<AnnotationMemberModel> members = new ArrayList<>();
		for (int i = 0; i < a.values.size(); i += 2) {
			String name = (String) a.values.get(i);
			Object value = a.values.get(i + 1);
			members.add(new AnnotationMemberModel(name, value(loader, value)));
		}
		return members;
	}

	private static AnnotationValue value(ModelLoader loader, Object v) {
		if (v instanceof Type) {
			Type type = (Type) v;
			return new AnnotationValueClass(loader.get(type.getClassName()));
		}
		if (v instanceof String[]) // Valeur enum : [descripteur de l'enum, nom de la constante]
			return new AnnotationValueLiteral(((String[]) v)[1]);
		if (v instanceof AnnotationNode)
			return new AnnotationValueAnnotation(new AsmAnnotation(loader, (AnnotationNode) v));
		if (v instanceof List<?>) {
			List<?> values = (List<?>) v;
			AnnotationValue[] array = new AnnotationValue[values.size()];
			for (int i = 0; i < values.size(); i++)
				array[i] = value(loader, values.get(i));
			return new AnnotationValueArray(array);
		}
		if (v instanceof Character)
			return new AnnotationValueLiteral(v.toString());

		// Primitives boxés et String
		if (v instanceof Number || v instanceof Boolean || v instanceof String)
			return new AnnotationValueLiteral(v.toString());

		throw new IllegalArgumentException("Unsupported annotation value: " + v);
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
