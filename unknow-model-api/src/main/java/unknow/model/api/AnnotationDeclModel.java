package unknow.model.api;

import java.util.Collection;
import java.util.Optional;

/**
 * Annotation declaration
 */
public interface AnnotationDeclModel extends TypeModel, WithMod {

	/**
	 * @return list of members declared
	 */
	Collection<AnnotationMemberDeclModel> members();

	/**
	 * @param name member to find
	 * @return member found
	 */
	default Optional<AnnotationMemberDeclModel> member(String name) {
		return members().stream().filter(m -> name.equals(m.name())).findAny();
	}

	/**
	 * member declaration
	 */
	interface AnnotationMemberDeclModel extends WithName, WithParent<AnnotationDeclModel>, WithAnnotation {

		/**
		 * @return annotation default value
		 */
		AnnotationValue defaultValue();

		default AnnotationMemberModel asAnnotationMember() {
			return new AnnotationMemberModel(name(), defaultValue());
		}
	}
}
