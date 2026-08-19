package unknow.model.api.impl;

import java.util.Collection;
import java.util.List;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;

public abstract class AbstractAnnotationDeclModel implements AnnotationDeclModel {
	private final ModelLoader loader;
	private final PackageModel parent;
	private final String name;
	private Collection<AnnotationModel> annotations;
	private Collection<AnnotationMemberDeclModel> members;

	/**
	 * @param loader the model loader
	 * @param parent the package
	 * @param name the annotation binary name
	 */
	protected AbstractAnnotationDeclModel(ModelLoader loader, PackageModel parent, String name) {
		this.loader = loader;
		this.parent = parent;
		this.name = name;
	}

	/**
	 * @param loader the model loader
	 * @return annotations
	 */
	protected abstract List<AnnotationModel> loadAnnotations(ModelLoader loader);

	/**
	 * @param loader the model loader
	 * @return annotation members
	 */
	protected abstract List<AnnotationMemberDeclModel> loadMembers(ModelLoader loader);

	@Override
	public final PackageModel parent() {
		return parent;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final Collection<AnnotationModel> annotations() {
		if (annotations == null)
			annotations = loadAnnotations(loader);
		return annotations;
	}

	@Override
	public final Collection<AnnotationMemberDeclModel> members() {
		if (members == null)
			members = loadMembers(loader);
		return members;
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + name;
	}
}
