package unknow.model.api;

public class AnnotationMemberModel implements WithName, AnnotationValue {
	private final String name;
	private final AnnotationValue value;

	public AnnotationMemberModel(String name, AnnotationValue value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public AnnotationValue[] asArray() {
		return value.asArray();
	}

	@Override
	public TypeModel asClass() {
		return value.asClass();
	}

	@Override
	public String asLiteral() {
		return value.asLiteral();
	}

	@Override
	public AnnotationModel asAnnotation() {
		return value.asAnnotation();
	}

	@Override
	public boolean valueEquals(AnnotationValue a) {
		return value.valueEquals(a);
	}

}
