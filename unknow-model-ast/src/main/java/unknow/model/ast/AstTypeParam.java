package unknow.model.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.type.TypeParameter;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.TypeParamModel;

/**
 * @author unknow
 */
public class AstTypeParam implements TypeParamModel {
	private final ModelLoader loader;
	private final ClassModel c;
	private final TypeParameter p;
	private final TypeModel type;
	private Collection<AnnotationModel> annotations;
	private List<ClassModel> bounds;

	/**
	 * create new AstParam
	 * 
	 * @param loader the loader
	 * @param c      the owner
	 * @param p      the parameter
	 * @param type   the type
	 */
	public AstTypeParam(ModelLoader loader, ClassModel c, TypeParameter p, TypeModel type) {
		this.loader = loader;
		this.c = c;
		this.p = p;
		this.type = type;
		if (p.getTypeBound() == null)
			bounds = Collections.emptyList();
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		if (annotations == null) {
			annotations = p.getAnnotations().stream().map(a -> new AstAnnotation(loader, a)).collect(Collectors.toList());
		}
		return annotations;
	}

	@Override
	public String name() {
		return p.getNameAsString();
	}

	@Override
	public ClassModel parent() {
		return c;
	}

	@Override
	public TypeModel type() {
		return type;
	}

	@Override
	public List<ClassModel> bounds() {
		if (bounds == null)
			bounds = p.getTypeBound().stream().map(t -> loader.get(t.resolve().describe(), c.parameters()).asClass()).collect(Collectors.toList());
		return bounds;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name());
		if (type() != null)
			sb.append('[').append(type()).append(']');
		return sb.toString();
	}
}
