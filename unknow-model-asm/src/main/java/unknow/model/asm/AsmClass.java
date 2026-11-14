package unknow.model.asm;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InnerClassNode;

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
		return access(clazz);
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
		return clazz.fields.stream().map(f -> new AsmField(loader, this, f)).collect(Collectors.toList());
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
		return AsmSignatureReader.parse(loader, this, paramsClass, clazz.signature);
	}

	static int access(ClassNode clazz) {
		for (InnerClassNode inner : clazz.innerClasses) {
			if (clazz.name.equals(inner.name))
				return inner.access;
		}
		return clazz.access;
	}
}
