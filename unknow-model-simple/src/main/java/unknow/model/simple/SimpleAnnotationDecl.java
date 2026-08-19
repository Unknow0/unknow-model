package unknow.model.simple;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationDeclModel;

public class SimpleAnnotationDecl extends SimpleWithMod implements AnnotationDeclModel {
	private final String name;
	private final List<AnnotationMemberDeclModel> members;

	public SimpleAnnotationDecl(String name) {
		this(name, Modifier.PUBLIC);
	}

	public SimpleAnnotationDecl(String name, int mod) {
		super(mod);
		this.name = name;
		this.members = new ArrayList<>();
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Collection<AnnotationMemberDeclModel> members() {
		return members;
	}

}
