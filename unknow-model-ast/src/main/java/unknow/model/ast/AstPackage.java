package unknow.model.ast;

import java.util.Collection;
import java.util.stream.Collectors;

import com.github.javaparser.ast.PackageDeclaration;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;

/**
 * @author unknow
 */
public class AstPackage implements PackageModel {
	private final ModelLoader loader;
	private final PackageDeclaration p;
	private Collection<AnnotationModel> annotations;

	/**
	 * create new AstPackage
	 * 
	 * @param loader the loader
	 * @param p the package
	 */
	public AstPackage(ModelLoader loader, PackageDeclaration p) {
		this.loader = loader;
		this.p = p;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = p.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
		return annotations;
	}

	@Override
	public String name() {
		return p.getNameAsString();
	}

}
