package unknow.model.jvm;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.AnnotationValue.AnnotationValueAnnotation;
import unknow.model.api.AnnotationValue.AnnotationValueArray;
import unknow.model.api.AnnotationValue.AnnotationValueClass;
import unknow.model.api.AnnotationValue.AnnotationValueLiteral;
import unknow.model.api.ModelLoader;
import unknow.model.api.impl.AbstractAnnotationModel;

/**
 * @author unknow
 */
public class JvmAnnotation extends AbstractAnnotationModel implements AnnotationModel {
	private final Annotation a;

	/**
	 * create new JvmAnnotation
	 * 
	 * @param loader the loader
	 * @param a the annotation
	 */
	public JvmAnnotation(ModelLoader loader, Annotation a) {
		super(loader, a.annotationType().getName());
		this.a = a;
	}

	@Override
	protected Collection<AnnotationMemberModel> loadMembers(ModelLoader loader) {
		List<AnnotationMemberModel> members = new ArrayList<>();
		Method[] methods = a.annotationType().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			try {
				AnnotationValue value = getValue(loader, m.invoke(a));
				members.add(new AnnotationMemberModel(m.getName(), value));
			} catch (Exception e) {
				throw new IllegalArgumentException(e);
			}
		}
		return members;
	}

	/**
	 * convert annotation value
	 * 
	 * @param loader the loader
	 * @param o value to convert
	 * @return object as annotation value
	 */
	public static AnnotationValue getValue(ModelLoader loader, Object o) {
		if (o == null)
			return AnnotationValue.NULL;
		if (o instanceof boolean[]) {
			boolean[] t = (boolean[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Boolean.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof byte[]) {
			byte[] t = (byte[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Byte.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof char[]) {
			char[] t = (char[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Character.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof short[]) {
			short[] t = (short[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Short.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof int[]) {
			int[] t = (int[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Integer.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof long[]) {
			long[] t = (long[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Long.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof float[]) {
			float[] t = (float[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Float.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o instanceof double[]) {
			double[] t = (double[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = new AnnotationValueLiteral(Double.toString(t[i]));
			return new AnnotationValueArray(a);
		}
		if (o.getClass().isArray()) {
			Object[] t = (Object[]) o;
			AnnotationValue[] a = new AnnotationValue[t.length];
			for (int i = 0; i < t.length; i++)
				a[i] = getValue(loader, t[i]);
			return new AnnotationValueArray(a);
		}
		if (o instanceof Class)
			return new AnnotationValueClass(loader.get(((Class<?>) o).getName()));
		if (o instanceof Enum)
			return new AnnotationValueLiteral(((Enum<?>) o).name());
		if (o instanceof Annotation)
			return new AnnotationValueAnnotation(new JvmAnnotation(loader, (Annotation) o));
		return new AnnotationValueLiteral(o.toString());
	}
}
