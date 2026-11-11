package unknow.model.asm;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractFieldModel;

public class AsmField extends AbstractFieldModel implements AsmAccess {
	private final FieldNode f;

	protected AsmField(ModelLoader loader, ClassModel cl, FieldNode f) {
		super(loader, cl);
		this.f = f;
	}

	@Override
	public int access() {
		return f.access;
	}

	@Override
	public AnnotationValue value(ModelLoader loader) {
		if (f.value == null)
			return AnnotationValue.NULL;
		return new AnnotationValue.AnnotationValueLiteral(String.valueOf(f.value));
	}

	@Override
	public String name() {
		return f.name;
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(Type.getType(f.desc).getClassName());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return AsmAnnotation.from(loader, f.invisibleAnnotations, f.visibleAnnotations);
	}
}
