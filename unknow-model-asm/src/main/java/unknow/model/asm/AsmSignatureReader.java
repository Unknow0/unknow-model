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
	private final List<TypeParamModel> list;

	private String currentName;
	private List<String> currentBounds;
	private int i;

	protected AsmSignatureReader(ModelLoader loader, ClassModel parent, TypeModel[] paramsClass) {
		super(Opcodes.ASM9);
		this.loader = loader;
		this.parent = parent;
		this.paramsClass = paramsClass;
		this.list = new ArrayList<>();
	}

	public static List<TypeParamModel> parse(ModelLoader loader, ClassModel parent, TypeModel[] paramsClass, String signature) {
		if (signature == null)
			return Collections.emptyList();
		AsmSignatureReader r = new AsmSignatureReader(loader, parent, paramsClass);
		new SignatureReader(signature).accept(r);
		r.flush();
		return r.list;
	}

	@Override
	public void visitFormalTypeParameter(String name) {
		System.out.println("visitFormalTypeParameter " + name);
		flush();
		currentName = name;
		currentBounds = new ArrayList<>();
	}

	@Override
	public SignatureVisitor visitClassBound() {
		System.out.println("visitClassBound");
		return new BoundReader(s -> currentBounds.add(s.toString()));
	}

	@Override
	public SignatureVisitor visitInterfaceBound() {
		System.out.println("visitInterfaceBound");
		return new BoundReader(s -> currentBounds.add(s.toString()));
	}

	private void flush() {
		if (currentName != null) {
			TypeModel t = null;
			if (i < paramsClass.length)
				t = paramsClass[i++];
			list.add(new AsmTypeParam(loader, parent, t, currentName, currentBounds));
			currentName = null;
			currentBounds = null;
		}
	}

	private static class BoundReader extends SignatureVisitor {

		private final Consumer<StringBuilder> c;
		private final StringBuilder sb;
		private BitSet hasArg;
		private int i = 0;

		protected BoundReader(Consumer<StringBuilder> c) {
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

		@Override
		public void visitClassType(String name) {
			i++;
			System.out.println("  visitClassType " + name);
			append(name);
		}

		@Override
		public void visitInnerClassType(String name) {
			System.out.println("  visitInnerClassType " + name);
			closeArg();
			sb.append('$');
			append(name);
		}

		@Override
		public SignatureVisitor visitTypeArgument(char wildcard) {
			System.out.println("  visitTypeArgument " + wildcard);
			startArg();

			if (wildcard == '+')
				sb.append("? extends ");
			else if (wildcard == '-')
				sb.append("? super ");
			return this;
		}

		@Override
		public void visitTypeVariable(String name) {
			System.out.println("  visitTypeVariable " + name);
			sb.append(name);
		}

		@Override
		public void visitTypeArgument() {
			System.out.println("  visitTypeArgument");
			startArg();
			sb.append('?');
		}

		@Override
		public SignatureVisitor visitArrayType() {
			System.out.println("  visitArrayType");
			return new BoundReader(s -> sb.append(s.append("[]")));
		}

		@Override
		public void visitEnd() {
			System.out.println("  visitEnd");
			closeArg();
			if (--i == 0)
				c.accept(sb);
		}
	}
}