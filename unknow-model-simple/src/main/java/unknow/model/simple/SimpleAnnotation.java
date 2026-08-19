package unknow.model.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue.AnnotationValueClass;
import unknow.model.api.AnnotationValue.AnnotationValueLiteral;
import unknow.model.api.TypeModel;

public class SimpleAnnotation implements AnnotationModel {
	private final AnnotationDeclModel type;
	private final List<AnnotationMemberModel> members;

	public SimpleAnnotation(AnnotationDeclModel type) {
		this.type = type;
		this.members = new ArrayList<>(0);
	}

	public SimpleAnnotation withLiteral(String name, String value) {
		members.add(new AnnotationMemberModel(name, new AnnotationValueLiteral(value)));
		return this;
	}

	public SimpleAnnotation withClass(String name, TypeModel type) {
		members.add(new AnnotationMemberModel(name, new AnnotationValueClass(type)));
		return this;
	}

	public SimpleAnnotationArray withArray(String name) {
		SimpleAnnotationArray a = new SimpleAnnotationArray();
		members.add(new AnnotationMemberModel(name, a));
		return a;
	}

	@Override
	public String name() {
		return type.name();
	}

	@Override
	public AnnotationDeclModel type() {
		return type;
	}

	@Override
	public Collection<AnnotationMemberModel> members() {
		return members;
	}
}
