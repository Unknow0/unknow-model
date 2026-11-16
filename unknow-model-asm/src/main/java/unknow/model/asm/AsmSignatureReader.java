package unknow.model.asm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;

import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;

public class AsmSignatureReader extends SignatureVisitor {
	private final ModelLoader loader;
	private final ClassModel parent;
	private final TypeModel[] paramsClass;
	public final List<TypeParamModel> params;
	public String superClass;
	public final List<String> interfaces;

	private String currentName;
	private List<String> currentBounds;
	private int i;

	protected AsmSignatureReader(ModelLoader loader, ClassModel parent, TypeModel[] paramsClass) {
		super(Opcodes.ASM9);
		this.loader = loader;
		this.parent = parent;
		this.paramsClass = paramsClass;
		this.params = new ArrayList<>();
		this.interfaces = new ArrayList<>();
	}

	public static AsmSignatureReader parse(ModelLoader loader, ClassModel parent, TypeModel[] paramsClass, String signature) {
		if (signature == null)
			return null;
		AsmSignatureReader r = new AsmSignatureReader(loader, parent, paramsClass);
		new SignatureReader(signature).accept(r);
		r.flush();
		return r;
	}

	public static String parseType(String signature) {
		if (signature == null)
			return null;
		StringBuilder result = new StringBuilder();
		new SignatureReader(signature).acceptType(new TypeReader(result::append));
		return result.toString();
	}

	@Override
	public SignatureVisitor visitSuperclass() {
		return new TypeReader(s -> superClass = s.toString());
	}

	@Override
	public SignatureVisitor visitInterface() {
		return new TypeReader(s -> interfaces.add(s.toString()));
	}

	@Override
	public void visitFormalTypeParameter(String name) {
		flush();
		currentName = name;
		currentBounds = new ArrayList<>();
	}

	@Override
	public SignatureVisitor visitClassBound() {
		return new TypeReader(s -> currentBounds.add(s.toString()));
	}

	@Override
	public SignatureVisitor visitInterfaceBound() {
		return new TypeReader(s -> currentBounds.add(s.toString()));
	}

	private void flush() {
		if (currentName != null) {
			TypeModel t = null;
			if (i < paramsClass.length)
				t = paramsClass[i++];
			params.add(new AsmTypeParam(loader, parent, t, currentName, currentBounds));
			currentName = null;
			currentBounds = null;
		}
	}

	private static class TypeReader extends SignatureVisitor {
		private final Consumer<StringBuilder> c;
		private final StringBuilder sb;
		private BitSet hasArg;
		private int i = 0;

		protected TypeReader(Consumer<StringBuilder> c) {
			super(Opcodes.ASM9);
			this.c = c;
			this.sb = new StringBuilder();
			this.hasArg = new BitSet();
		}

		private void append(String name) {
			int i = name.indexOf('/');
			int s = 0;
			while (i != -1) {
				sb.append(name, s, i).append('.');
				s = i + 1;
				i = name.indexOf('/', s);
			}
			sb.append(name, s, name.length());
		}

		private void startArg() {
			if (!hasArg.get(i)) {
				sb.append('<');
				hasArg.set(i);
			} else
				sb.append(',');
		}

		private void closeArg() {
			if (hasArg.get(i)) {
				hasArg.clear(i);
				sb.append('>');
			}
		}

		private void flush() {
			if (i == 0) {
				c.accept(sb);
				sb.setLength(0);
			}
		}

		@Override
		public void visitClassType(String name) {
			i++;
			append(name);
		}

		@Override
		public void visitInnerClassType(String name) {
			closeArg();
			sb.append('$');
			append(name);
		}

		@Override
		public void visitBaseType(char type) {
			switch (type) {
				case 'Z':
					sb.append("boolean");
					break;
				case 'B':
					sb.append("byte");
					break;
				case 'C':
					sb.append("char");
					break;
				case 'S':
					sb.append("short");
					break;
				case 'I':
					sb.append("int");
					break;
				case 'J':
					sb.append("long");
					break;
				case 'F':
					sb.append("float");
					break;
				case 'D':
					sb.append("double");
					break;
				case 'V':
					sb.append("void");
					break;
				default:
					throw new IllegalArgumentException("Unknown base type: " + type);
			}
			flush();
		}

		@Override
		public SignatureVisitor visitTypeArgument(char wildcard) {
			startArg();

			if (wildcard == '+')
				sb.append("? extends ");
			else if (wildcard == '-')
				sb.append("? super ");
			return this;
		}

		@Override
		public void visitTypeVariable(String name) {
			sb.append(name);
			flush();
		}

		@Override
		public void visitTypeArgument() {
			startArg();
			sb.append('?');
		}

		@Override
		public SignatureVisitor visitArrayType() {
			return new TypeReader(s -> {
				sb.append(s).append("[]");
				flush();
			});
		}

		@Override
		public void visitEnd() {
			closeArg();
			i--;
			flush();
		}
	}
}