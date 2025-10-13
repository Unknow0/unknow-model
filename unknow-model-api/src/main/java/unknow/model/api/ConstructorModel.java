/**
 * 
 */
package unknow.model.api;

import java.util.List;

/**
 * @author unknow
 */
public interface ConstructorModel extends WithAnnotation, WithMod, WithParent<ClassModel> {

	/**
	 * @return generic param
	 */
	List<ParamModel<ConstructorModel>> parameters();

	/**
	 * @param i index of the parameter to get
	 * @return get the i'th parameter
	 */
	default ParamModel<ConstructorModel> parameter(int i) {
		return parameters().get(i);
	}

	/**
	 * @return method signature name(parameters type)return type
	 */
	default String signature() {
		StringBuilder sb = new StringBuilder("<init>").append('(');
		if (!parameters().isEmpty()) {
			for (ParamModel<ConstructorModel> t : parameters())
				sb.append(t.type().name()).append(',');
			sb.setLength(sb.length() - 1);
		}
		return sb.append(')').toString();
	}
}
