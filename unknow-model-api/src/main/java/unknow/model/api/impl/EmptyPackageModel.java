package unknow.model.api.impl;

import java.util.Collection;
import java.util.Collections;

import unknow.model.api.AnnotationModel;
import unknow.model.api.PackageModel;

public class EmptyPackageModel implements PackageModel {
	private final String name;

	/**
	 * @param name the package name
	 */
	public EmptyPackageModel(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public final Collection<AnnotationModel> annotations() {
		return Collections.emptyList();
	}

	@Override
	public final String toString() {
		return getClass().getSimpleName() + " " + name;
	}
}
