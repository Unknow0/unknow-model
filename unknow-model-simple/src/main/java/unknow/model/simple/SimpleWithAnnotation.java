package unknow.model.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.WithAnnotation;

public class SimpleWithAnnotation implements WithAnnotation {
	private final List<AnnotationModel> annotations = new ArrayList<>(0);

	public SimpleAnnotation withAnnotation(Class<?> type) {
		return withAnnotation(new SimpleAnnotationDecl(type.getName()));
	}

	public SimpleAnnotation withAnnotation(AnnotationDeclModel type) {
		SimpleAnnotation a = new SimpleAnnotation(type);
		annotations.add(a);
		return a;
	}

	@Override
	public Collection<AnnotationModel> annotations() {
		return annotations;
	}
}
