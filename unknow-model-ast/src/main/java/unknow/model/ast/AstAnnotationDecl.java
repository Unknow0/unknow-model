package unknow.model.ast;

import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.impl.AbstractAnnotationDeclModel;
import unknow.model.api.impl.AbstractAnnotationMemberDeclModel;

public class AstAnnotationDecl extends AbstractAnnotationDeclModel implements AnnotationDeclModel, AstMod<AnnotationDeclaration> {
	private final AnnotationDeclaration a;

	/**
	 * @param loader the model loader
	 * @param parent the package
	 * @param a the annotation decl
	 */
	public AstAnnotationDecl(ModelLoader loader, PackageModel parent, AnnotationDeclaration a) {
		super(loader, parent, AstUtils.toBinaryName(a));
		this.a = a;
	}

	@Override
	public AnnotationDeclaration object() {
		return a;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return a.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
	}

	@Override
	protected List<AnnotationMemberDeclModel> loadMembers(ModelLoader loader) {
		return a.getMethods().stream().map(m -> new AstAnnotationMember(loader, this, m.asAnnotationMemberDeclaration())).collect(Collectors.toList());
	}

	private static final class AstAnnotationMember extends AbstractAnnotationMemberDeclModel {
		private final AnnotationMemberDeclaration a;

		protected AstAnnotationMember(ModelLoader loader, AnnotationDeclModel parent, AnnotationMemberDeclaration a) {
			super(loader, parent, a.getNameAsString());
			this.a = a;
		}

		@Override
		protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
			return a.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
		}

		@Override
		protected AnnotationValue loadValue(ModelLoader loader) {
			AnnotationValue value = a.getDefaultValue().map(v -> AstAnnotation.value(loader, v)).orElse(null);
			if (value != null)
				return value;
			return AnnotationValue.defaultValue(loader.get(AstUtils.toBinaryName(a.getType().resolve())));
		}
	}
}
