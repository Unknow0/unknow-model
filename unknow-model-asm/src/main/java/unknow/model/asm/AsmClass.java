package unknow.model.asm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;
import unknow.model.api.impl.AbstractClassModel;

public class AsmClass extends AbstractClassModel implements ClassModel, AsmAccess {
	private final ClassNode clazz;

	public AsmClass(ModelLoader loader, PackageModel parent, ClassNode clazz, TypeModel[] paramsClass) {
		super(loader, parent, clazz.name.replace('/', '.'), paramsClass);
		this.clazz = clazz;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return AsmAnnotation.from(loader, clazz.invisibleAnnotations, clazz.visibleAnnotations);
	}

	@Override
	public int access() {
		return clazz.access;
	}

	@Override
	public boolean isInterface() {
		return (clazz.access & Opcodes.ACC_INTERFACE) != 0;
	}

	@Override
	protected ClassModel loadSuperType(ModelLoader loader) {
		if (clazz.superName == null)
			return this;
		return loader.get(clazz.superName.replace('/', '.')).asClass();
	}

	@Override
	protected List<ClassModel> loadInterfaces(ModelLoader loader) {
		return clazz.interfaces.stream().map(t -> loader.get(t.replace('/', '.')).asClass()).collect(Collectors.toList());
	}

	@Override
	protected Collection<FieldModel> loadFields(ModelLoader loader) {
		clazz.fields.stream().map(f -> new AsmField(loader, this, f));
		return null;
	}

	@Override
	protected Collection<MethodModel> loadConstructors(ModelLoader loader) {
		return clazz.methods.stream().filter(m -> "<init>".equals(m.name)).map(m -> new AsmMethod(loader, this, m)).collect(Collectors.toList());
	}

	@Override
	protected Collection<MethodModel> loadMethods(ModelLoader loader) {
		return clazz.methods.stream().filter(m -> m.name.charAt(0) != '<').map(m -> new AsmMethod(loader, this, m)).collect(Collectors.toList());
	}

	@Override
	protected List<TypeParamModel> loadParameters(ModelLoader loader, TypeModel[] paramsClass) {
		if (clazz.signature == null)
			return Collections.emptyList();
		SignatureReader reader = new SignatureReader(clazz.signature);
		return null;
	}

	private static class ParamReader extends SignatureVisitor {
		private final List<TypeParamModel> list;

		private String currentName;
		private List<String> currentBounds;

		protected ParamReader() {
			super(Opcodes.ASM9);
			this.list = new ArrayList<>();
		}

		public static List<TypeParamModel> parse(String signature) {
			if (signature == null)
				return Collections.emptyList();
			ParamReader r = new ParamReader();
			new SignatureReader(signature).accept(r);
			return r.list;
		}

		@Override
		public void visitFormalTypeParameter(String name) {
			flush();
			currentName = name;
			currentBounds = new ArrayList<>();
		}

		@Override
		public SignatureVisitor visitSuperclass() {
			flush();
			return this;
		}

		private void flush() {
			if (currentName != null) {
//                list.add(new TypeParam(currentName, currentBounds));
			}
		}
	}
}
