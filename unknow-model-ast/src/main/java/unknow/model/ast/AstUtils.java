package unknow.model.ast;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedWildcard;

public abstract class AstUtils {
	private AstUtils() {
	}

	/**
	 * convert to binary name
	 * @param type type to convert
	 * @return the binaryName
	 */
	public static String toBinaryName(ResolvedType type) {
		if (type.isArray()) {
			return toBinaryName(type.asArrayType().getComponentType()) + "[]";
		}
		if (type.isPrimitive()) {
			return type.asPrimitive().describe(); // "int", "boolean", "double", ...
		}
		if (type.isVoid()) {
			return "void";
		}
		if (type.isTypeVariable()) {
			return type.asTypeVariable().describe(); // "E", "T", ...
		}
		if (type.isWildcard()) {
			ResolvedWildcard w = type.asWildcard();
			if (!w.isBounded())
				return "?";
			String kw = w.isExtends() ? "? extends " : "? super ";
			return kw + toBinaryName(w.getBoundedType());
		}
		if (type.isReferenceType()) {
			ResolvedReferenceType rt = type.asReferenceType();
			ResolvedReferenceTypeDeclaration decl = rt.getTypeDeclaration()
					.orElseThrow(() -> new IllegalStateException("Failed to resolve type declaration " + rt.describe()));

			String raw = toBinaryName(decl);
			List<ResolvedType> typeArgs = rt.typeParametersValues();
			if (typeArgs.isEmpty())
				return raw;
			return typeArgs.stream().map(AstUtils::toBinaryName).collect(Collectors.joining(", ", raw + "<", ">"));
		}

		throw new IllegalArgumentException("Non manager ResolvedType  : " + type.getClass());
	}

	/**
	 * convert to binary name
	 * @param type type to convert
	 * @return the binaryName
	 */
	public static String toBinaryName(ResolvedTypeDeclaration type) {
		try {
			StringBuilder name = new StringBuilder(type.getName());
			Optional<ResolvedReferenceTypeDeclaration> containing = type.containerType();
			while (containing.isPresent()) {
				name.insert(0, '$').insert(0, containing.get().getName());
				containing = containing.get().containerType();
			}
			if (!type.getPackageName().isEmpty())
				name.insert(0, '.').insert(0, type.getPackageName());
			return name.toString();
		} catch (@SuppressWarnings("unused") UnsupportedOperationException e) {
			String qualifiedName = type.getQualifiedName();
			StringBuilder name = new StringBuilder(type.getPackageName());
			if (name.length() > 0)
				name.append('.');
			String rest = qualifiedName.substring(name.length());
			return name.append(rest.replace('.', '$')).toString();
		}
	}

	/**
	 * convert to binary name
	 * @param type type to convert
	 * @return the binaryName
	 */
	public static String toBinaryName(TypeDeclaration<?> type) {
		StringBuilder name = new StringBuilder(type.getNameAsString());
		Node n = type.getParentNode().orElse(null);
		while (n != null) {
			if (n instanceof TypeDeclaration)
				name.insert(0, '$').insert(0, ((NodeWithSimpleName<?>) n).getNameAsString());
			if (n instanceof CompilationUnit) {
				Optional<PackageDeclaration> o = ((CompilationUnit) n).getPackageDeclaration();
				if (o.isPresent())
					name.insert(0, '.').insert(0, o.get().getNameAsString());
			}
			n = n.getParentNode().orElse(null);
		}
		return name.toString();
	}
}
