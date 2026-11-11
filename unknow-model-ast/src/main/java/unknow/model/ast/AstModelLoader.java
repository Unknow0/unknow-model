package unknow.model.ast;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node.TreeTraversal;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;

/**
 * @author unknow
 */
public class AstModelLoader extends ModelLoader {
	private static final Logger logger = LoggerFactory.getLogger(AstModelLoader.class);

	private final JavaParser parser;
	private final Path[] srcs;

	/**
	 * create new AstModelLoader
	 * 
	 * @param srcs source folder to use
	 */
	public AstModelLoader(Path... srcs) {
		this(null, srcs);
	}

	/**
	 * create new AstModelLoader
	 * 
	 * @param parser java parser to use (null to create one)
	 * @param srcs source folder to use
	 */
	public AstModelLoader(JavaParser parser, Path... srcs) {
		this.parser = parser == null ? createParser() : parser;
		this.srcs = srcs;
	}

	private static final JavaParser createParser() {
		TypeSolver resolver = new ReflectionTypeSolver(false);
		JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(resolver);
		return new JavaParser(new ParserConfiguration().setStoreTokens(true).setSymbolResolver(javaSymbolSolver));
	}

	@Override
	protected PackageModel loadPackage(ModelLoader loader, String name) {
		String f = name.isEmpty() ? "package-info.java" : name.replace('.', '/') + "/package-info.java";
		return load(f).flatMap(cu -> cu.getPackageDeclaration()).map(p -> new AstPackage(loader, p)).orElse(null);
	}

	@Override
	protected TypeModel load(ModelLoader loader, PackageModel parent, String cl, TypeModel[] params) {
		TypeDeclaration<?> t = getType(cl);
		if (t == null)
			return null;

		if (t.isClassOrInterfaceDeclaration() || t.isEnumDeclaration())
			return new AstClassOrEnum<TypeDeclaration<?>>(loader, params, parent, t);
		if (t.isAnnotationDeclaration())
			return new AstAnnotationDecl(loader, parent, t.asAnnotationDeclaration());
		throw new IllegalArgumentException("unsuported type " + t);
	}

	private TypeDeclaration<?> getType(String cl) {
		return load(fileName(cl)).flatMap(cu -> cu.findFirst(TreeTraversal.PREORDER, n -> {
			if (!(n instanceof TypeDeclaration))
				return Optional.empty();
			TypeDeclaration<?> t = (TypeDeclaration<?>) n;
			if (cl.equals(AstUtils.toBinaryName(t)))
				return Optional.of(t);
			return Optional.empty();
		})).orElse(null);
	}

	private final Optional<CompilationUnit> load(String file) {
		for (int i = 0; i < srcs.length; i++) {
			Path f = srcs[i].resolve(file);
			if (Files.isRegularFile(f)) {
				try {
					ParseResult<CompilationUnit> parse = parser.parse(f);
					CompilationUnit cu = parse.getResult().orElse(null);
					if (cu != null)
						return Optional.of(cu);
					logger.warn("Failed to parse {}: {}", f, parse.getProblems());
				} catch (IOException e) {
					logger.warn("Failed to parse {}: {}", f, e);
				}
			}
		}
		return Optional.empty();
	}

	private static String fileName(String cl) {
		int i = cl.indexOf('$');
		if (i > 0)
			cl = cl.substring(0, i);
		return cl.replace('.', '/').concat(".java");
	}

}
