package unknow.model.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unknow.model.test.AbstractTestLoader;

/**
 * @author unknow
 */
public class ModelLoaderTest extends AbstractTestLoader {
	static final Logger logger = LoggerFactory.getLogger(ModelLoaderTest.class);

	protected ModelLoaderTest() {
		super(JvmModelLoader.GLOBAL);
	}
}
