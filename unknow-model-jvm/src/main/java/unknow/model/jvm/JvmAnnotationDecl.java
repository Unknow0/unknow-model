package unknow.model.jvm;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.impl.AbstractAnnotationDeclModel;
import unknow.model.api.impl.AbstractAnnotationMemberDeclModel;

public class JvmAnnotationDecl extends AbstractAnnotationDeclModel implements JvmMod {
	private final Class<?> cl;

	/**
	 * @param loader the model loader
	 * @param parent the package
	 * @param cl the class
	 */
	public JvmAnnotationDecl(ModelLoader loader, PackageModel parent, Class<?> cl) {
		super(loader, parent, cl.getName());
		this.cl = cl;
	}

	@Override
	public int mod() {
		return cl.getModifiers();
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return Arrays.stream(cl.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	protected List<AnnotationMemberDeclModel> loadMembers(ModelLoader loader) {
		return Stream.of(cl.getMethods()).map(m -> new JvmAnnotationMemberDecl(loader, this, m)).collect(Collectors.toList());
	}

	private static class JvmAnnotationMemberDecl extends AbstractAnnotationMemberDeclModel {
		private final Method m;

		protected JvmAnnotationMemberDecl(ModelLoader loader, AnnotationDeclModel parent, Method m) {
			super(loader, parent, m.getName());
			this.m = m;
		}

		@Override
		protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
			return Arrays.stream(m.getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
		}

		@Override
		protected AnnotationValue loadValue(ModelLoader loader) {
			return JvmAnnotation.getValue(loader, m.getDefaultValue());
		}
	}
}
