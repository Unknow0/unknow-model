/**
 * 
 */
package unknow.model.jvm;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.PackageModel;

/**
 * @author unknow
 */
public class JvmPackage implements PackageModel {
	private final JvmModelLoader loader;
	private final Package p;
	private Collection<AnnotationModel> annotations;

	/**
	 * create new JvmPackage
	 * 
	 * @param loader the loader
	 * @param p the package
	 */
	public JvmPackage(JvmModelLoader loader, Package p) {
		this.loader = loader;
		this.p = p;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = Arrays.stream(p.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
		return annotations;
	}

	@Override
	public String name() {
		return p.getName();
	}

}
