package unknow.model.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.EnumConstantDeclaration;

import unknow.model.api.AnnotationModel;
import unknow.model.api.EnumModel;
import unknow.model.api.EnumModel.EnumConstant;
import unknow.model.api.ModelLoader;
import unknow.model.api.impl.AbstractEnumConstant;

public class AstEnumConstant extends AbstractEnumConstant implements EnumConstant {
	private final EnumConstantDeclaration e;

	/**
	 * create new AstEnumConstant
	 * 
	 * @param loader the loader
	 * @param parent the enum
	 * @param e the constant
	 */
	public AstEnumConstant(ModelLoader loader, EnumModel parent, EnumConstantDeclaration e) {
		super(loader, parent);
		this.e = e;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return e.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public String name() {
		return e.getNameAsString();
	}
}