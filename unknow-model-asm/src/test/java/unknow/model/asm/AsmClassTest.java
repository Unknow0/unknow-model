package unknow.model.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import unknow.model.api.ClassModel;
import unknow.model.api.MethodModel;
import unknow.model.api.TypeModel;

public class AsmClassTest {

	public static class Empty { // ok
	}

	@Test
	void empty() {
		AsmModelLoader loader = new AsmModelLoader(AsmClassTest.class.getClassLoader());
		TypeModel t = loader.load(loader, null, Empty.class.getName(), new TypeModel[0]);
		assertTrue(t.isClass());
		ClassModel cl = t.asClass();
		assertTrue(cl.isPublic());
		assertFalse(cl.isAbstract());
		assertFalse(cl.isInterface());
		assertEquals(Empty.class.getName(), cl.genericName());
		assertEquals(Empty.class.getName(), cl.name());
		assertEquals("java.lang.Object", cl.superType().name());
		assertTrue(cl.interfaces().isEmpty());
		assertTrue(cl.annotations().isEmpty());
		assertTrue(cl.fields().isEmpty());
		assertTrue(cl.methods().isEmpty());
		assertEquals(1, cl.constructors().size());
		MethodModel c = cl.constructors().iterator().next();
		assertTrue(c.isPublic());
		assertTrue(c.parameters().isEmpty());
	}

	static interface EmptyInter { // ok
	}

	@Test
	void emptyInterface() {
		AsmModelLoader loader = new AsmModelLoader(AsmClassTest.class.getClassLoader());
		TypeModel t = loader.load(loader, null, EmptyInter.class.getName(), new TypeModel[0]);
		assertTrue(t.isClass());
		ClassModel cl = t.asClass();
		assertTrue(cl.isPackagePrivate());
		assertTrue(cl.isInterface());
		assertTrue(cl.isAbstract());
		assertEquals(EmptyInter.class.getName(), cl.genericName());
		assertEquals(EmptyInter.class.getName(), cl.name());
		assertEquals(null, cl.superType());
		assertTrue(cl.interfaces().isEmpty());
		assertTrue(cl.annotations().isEmpty());
		assertTrue(cl.fields().isEmpty());
		assertTrue(cl.methods().isEmpty());
		assertTrue(cl.constructors().isEmpty());
	}

	private static abstract class Simple {
		int field1;
		private List<String> list;

		public <T> T doStuff() {
			return null;
		}

		static void compute(String s) {
		}
	}

	@Test
	void simple() {
		AsmModelLoader loader = new AsmModelLoader(AsmClassTest.class.getClassLoader());
		TypeModel t = loader.load(loader, null, Simple.class.getName(), new TypeModel[0]);
		assertTrue(t.isClass());
		ClassModel cl = t.asClass();
		assertTrue(cl.isPrivate());
		assertFalse(cl.isInterface());
		assertTrue(cl.isAbstract());
		assertEquals(Simple.class.getName(), cl.genericName());
		assertEquals(Simple.class.getName(), cl.name());
		assertEquals("java.lang.Object", cl.superType().name());
		assertTrue(cl.interfaces().isEmpty());
		assertTrue(cl.annotations().isEmpty());
		assertTrue(cl.fields().isEmpty());
		assertTrue(cl.methods().isEmpty());
		assertEquals(1, cl.constructors().size());
		MethodModel c = cl.constructors().iterator().next();
		assertTrue(c.isPublic());
		assertTrue(c.parameters().isEmpty());
	}
}
