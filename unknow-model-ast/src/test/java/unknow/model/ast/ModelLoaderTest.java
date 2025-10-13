package unknow.model.ast;

import java.io.IOException;
import java.nio.file.Paths;

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

	protected ModelLoaderTest() throws IOException {
		super(loader());
	}

	private static ModelLoader loader() throws IOException {
		return ModelLoader.from(AstModelLoaderBuilder.build(Paths.get(System.getProperty("basedir"), "..", "unknow-model-test", "src", "main", "java")),
				JvmModelLoader.GLOBAL);
	}
}
