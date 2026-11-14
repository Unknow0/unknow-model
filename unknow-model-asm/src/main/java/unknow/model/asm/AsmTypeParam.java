package unknow.model.asm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ClassModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.TypeModel;
import unknow.model.api.impl.AbstractTypeParamModel;

public class AsmTypeParam extends AbstractTypeParamModel {

	final String name;
	final List<String> bounds;

	public AsmTypeParam(ModelLoader loader, ClassModel c, TypeModel type, String name, List<String> bounds) {
		super(loader, c, type);
		this.name = name;
		this.bounds = bounds;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	protected List<ClassModel> loadBounds(ModelLoader loader) {
		return bounds.stream().map(c -> loader.get(c).asClass()).collect(Collectors.toList());
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AsmTypeParam))
			return false;
		AsmTypeParam a = (AsmTypeParam) obj;
		return Objects.equals(name, a.name) && Objects.equals(bounds, a.bounds);
	}
}
