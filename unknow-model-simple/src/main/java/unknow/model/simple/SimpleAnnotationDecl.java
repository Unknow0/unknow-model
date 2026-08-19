package unknow.model.simple;

import java.util.Collection;

import unknow.model.api.AnnotationDeclModel;

public class SimpleAnnotationDecl extends SimpleWithMod implements AnnotationDeclModel {
	private final String name;

	public SimpleAnnotationDecl(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isTransient() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStatic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPublic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isProtected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAbstract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<AnnotationMemberDeclModel> members() {
		// TODO Auto-generated method stub
		return null;
	}

}
