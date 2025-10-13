package unknow.model.simple;

import java.util.Arrays;

import unknow.model.api.AnnotationValue;
import unknow.model.api.AnnotationValue.AnnotationValueArray;

public class SimpleAnnotationArray extends AnnotationValueArray {

	/**
	 * create new AnnotationValueArray
	 */
	public SimpleAnnotationArray() {
		super(new AnnotationValue[0]);
	}

	public SimpleAnnotationArray with(AnnotationValue v) {
		a = Arrays.copyOf(a, a.length + 1);
		a[a.length - 1] = v;
		return this;
	}

	public SimpleAnnotationArray withNull() {
		return with(AnnotationValue.NULL);
	}

	public SimpleAnnotationArray withLiteral(String value) {
		return with(new AnnotationValueLiteral(value));
	}

	public SimpleAnnotation withAnnotation(Class<?> clazz) {
		return withAnnotation(clazz.getName());
	}

	private SimpleAnnotation withAnnotation(String name) {
		SimpleAnnotation an = new SimpleAnnotation(name);
		with(new AnnotationValueAnnotation(an));
		return an;
	}

	@Override
	public AnnotationValue[] asArray() {
		return a;
	}
}
