package unknow.model.asm;

import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.tree.ClassNode;

public class Test {
	public static void main(String[] arg) throws Exception {
// interface List<E> extends Collection<E> 		<E:Ljava/lang/Object;>Ljava/lang/Object;Ljava/util/Collection<TE;>;
// ArrayList<E> extends AbstractList<E> implements List<E>,RandomAccess,Clonable,Serializable
// <E:Ljava/lang/Object;>Ljava/util/AbstractList<TE;>;Ljava/util/List<TE;>;Ljava/util/RandomAccess;Ljava/lang/Cloneable;Ljava/io/Serializable;

		// Foo extends ArrayList<Foo>
		// Ljava/util/ArrayList<Lunknow/model/asm/Foo;>;
		// Foo extends ArrayList<List<? super Foo>>
		// Ljava/util/ArrayList<Ljava/util/List<-Lunknow/model/asm/Foo;>;>;

		// Foo<T>
		// <T:Ljava/lang/Object;>Ljava/lang/Object;
		// Foo<T extends Number & Comparable<T> & Serializable>
		// <T:Ljava/lang/Number;:Ljava/lang/Comparable<TT;>;:Ljava/io/Serializable;>Ljava/lang/Object;
		// Foo<K, V extends Foo<K,V>> implements List<int[][]>
		// "<K:Ljava/lang/Object;V:Lunknow/model/asm/Foo<TK;TV;>;>Ljava/lang/Object;Ljava/util/List<[[I>;"

		ClassNode node = new ClassNode();
		try (InputStream is = Test.class.getResourceAsStream("/unknow/model/asm/Foo.class")) {
			new ClassReader(is).accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
			System.out.println(node.signature);
		}

//		TraceSignatureVisitor v=new TraceSignatureVisitor();

		ParamReader r = new ParamReader();
		new SignatureReader("<K:Ljava/lang/Object;V:Lunknow/model/asm/Foo<TK;TV;>;>Ljava/lang/Object;Ljava/util/List<[[I>;").accept(r);
		r.flush();
	}

	private static class ParamReader extends SignatureVisitor {
		private static final int PARAM = 1;
		private static final int SUPER = 2;
		private static final int INTERFACE = 3;

		private final StringBuilder sb;
		private int type;
		private int arrayDepth;
		private int genericStack;

		protected ParamReader() {
			super(Opcodes.ASM9);
			this.sb = new StringBuilder();
		}

		public void flush() {
			if (type == 0)
				return;

			String t = type == INTERFACE ? "implements" : (type == SUPER ? "extends" : (type == PARAM ? "<>" : "unknown"));
			System.out.println("flush: " + t + " " + sb);
			sb.setLength(0);
		}

		private void endType() {
			while (arrayDepth > 0) {
				sb.append("[]");
				arrayDepth--;
			}
		}

		@Override
		public void visitFormalTypeParameter(String name) {
			flush();
			System.out.println("visitFormalTypeParameter " + name);
			type = PARAM;
			// TODO replace name with read value from array?
			sb.append(name);
		}

		@Override
		public void visitBaseType(char descriptor) {
			switch (descriptor) {
				case 'V':
					sb.append("void");
					break;
				case 'B':
					sb.append("byte");
					break;
				case 'J':
					sb.append("long");
					break;
				case 'Z':
					sb.append("boolean");
					break;
				case 'I':
					sb.append("int");
					break;
				case 'S':
					sb.append("short");
					break;
				case 'C':
					sb.append("char");
					break;
				case 'F':
					sb.append("float");
					break;
				case 'D':
					sb.append("double");
					break;
				default:
					throw new IllegalArgumentException();
			}
			endType();
		}

		@Override
		public SignatureVisitor visitSuperclass() {
			flush();
			System.out.println("visitSuperclass");
			type = SUPER;
			return this;
		}

		@Override
		public SignatureVisitor visitInterface() {
			flush();
			System.out.println("visitInterface");
			type = INTERFACE;
			return this;
		}

		@Override
		public SignatureVisitor visitArrayType() {
			System.out.println("visitArrayType");
			arrayDepth++;
			return this;
		}

		@Override
		public void visitInnerClassType(String name) {
			if (genericStack % 2 == 1)
				sb.append('>');
			System.out.println("visitInnerClassType " + name);
			sb.append('$').append(name);
			genericStack &= ~1;
		}

		@Override
		public void visitClassType(String name) {
			System.out.println("visitClassType " + name);
			sb.append(name.replace('/', '.'));
			genericStack <<= 1;
		}

		@Override
		public void visitEnd() {
			System.out.println("visitEnd");
			if (genericStack % 2 == 1)
				sb.append('>');
			genericStack >>= 1;
			endType();
		}

		@Override
		public void visitTypeArgument() {
			System.out.println("visitTypeArgument");
			if (genericStack % 2 == 0) {
				genericStack |= 1;
				sb.append('<');
			} else
				sb.append(',');
			sb.append('?');
		}

		@Override
		public SignatureVisitor visitTypeArgument(char wildcard) {
			System.out.println("visitTypeArguement " + wildcard);
			if (genericStack % 2 == 0) {
				genericStack |= 1;
				sb.append('<');
			} else
				sb.append(',');
			switch (wildcard) {
				case '+':
					sb.append("? extends ");
					break;
				case '-':
					sb.append("? super ");
					break;
				case '=':
					break;
				default:
					throw new IllegalArgumentException();
			}
			return this;
		}

		@Override
		public void visitTypeVariable(String name) {
			System.out.println("visitTypeVariable " + name);
			sb.append(name);
			endType();
		}

		@Override
		public SignatureVisitor visitClassBound() {
			System.out.println("visitClassBound");
			sb.append(" extends ");
			return this;
		}

		@Override
		public SignatureVisitor visitInterfaceBound() {
			System.out.println("visitInterfaceBound");
			sb.append('&');
			return this;
		}
	}
}
