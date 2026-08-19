package unknow.model.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.AnnotationValue;
import unknow.model.api.AnnotationValue.AnnotationValueAnnotation;
import unknow.model.api.AnnotationValue.AnnotationValueArray;
import unknow.model.api.AnnotationValue.AnnotationValueClass;
import unknow.model.api.AnnotationValue.AnnotationValueLiteral;
import unknow.model.api.FieldModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractAnnotationModel;

/**
 * @author unknow
 */
public class AstAnnotation extends AbstractAnnotationModel implements AnnotationModel {
	private final AnnotationExpr a;

	/**
	 * create new AstAnnotation
	 * 
	 * @param loader the loader
	 * @param a the annotation
	 */
	public AstAnnotation(ModelLoader loader, AnnotationExpr a) {
		super(loader, AstUtils.toBinaryName(a.resolve()));
		this.a = a;
	}

	@Override
	protected Collection<AnnotationMemberModel> loadMembers(ModelLoader loader) {
		if (a.isMarkerAnnotationExpr())
			return Collections.emptyList();

		if (a.isSingleMemberAnnotationExpr())
			return Arrays.asList(new AnnotationMemberModel("value", value(loader, a.asSingleMemberAnnotationExpr().getMemberValue())));

		Collection<AnnotationMemberModel> list = new ArrayList<>();
		for (MemberValuePair m : a.asNormalAnnotationExpr().getPairs())
			list.add(new AnnotationMemberModel(m.getNameAsString(), value(loader, m.getValue())));
		return list;
	}

	/**
	 * get annotation value
	 * 
	 * @param loader the loader
	 * @param v the value
	 * @return expression as AnnotationValue
	 */
	public static AnnotationValue value(ModelLoader loader, Expression v) {
		if (v.isNullLiteralExpr())
			return AnnotationValue.NULL;
		if (v.isStringLiteralExpr())
			return new AnnotationValueLiteral(v.asStringLiteralExpr().asString());
		if (v.isBooleanLiteralExpr())
			return new AnnotationValueLiteral(Boolean.toString(v.asBooleanLiteralExpr().getValue()));
		if (v.isIntegerLiteralExpr())
			return new AnnotationValueLiteral(v.asIntegerLiteralExpr().getValue());
		if (v.isLongLiteralExpr())
			return new AnnotationValueLiteral(v.asLongLiteralExpr().getValue());
		if (v.isCharLiteralExpr())
			return new AnnotationValueLiteral(v.asCharLiteralExpr().getValue());
		if (v.isDoubleLiteralExpr())
			return new AnnotationValueLiteral(v.asDoubleLiteralExpr().getValue());

		if (v.isFieldAccessExpr())
			return value(loader, v.asFieldAccessExpr().resolve());
		if (v.isNameExpr())
			return value(loader, v.asNameExpr().resolve());
		if (v.isClassExpr())
			return new AnnotationValueClass(loader.get(AstUtils.toBinaryName(v.asClassExpr().getType().resolve())));
		if (v.isArrayInitializerExpr()) {
			NodeList<Expression> values = v.asArrayInitializerExpr().getValues();
			AnnotationValue[] a = new AnnotationValue[values.size()];
			int i = 0;
			for (Expression e : values)
				a[i++] = value(loader, e);
			return new AnnotationValueArray(a);
		}
		if (v.isAnnotationExpr())
			return new AnnotationValueAnnotation(new AstAnnotation(loader, v.asAnnotationExpr()));

		throw new IllegalArgumentException("unsuported value: " + v.getClass());
	}

	private static AnnotationValue value(ModelLoader loader, ResolvedValueDeclaration r) {
		if (r.isEnumConstant())
			return new AnnotationValueLiteral(r.asEnumConstant().getName());
		if (r.isField()) {
			ResolvedFieldDeclaration f = r.asField();
			TypeModel t = loader.get(AstUtils.toBinaryName(f.declaringType()));
			if (t.isEnum())
				return new AnnotationValueLiteral(f.getName());
			if (f.isField()) {
				FieldModel field = t.asClass().field(f.getName()).orElse(null);
				if (field == null)
					throw new IllegalArgumentException("Can't find field '" + f.getName() + "' in " + t);
				if (!field.isStatic())
					throw new IllegalArgumentException("Field " + field + " sould be static");
				return field.value(loader);
			}
		}
		throw new IllegalArgumentException("Annotation value '" + r + "' not supported");
	}
}
