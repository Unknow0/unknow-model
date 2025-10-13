package unknow.model.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;

public abstract class AbstractTestLoader {
	private final ModelLoader loader;

	protected AbstractTestLoader(ModelLoader loader) {
		this.loader = loader;
	}

	@Test
	void testParameter() {
		TypeModel col = loader.get(G.class.getName());
		ClassModel slist = loader.get(GString.class.getName()).asClass();
		assertEquals(GString.class.getName(), slist.name());
		assertTrue(col.isAssignableFrom(slist));
		assertEquals("java.lang.String", slist.superType().parameter(0).type().name());
		assertEquals("java.lang.String", slist.superType().field("a").type().name());

		ClassModel ilist = loader.get(GInteger.class.getName()).asClass();
		assertTrue(col.isAssignableFrom(ilist));
		assertEquals("java.lang.Integer", ilist.superType().parameter(0).type().name());
		MethodModel m = ilist.superType().method("m", loader.get("java.lang.Integer")).orElse(null);
		assertEquals("java.lang.Integer", m.type().name());
		assertEquals("java.lang.Integer", m.parameter(0).type().name());
	}
}
