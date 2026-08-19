package unknow.model.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.ConstructorDeclaration;
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
public class AstConstructor extends AbstractMethodModel implements MethodModel, AstMod<ConstructorDeclaration> {
	private final ConstructorDeclaration c;

	/**
	 * create new AstMethod
	 * 
	 * @param parent the class owning the constructor
	 * @param loader the loader
	 * @param c the constructor
	 */
	public AstConstructor(ModelLoader loader, ClassModel parent, ConstructorDeclaration c) {
		super(loader, parent, "<init>");
		this.c = c;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return c.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public ConstructorDeclaration object() {
		return c;
	}

	@Override
	protected List<ParamModel> loadParameters(ModelLoader loader) {
		int i = 0;
		List<ParamModel> params = new ArrayList<>();
		for (Parameter p : c.getParameters())
			params.add(new AstParam(loader, this, p, i++));
		return params;
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return null;
	}
}
