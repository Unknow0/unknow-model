package unknow.model.asm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import unknow.model.api.AnnotationModel;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;

class AsmClassParamReaderTest {
	static Stream<Arguments> parse() {
		T t1 = new T();
		T t2 = new T();
		AsmTypeParam k = new AsmTypeParam(null, null, t1, "K", List.of("java.lang.Object"));
		AsmTypeParam v = new AsmTypeParam(null, null, t2, "V", List.of("unknow.Foo$Inner<K,V>"));
		TypeModel[] empty = {};
		TypeModel[] params = { t1, t2 };
		return Stream.of( //
				Arguments.of(Collections.emptyList(), empty, null), //
				Arguments.of(Collections.emptyList(), empty, "Ljava/lang/Object;"), //
				Arguments.of(List.of(new AsmTypeParam(null, null, null, "T", List.of("java.lang.Object"))), empty, "<T:Ljava/lang/Object;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("java.lang.Object"))), params, "<T:Ljava/lang/Object;>Ljava/lang/Object;"),
				Arguments.of(List.of(k, v), params, "<K:Ljava/lang/Object;V:Lunknow/Foo$Inner<TK;TV;>;>Ljava/lang/Object;Ljava/util/List<[[I>;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("Map<Number,?>"))), params, "<T::LMap<LNumber;*>;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("java.util.List<? extends java.lang.Number>"))), params,
						"<T::Ljava/util/List<+Ljava/lang/Number;>;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("List<? extends ArrayList<? super Number>>"))), params,
						"<T::LList<+LArrayList<-LNumber;>;>;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("java.lang.Number", "java.io.Serializable", "java.lang.Comparable<T>"))), params,
						"<T:Ljava/lang/Number;:Ljava/io/Serializable;" + ":Ljava/lang/Comparable<TT;>;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("java.util.List<java.lang.String[]>"))), params,
						"<T::Ljava/util/List<[Ljava/lang/String;>;>Ljava/lang/Object;"),
				Arguments.of(List.of(new AsmTypeParam(null, null, t1, "T", List.of("pkg.Outer<T>$Inner<T>"))), params, "<T:Lpkg/Outer<TT;>.Inner<TT;>;>Ljava/lang/Object;"));
	}

	@ParameterizedTest
	@MethodSource
	void parse(List<AsmTypeParam> expected, TypeModel[] params, String sig) {
		List<TypeParamModel> list = AsmSignatureReader.parse(null, null, params, sig);
		assertEquals(expected.size(), list.size());
		for (int i = 0; i < expected.size(); i++) {
			AsmTypeParam e = expected.get(i);
			AsmTypeParam l = (AsmTypeParam) list.get(i);
			assertEquals(e.name, l.name);
			assertEquals(e.bounds, l.bounds);
			assertEquals(e.type(), l.type());
		}
	}

	static Stream<Arguments> parseType() {
		return Stream.of(Arguments.of("T", "TT;"), Arguments.of("T[]", "[TT;"), Arguments.of("String", "LString;"), Arguments.of("int", "I"),
				Arguments.of("java.util.List<java.lang.String>", "Ljava/util/List<Ljava/lang/String;>;"));
	}

	@ParameterizedTest
	@MethodSource
	void parseType(String expected, String sig) {
		assertEquals(expected, AsmSignatureReader.parseType(sig));
	}

	static class T implements TypeModel {

		@Override
		public Collection<AnnotationModel> annotations() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String name() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}