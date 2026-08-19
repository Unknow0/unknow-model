package unknow.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.TypeParameter;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;
import unknow.model.api.impl.AbstractTypeParamModel;

public class AstTypeParam extends AbstractTypeParamModel implements TypeParamModel {
	private final TypeParameter p;

	/**
	 * create new AstParam
	 * 
	 * @param loader the loader
	 * @param c      the owner
	 * @param p      the parameter
	 * @param type   the type
	 */
	public AstTypeParam(ModelLoader loader, ClassModel c, TypeParameter p, TypeModel type) {
		super(loader, c, type);
		this.p = p;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return p.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public String name() {
		return p.getNameAsString();
	}

	@Override
	protected List<ClassModel> loadBounds(ModelLoader loader) {
		if (p.getTypeBound() == null)
			return Collections.emptyList();
		return p.getTypeBound().stream().map(t -> loader.get(AstUtils.toBinaryName(t.resolve()), parent().parameters()).asClass()).collect(Collectors.toList());
	}
}
