package unknow.model.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractFieldModel;

/**
 * @author unknow
 */
public class AstField extends AbstractFieldModel implements FieldModel, AstMod<FieldDeclaration> {
	private final FieldDeclaration f;
	private final VariableDeclarator v;

	/**
	 * create new AstField
	 * 
	 * @param loader the loader
	 * @param cl the class owning the field
	 * @param f the field declaration (can contains multiple variable)
	 * @param v the variable
	 */
	public AstField(ModelLoader loader, ClassModel cl, FieldDeclaration f, VariableDeclarator v) {
		super(loader, cl);
		this.f = f;
		this.v = v;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return f.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	public FieldDeclaration object() {
		return f;
	}

	@Override
	public String name() {
		return v.getNameAsString();
	}

	@Override
	protected TypeModel loadType(ModelLoader loader) {
		return loader.get(AstUtils.toBinaryName(v.getType().resolve()), parent().parameters());
	}

	@Override
	public AnnotationValue value(ModelLoader loader) {
		return AstAnnotation.value(loader, f.getVariable(0).getInitializer().orElseThrow(() -> new IllegalArgumentException("Can't find value for " + this)));
	}
}
