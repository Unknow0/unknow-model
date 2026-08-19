package unknow.model.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import unknow.model.api.AnnotationDeclModel;
import unknow.model.api.AnnotationDeclModel.AnnotationMemberDeclModel;
import unknow.model.api.AnnotationMemberModel;
import unknow.model.api.AnnotationModel;
import unknow.model.api.ModelLoader;

public abstract class AbstractAnnotationModel implements AnnotationModel {
	private final ModelLoader loader;
	private final String name;
	private AnnotationDeclModel type;
	private Collection<AnnotationMemberModel> members;

	/**
	 * @param loader the model loader
	 * @param name annotation binary name
	 */
	protected AbstractAnnotationModel(ModelLoader loader, String name) {
		this.loader = loader;
		this.name = name;
	}

	/**
	 * @param loader the model loader
	 * @return annotation members
	 */
	protected abstract Collection<AnnotationMemberModel> loadMembers(ModelLoader loader);

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final AnnotationDeclModel type() {
		if (type == null)
			type = loader.get(name).asAnnotationDecl();
		return type;
	}

	@Override
	public final Collection<AnnotationMemberModel> members() {
		if (members == null) {
			Map<String, AnnotationMemberModel> map = new HashMap<>();
			for (AnnotationMemberModel m : loadMembers(loader))
				map.put(m.name(), m);
			for (AnnotationMemberDeclModel m : type().members())
				map.computeIfAbsent(m.name(), n -> m.asAnnotationMember());
			members = new ArrayList<>(map.values());
		}
		return members;
	}
}
