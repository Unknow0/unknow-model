package unknow.model.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;

/**
 * @author unknow
 */
public class AstMethod implements MethodModel, AstMod<MethodDeclaration> {
	private final ClassModel parent;
	private final ModelLoader loader;
	private final MethodDeclaration m;
	private Collection<AnnotationModel> annotations;
	private TypeModel type;
	private List<ParamModel<MethodModel>> params;

	/**
	 * create new AstMethod
	 * 
	 * @param parent the class owning the method
	 * @param loader the loader
	 * @param m the method
	 */
	public AstMethod(ClassModel parent, ModelLoader loader, MethodDeclaration m) {
		this.parent = parent;
		this.loader = loader;
		this.m = m;
	}

	@Override
	public ClassModel parent() {
		return parent;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null) {
			annotations = m.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
		}
		return annotations;
	}

	@Override
	public MethodDeclaration object() {
		return m;
	}

	@Override
	public String name() {
		return m.getNameAsString();
	}

	@Override
	public String toString() {
		return parent.name() + "." + signature();
	}

	@Override
	public TypeModel type() {
		if (type == null)
			type = loader.get(m.getType().resolve().describe(), parent.parameters());
		return type;
	}

	@Override
	public List<ParamModel<MethodModel>> parameters() {
		if (params == null) {
			int i = 0;
			params = new ArrayList<>();
			for (Parameter p : m.getParameters())
				params.add(new AstParam<>(loader, this, p, i++));
		}
		return params;
	}

	@Override
	public AnnotationValue defaultValue() {
		return AnnotationValue.NULL;
	}
}
