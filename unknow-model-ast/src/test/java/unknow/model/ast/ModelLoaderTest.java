package unknow.model.ast;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unknow.model.api.ModelLoader;
import unknow.model.jvm.JvmModelLoader;
import unknow.model.test.AbstractTestLoader;

/**
 * @author unknow
 */
public class ModelLoaderTest extends AbstractTestLoader {
	static final Logger logger = LoggerFactory.getLogger(ModelLoaderTest.class);

	protected ModelLoaderTest() {
		super(loader());
	}

	private static ModelLoader loader() {
		return ModelLoader.from(new AstModelLoader(Path.of(System.getProperty("basedir"), "..", "unknow-model-test", "src", "main", "java")), JvmModelLoader.GLOBAL);
	}
}
