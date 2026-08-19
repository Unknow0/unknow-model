package unknow.model.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.Parameter;

import unknow.model.api.AnnotationModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.ParamModel;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractParamModel;

/**
 * @author unknow
 */
public class AstParam extends AbstractParamModel implements ParamModel {
	private final Parameter p;

	/**
	 * create new AstParam
	 * 
	 * @param loader the loader
	 * @param m the owner
	 * @param p the parameter
	 * @param index the param index
	 */
	public AstParam(ModelLoader loader, MethodModel m, Parameter p, int index) {
		super(loader, m, index);
		this.p = p;
	}

	@Override
	public String name() {
		return p.getNameAsString();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(AstUtils.toBinaryName(p.getType().resolve()), parent().parent().parameters());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return p.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}
}
