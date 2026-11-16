package unknow.model.asm;

import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.impl.AbstractAnnotationDeclModel;
import unknow.model.api.impl.AbstractAnnotationMemberDeclModel;

public class AsmAnnotationDecl extends AbstractAnnotationDeclModel implements AsmAccess {
	private final ClassNode clazz;

	public AsmAnnotationDecl(ModelLoader loader, PackageModel parent, ClassNode clazz) {
		super(loader, parent, clazz.name.replace('/', '.'));
		this.clazz = clazz;
	}

	@Override
	public int access() {
		return AsmClass.access(clazz);
	}

	@Override
	protected List<AnnotationMemberDeclModel> loadMembers(ModelLoader loader) {
		return clazz.methods.stream().map(m -> new AsmAnnotationMember(loader, this, m)).collect(Collectors.toList());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return AsmAnnotation.from(loader, clazz.invisibleAnnotations, clazz.visibleAnnotations);
	}

	public static class AsmAnnotationMember extends AbstractAnnotationMemberDeclModel {
		private final MethodNode m;

		public AsmAnnotationMember(ModelLoader loader, AnnotationDeclModel parent, MethodNode m) {
			super(loader, parent, m.name);
			this.m = m;
		}

		@Override
		protected AnnotationValue loadValue(ModelLoader loader) {
			return AsmAnnotation.value(loader, m.annotationDefault);
		}

		@Override
		protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
			return AsmAnnotation.from(loader, m.invisibleAnnotations, m.visibleAnnotations);
		}
	}
}
