package unknow.model.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractMethodModel;

/**
 * @author unknow
 */
public class AstMethod extends AbstractMethodModel implements MethodModel, AstMod<MethodDeclaration> {
	private final MethodDeclaration m;

	/**
	 * create new AstMethod
	 * 
	 * @param parent the class owning the method
	 * @param loader the loader
	 * @param m the method
	 */
	public AstMethod(ModelLoader loader, ClassModel parent, MethodDeclaration m) {
		super(loader, parent, m.getNameAsString());
		this.m = m;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return m.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public boolean isPublic() {
		if (parent().isInterface())
			return true;
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.PUBLIC);
	}

	@Override
	public MethodDeclaration object() {
		return m;
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(AstUtils.toBinaryName(m.getType().resolve()), parent().parameters());
	}

	@Override
	protected List<ParamModel> loadParameters(ModelLoader loader) {
		int i = 0;
		List<ParamModel> params = new ArrayList<>();
		for (Parameter p : m.getParameters())
			params.add(new AstParam(loader, this, p, i++));
		return params;
	}

//	@Override
//	public AnnotationValue defaultValue() {
//		return AnnotationValue.NULL;
//	}
}
