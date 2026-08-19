/**
 * 
 */
package unknow.model.jvm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.impl.AbstractPackageModel;

/**
 * @author unknow
 */
public class JvmPackage extends AbstractPackageModel implements PackageModel {
	private final Package p;

	/**
	 * create new JvmPackage
	 * 
	 * @param loader the loader
	 * @param p the package
	 */
	public JvmPackage(ModelLoader loader, Package p) {
		super(loader, p.getName());
		this.p = p;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(p.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}
}
