package unknow.model.ast;

import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;

import unknow.model.api.WithMod;

/**
 * @author unknow
 */
public interface AstMod<T extends NodeWithModifiers<?>> extends WithMod {
	/**
	 * @return the object with modifier
	 */
	T object();

	@Override
	default boolean isTransient() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.TRANSIENT);
	}

	@Override
	default boolean isStatic() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.STATIC);
	}

	@Override
	default boolean isPublic() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.PUBLIC);
	}

	@Override
	default boolean isProtected() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.PROTECTED);
	}

	@Override
	default boolean isPrivate() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.PRIVATE);
	}

	@Override
	default boolean isAbstract() {
		return object().getModifiers().stream().anyMatch(m -> m.getKeyword() == Keyword.ABSTRACT);
	}
}
