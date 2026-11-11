/**
 * 
 */
package unknow.model.jvm;

import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;

/**
 * @author unknow
 */
public class JvmModelLoader extends ModelLoader {
	/** global jvm loader */
	public static final JvmModelLoader GLOBAL = new JvmModelLoader(JvmModelLoader.class.getClassLoader());

	private final ClassLoader cl;

	/**
	 * create new JvmModelLoader
	 * 
	 * @param cl class loader
	 */
	public JvmModelLoader(ClassLoader cl) {
		this.cl = cl;
	}

	@Override
	protected PackageModel loadPackage(ModelLoader loader, String name) {
		Package p = cl.getDefinedPackage(name);
		return p == null ? null : new JvmPackage(loader, p);
	}

	@Override
	protected TypeModel load(ModelLoader loader, PackageModel parent, String cl, TypeModel[] params) {
		Class<?> c = tryLoad(cl);
		if (c == null)
			return null;
		if (c.isAnnotation())
			return new JvmAnnotationDecl(this, parent, c);
		return new JvmClassOrEnum(this, parent, c, params);
	}

	private Class<?> tryLoad(String clazz) {
		try {
			return cl.loadClass(clazz);
		} catch (@SuppressWarnings("unused") ClassNotFoundException e) {
			return null;
		}
	}
}
