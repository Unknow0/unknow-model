package unknow.model.api.impl;

import java.util.List;

import unknow.model.api.EnumModel;
import unknow.model.api.ModelLoader;
import unknow.model.api.PackageModel;
import unknow.model.api.TypeModel;

public abstract class AbstractEnumModel extends AbstractClassModel implements EnumModel {
	private List<EnumConstant> entries;

	/**
	 * create new AbstractClassModel
	 * 
	 * @param loader      the loader
	 * @param parent package
	 * @param name class binary name
	 * @param paramsClass class parameter
	 */
	protected AbstractEnumModel(ModelLoader loader, PackageModel parent, String name, TypeModel[] paramsClass) {
		super(loader, parent, name, paramsClass);
	}

	protected abstract List<EnumConstant> loadEntries(ModelLoader loader);

	@Override
	public List<EnumConstant> entries() {
		if (entries == null)
			entries = loadEntries(loader);
		return entries;
	}

}
