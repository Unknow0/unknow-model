package unknow.model.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.JavaBean;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.processing.Generated;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.FieldModel;
import unknow.model.api.MethodModel;
import unknow.model.api.TypeModel;

@SuppressWarnings("unused")
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

	@Marker
	@DisplayName("test")
	private static abstract class Simple extends Empty implements EmptyInter {
		int field1;
		private List<String> list;

		Simple(int field1) {
			this.field1 = field1;
		}

		public <T> T doStuff() {
			return null;
		}

		static void compute(String s) { //
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
		assertEquals(Empty.class.getName(), cl.superType().name());
		assertEquals(1, cl.interfaces().size());
		assertEquals(EmptyInter.class.getName(), cl.interfaces().iterator().next().name());
		assertEquals(2, cl.annotations().size());
		Optional<AnnotationModel> a = cl.annotation(Marker.class);
		assertTrue(a.isPresent());
		assertTrue(a.get().members().isEmpty());
		a = cl.annotation(DisplayName.class);
		assertTrue(a.isPresent());
		assertEquals("test", a.get().value().map(v -> v.asLiteral()).orElse(null));
		assertEquals(2, cl.fields().size());

		Optional<FieldModel> f = cl.field("field1");
		assertTrue(f.isPresent());
		assertEquals("int", f.get().type().name());
		f = cl.field("list");
		assertTrue(f.isPresent());
		assertClass(f.get().type().asClass(), "java.util.List", "java.lang.String");

		assertEquals(2, cl.methods().size());
		Optional<MethodModel> m = cl.method("doStuff");
		assertTrue(m.isPresent());
		assertTrue(m.get().isPublic());
		assertEquals("java.lang.Object", m.get().type().name());
		m = cl.method("compute", loader.get("java.lang.String"));
		assertTrue(m.isPresent());
		assertTrue(m.get().isPackagePrivate());
		assertTrue(m.get().isStatic());
		assertEquals("void", m.get().type().name());
		assertEquals(1, cl.constructors().size());
		MethodModel c = cl.constructors().iterator().next();
		assertTrue(c.isPackagePrivate());
		assertEquals(1, c.parameters().size());
		assertEquals("int", c.parameter(0).type().name());
	}

	private static abstract class Generic<T extends Number> extends ArrayList<T> implements List<T> {
		private static final long serialVersionUID = 1L;

		public T doStuff() {
			return null;
		}

		static List<String> compute(List<String> s) {
			return s;
		}
	}

	@Test
	void generic() {
		AsmModelLoader loader = new AsmModelLoader(AsmClassTest.class.getClassLoader());
		TypeModel t = loader.load(loader, null, Generic.class.getName(), new TypeModel[0]);
		assertTrue(t.isClass());
		ClassModel cl = t.asClass();
		assertTrue(cl.isPrivate());
		assertFalse(cl.isInterface());
		assertTrue(cl.isAbstract());
		assertEquals(Generic.class.getName() + "<T extends java.lang.Number>", cl.genericName());
		assertEquals(Generic.class.getName(), cl.name());
		assertEquals(ArrayList.class.getName(), cl.superType().name());
		assertEquals(1, cl.interfaces().size());
		assertEquals(List.class.getName() + "<T extends Number>", cl.interfaces().iterator().next().genericName());
		assertTrue(cl.annotations().isEmpty());
		assertEquals(2, cl.fields().size());

		Optional<FieldModel> f = cl.field("field1");
		assertTrue(f.isPresent());
		assertEquals("int", f.get().type().name());
		f = cl.field("list");
		assertTrue(f.isPresent());
		assertClass(f.get().type().asClass(), "java.util.List", "java.lang.String");

		assertEquals(2, cl.methods().size());
		Optional<MethodModel> m = cl.method("doStuff");
		assertTrue(m.isPresent());
		assertTrue(m.get().isPublic());
		assertEquals("java.lang.Object", m.get().type().name());
		m = cl.method("compute", loader.get("java.util.List<java.lang.String>"));
		assertTrue(m.isPresent());
		assertTrue(m.get().isPackagePrivate());
		assertTrue(m.get().isStatic());
		assertEquals("java.util.List<java.lang.String>", m.get().type().toString());
		assertEquals(1, cl.constructors().size());
		MethodModel c = cl.constructors().iterator().next();
		assertTrue(c.isPackagePrivate());
		assertEquals(1, c.parameters().size());
		assertEquals("int", c.parameter(0).type().name());
	}

	void assertClass(ClassModel cl, String name, String... params) {
		assertEquals(name, cl.name());
		assertEquals(params.length, cl.parameters().size());
		for (int i = 0; i < params.length; i++)
			assertEquals(params[i], cl.parameter(i).type().name());
	}

	public @interface Marker { //ok
	}
}
