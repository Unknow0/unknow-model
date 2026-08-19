package unknow.model.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.PackageDeclaration;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.impl.AbstractPackageModel;

/**
 * @author unknow
 */
public class AstPackage extends AbstractPackageModel implements PackageModel {
	private final PackageDeclaration p;

	/**
	 * create new AstPackage
	 * 
	 * @param loader the loader
	 * @param p the package
	 */
	public AstPackage(ModelLoader loader, PackageDeclaration p) {
		super(loader, p.getNameAsString());
		this.p = p;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return p.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}
}
