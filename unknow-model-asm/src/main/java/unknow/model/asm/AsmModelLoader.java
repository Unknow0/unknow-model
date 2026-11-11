
package unknow.model.asm;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;

public class AsmModelLoader extends ModelLoader {
	private static final Logger logger = LoggerFactory.getLogger(AsmModelLoader.class);

	private final ClassLoader classLoader;

	public AsmModelLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	protected PackageModel loadPackage(ModelLoader loader, String name) {
		ClassNode node = new ClassNode();
		try (InputStream is = classLoader.getResourceAsStream(name.replace('.', '/') + "package-info.class")) {
			if (is == null)
				return null;
			new ClassReader(is).accept(node, 0);
			return new AsmPackage(loader, node, name);
		} catch (IOException e) {
			logger.warn("Failed to parse package-info {}", name, e);
			return null;
		}
	}

	@Override
	protected TypeModel load(ModelLoader loader, PackageModel parent, String cl, TypeModel[] params) {
		ClassNode node = new ClassNode();
		try (InputStream is = classLoader.getResourceAsStream(cl.replace('.', '/') + ".class")) {
			if (is == null)
				return null;
			new ClassReader(is).accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
			return new AsmClass(loader, parent, node, params);
		} catch (IOException e) {
			logger.warn("Failed to parse class {}", cl, e);
			return null;
		}
	}
}