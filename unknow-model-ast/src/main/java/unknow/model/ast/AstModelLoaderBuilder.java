package unknow.model.ast;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class AstModelLoaderBuilder extends SimpleFileVisitor<Path> {
	private static final Logger logger = LoggerFactory.getLogger(AstModelLoaderBuilder.class);

	private final Map<String, TypeDeclaration<?>> classes;
	private final Map<String, PackageDeclaration> packages;
	private final JavaParser parser;

	public AstModelLoaderBuilder() {
		classes = new HashMap<>();
		packages = new HashMap<>();

		TypeSolver resolver = new ReflectionTypeSolver(false);
		JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(resolver);
		parser = new JavaParser(new ParserConfiguration().setStoreTokens(true).setSymbolResolver(javaSymbolSolver));
	}

	public void scan(Path src) throws IOException {
		Files.walkFileTree(src, this);
	}

	public AstModelLoader build() {
		return new AstModelLoader(classes, packages);
	}

	public static AstModelLoader build(Path... src) throws IOException {
		AstModelLoaderBuilder w = new AstModelLoaderBuilder();
		for (int i = 0; i < src.length; i++)
			w.scan(src[i]);
		return w.build();
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		String f = file.getFileName().toString();
		if (!f.endsWith(".java"))
			return FileVisitResult.CONTINUE;
		ParseResult<CompilationUnit> parse = parser.parse(file);

		if (!parse.isSuccessful()) {
			logger.warn("Failed to parse {}: {}", f, parse.getProblems());
			return FileVisitResult.CONTINUE;
		}
		CompilationUnit cu = parse.getResult().orElse(null);
		if (cu == null)
			return FileVisitResult.CONTINUE;
		cu.getPackageDeclaration().filter(v -> v.getAnnotations() != null).ifPresent(v -> packages.put(v.getNameAsString(), v));
		for (TypeDeclaration<?> v : cu.findAll(TypeDeclaration.class)) {
			String qualifiedName = v.resolve().getQualifiedName();
			classes.put(qualifiedName, v);
		}
		return FileVisitResult.CONTINUE;
	}
}