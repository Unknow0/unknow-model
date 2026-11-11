package unknow.model.asm;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.impl.AbstractPackageModel;

public class AsmPackage extends AbstractPackageModel {
	private final ClassNode clazz;

	protected AsmPackage(ModelLoader loader, ClassNode clazz, String name) {
		super(loader, name);
		this.clazz = clazz;
	}

	@Override
	protected List<AnnotationModel> loadAnnotations(ModelLoader loader) {
		return AsmAnnotation.from(loader, clazz.invisibleAnnotations, clazz.visibleAnnotations);
	}

}
