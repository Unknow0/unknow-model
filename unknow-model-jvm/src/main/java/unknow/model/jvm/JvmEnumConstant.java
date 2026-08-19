package unknow.model.jvm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.EnumModel;
import unknow.model.api.EnumModel.EnumConstant;
import unknow.model.api.ModelLoader;
import unknow.model.api.impl.AbstractEnumConstant;

/**
 * @author unknow
 */
public class JvmEnumConstant extends AbstractEnumConstant implements EnumConstant {
	private final Enum<?> e;

	/**
	 * create new AstEnumConstant
	 * 
	 * @param loader the loader
	 * @param parent the enum
	 * @param e the enum entry
	 */
	public JvmEnumConstant(ModelLoader loader, EnumModel parent, Enum<?> e) {
		super(loader, parent);
		this.e = e;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		try {
			return Arrays.stream(e.getClass().getField(name()).getAnnotations()).map(a -> new JvmAnnotation(loader, a)).collect(Collectors.toList());
		} catch (NoSuchFieldException x) {
			throw new IllegalStateException(x);
		}
	}

	@Override
	public String name() {
		return e.name();
	}
}