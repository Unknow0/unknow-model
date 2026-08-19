/**
 * 
 */
package unknow.model.api;

import java.util.List;

/**
 * @author unknow
 */
public interface MethodModel extends WithAnnotation, WithMod, WithName, WithType, WithParent<ClassModel> {

	/**
	 * @return generic param
	 */
	List<ParamModel> parameters();

	/**
	 * @param i index of the parameter to get
	 * @return get the i'th parameter
	 */
	default ParamModel parameter(int i) {
		return parameters().get(i);
	}

	/**
	 * @return method signature name(parameters type)return type
	 */
	default String signature() {
		StringBuilder sb = new StringBuilder(name()).append('(');
		if (!parameters().isEmpty()) {
			for (ParamModel t : parameters())
				sb.append(t.type().name()).append(',');
			sb.setLength(sb.length() - 1);
		}
		sb.append(')').append(type().name());
		return sb.toString();
	}
}
