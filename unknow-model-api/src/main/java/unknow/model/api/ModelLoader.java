/**
 * 
 */
package unknow.model.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unknow.model.api.impl.AbstractPackageModel.EmptyPackageModel;

/**
 * @author unknow
 */
public abstract class ModelLoader {
	private static final TypeModel[] EMPTY = {};

	private static final Map<String, TypeModel> BUILDIN = new HashMap<>();

	static {
		for (PrimitiveModel t : PrimitiveModel.PRIMITIVES) {
			BUILDIN.put(t.simpleName(), t);
			BUILDIN.put(t.name(), t);
		}
	}

	protected static final Pattern CLAZZ = Pattern.compile("(.+?)(?:<(.*?)>)?");
	protected static final Pattern CLAZZ_LIST = Pattern.compile("(.+?(?:<.*?>)?)(?:,|$)");

	private final Map<String, PackageModel> packages;
	private final Map<String, TypeModel> cache;

	protected ModelLoader() {
		this.packages = new HashMap<>();
		this.cache = new HashMap<>();
	}

	/**
	 * @param loaders the loaders
	 * @return a model loader
	 */
	public static ModelLoader from(ModelLoader... loaders) {
		if (loaders.length == 1)
			return loaders[1];
		return new CompositeLoader(loaders);
	}

	/**
	 * @param cl the class to load
	 * @return the loaded type
	 */
	public TypeModel get(String cl) {
		return get(cl, Collections.emptyList());
	}

	/**
	 * get of load a class from it's binary name
	 * @param cl class name
	 * @param parameters actual type params
	 * @return the loaded type
	 */
	public TypeModel get(String cl, List<TypeParamModel> parameters) {
		String key = cl + "#" + parameters;
		TypeModel t = cache.get(key);
		if (t != null)
			return t;

		t = create(cl, parameters);
		if (t == null)
			throw new IllegalArgumentException(this.getClass().getName() + ": Type not found " + cl);
		cache.put(key, t);
		return t;
	}

	public PackageModel getPackage(String name) {
		PackageModel p = packages.get(name);
		if (p == null) {
			p = loadPackage(this, name);
			if (p == null)
				p = new EmptyPackageModel(name);
			packages.put(name, p);
		}
		return p;
	}

	private final TypeModel create(String cl, List<TypeParamModel> parameters) {
		TypeModel b = BUILDIN.get(cl);
		if (b != null)
			return b;

		if (cl.endsWith("[]"))
			return new ArrayModel(get(cl.substring(0, cl.length() - 2), parameters));
		if (cl.equals("?"))
			return WildcardModel.EMPTY;
		if (cl.startsWith("? extends "))
			return new WildcardModel(get(cl.substring(10), parameters), true);
		if (cl.startsWith("? super "))
			return new WildcardModel(get(cl.substring(8), parameters), false);

		List<String> parse = parse(cl);
		Map<String, TypeModel> map = new HashMap<>();
		for (TypeParamModel t : parameters)
			map.put(t.name(), t.type());

		TypeModel[] params = EMPTY;
		if (parse.size() > 1) {
			cl = parse.get(0);
			params = new TypeModel[parse.size() - 1];
			for (int i = 1; i < parse.size(); i++) {
				String s = parse.get(i);
				params[i - 1] = map.containsKey(s) ? map.get(s) : get(s, parameters);
			}
		}
		TypeModel t = map.get(cl);
		if (t != null)
			cl = t.name();

		int i = cl.lastIndexOf('.');
		PackageModel p = getPackage(i < 0 ? "" : cl.substring(0, i));
		return load(this, p, cl, params);
	}

	/**
	 * @param loader the loader
	 * @param parent package defining the type
	 * @param cl the fqn class to load
	 * @param params the concrete generic param
	 */
	protected abstract TypeModel load(ModelLoader loader, PackageModel parent, String cl, TypeModel[] params);

	/**
	 * @param loader the loader
	 * @param name package name
	 * @return the packageModel or null if no package defined
	 */
	protected abstract PackageModel loadPackage(ModelLoader loader, String name);

	/**
	 * split class into the class name and it's param
	 * 
	 * @param cl the class
	 * @return a list with the class and it's param {@code (String => [String], List<String> => [List, String], Map<String,List<String>> => [Map, String, List<String>]})
	 */
	public static List<String> parse(String cl) {
		Matcher m = CLAZZ.matcher(cl);
		if (!m.matches())
			throw new IllegalArgumentException("malformed class " + cl);
		if (m.group(2) == null)
			return Arrays.asList(m.group(1).trim());
		if (m.group(2).trim().isEmpty())
			return Arrays.asList(m.group(1).trim(), "");

		List<String> list = new ArrayList<>();
		list.add(m.group(1));
		m = CLAZZ_LIST.matcher(m.group(2).trim());
		while (m.find())
			list.add(m.group(1).trim());
		return list;
	}

	private static class CompositeLoader extends ModelLoader {

		private final ModelLoader[] loaders;

		public CompositeLoader(ModelLoader... loaders) {
			this.loaders = loaders;
		}

		@Override
		protected TypeModel load(ModelLoader loader, PackageModel parent, String cl, TypeModel[] params) {
			for (int i = 0; i < loaders.length; i++) {
				TypeModel t = loaders[i].load(loader, parent, cl, params);
				if (t != null)
					return t;
			}
			return null;
		}

		@Override
		protected PackageModel loadPackage(ModelLoader loader, String name) {
			for (int i = 0; i < loaders.length; i++) {
				PackageModel p = loaders[i].loadPackage(loader, name);
				if (p != null)
					return p;
			}
			return null;
		}
	}
}
