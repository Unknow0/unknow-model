package unknow.model.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.EnumModel;
import unknow.model.api.FieldModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;
import unknow.model.api.impl.AbstractEnumModel;

/**
 * @author unknow
 * @param <T> class / enum
 */
public class AstClassOrEnum<T extends TypeDeclaration<?>> extends AbstractEnumModel implements ClassModel, EnumModel, AstMod<T> {
	/** the class or enum */
	protected final T c;

	/**
	 * @param loader model loader
	 * @param paramsClass class parameter
	 * @param p package
	 * @param c the class or enum
	 */
	protected AstClassOrEnum(ModelLoader loader, TypeModel[] paramsClass, PackageModel p, T c) {
		super(loader, p, AstUtils.toBinaryName(c), paramsClass);
		this.c = c;
	}

	@Override
	public boolean isInterface() {
		return c.isClassOrInterfaceDeclaration() && c.asClassOrInterfaceDeclaration().isInterface();
	}

	@Override
	public boolean isEnum() {
		return c.isEnumDeclaration();
	}

	@Override
	public T object() {
		return c;
	}

	@Override
	protected ClassModel loadSuperType(ModelLoader loader) {
		if (c.isEnumDeclaration())
			return loader.get("java.lang.Enum<" + name() + ">").asClass();

		ResolvedReferenceTypeDeclaration r = c.resolve();
		return loader.get(r.asClass().getSuperClass().map(v -> AstUtils.toBinaryName(v)).orElse("java.lang.Object"), parameters()).asClass();
	}

	@Override
	protected List<ClassModel> loadInterfaces(ModelLoader loader) {
		NodeList<ClassOrInterfaceType> interfaces;
		if (c.isEnumDeclaration())
			interfaces = c.asEnumDeclaration().getImplementedTypes();
		if (c.isClassOrInterfaceDeclaration()) {
			ClassOrInterfaceDeclaration cl = c.asClassOrInterfaceDeclaration();
			interfaces = (cl.isInterface() ? cl.getExtendedTypes() : cl.getImplementedTypes());
		} else
			return Collections.emptyList();
		return interfaces.stream().map(v -> loader.get(AstUtils.toBinaryName(v.resolve()), parameters()).asClass()).collect(Collectors.toList());
	}

	@Override
	protected List<TypeParamModel> loadParameters(ModelLoader loader, TypeModel[] paramsClass) {
		if (!c.isClassOrInterfaceDeclaration())
			return Collections.emptyList();

		ClassOrInterfaceDeclaration cl = c.asClassOrInterfaceDeclaration();
		int l = cl.getTypeParameters().size();
		List<TypeParamModel> parameters = new ArrayList<>(l);
		for (int i = 0; i < l; i++)
			parameters.add(new AstTypeParam(loader, this, cl.getTypeParameter(i), paramsClass.length == l ? paramsClass[i] : null));
		return parameters;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return c.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	protected Collection<MethodModel> loadConstructors(ModelLoader loader) {
		return c.getConstructors().stream().map(v -> new AstConstructor(loader, this, v)).collect(Collectors.toList());
	}

	@Override
	protected Collection<FieldModel> loadFields(ModelLoader loader) {
		List<FieldModel> fields = new ArrayList<>();
		for (FieldDeclaration f : c.getFields())
			f.getVariables().stream().map(v -> new AstField(loader, this, f, v)).forEach(fields::add);
		return fields;
	}

	@Override
	protected Collection<MethodModel> loadMethods(ModelLoader loader) {
		return c.getMethods().stream().map(m -> new AstMethod(loader, this, m)).collect(Collectors.toList());
	}

	@Override
	protected List<EnumConstant> loadEntries(ModelLoader loader) {
		if (!c.isEnumDeclaration())
			return Collections.emptyList();
		return c.asEnumDeclaration().getEntries().stream().map(v -> new AstEnumConstant(loader, this, v)).collect(Collectors.toList());
	}

}
