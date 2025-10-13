
package unknow.model.simple;

import unknow.model.api.PackageModel;

public class SimplePackage extends SimpleWithAnnotation implements PackageModel {
	private final String name;

	public SimplePackage(String name) {
		this.name = name;
	}

	@Override
	public String name() {
		return name;
	}
}
