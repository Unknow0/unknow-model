package unknow.model.simple;

import java.lang.reflect.Modifier;

public class SimpleWithMod extends SimpleWithAnnotation {
	private int mod;

	public SimpleWithMod() {
	}

	public SimpleWithMod(int mod) {
		this.mod = mod;
	}

	public boolean isTransient() {
		return Modifier.isTransient(mod);
	}

	public boolean isStatic() {
		return Modifier.isStatic(mod);
	}

	public boolean isPublic() {
		return Modifier.isPublic(mod);
	}

	public boolean isProtected() {
		return Modifier.isProtected(mod);
	}

	public boolean isPrivate() {
		return Modifier.isPrivate(mod);
	}

	public boolean isAbstract() {
		return Modifier.isAbstract(mod);
	}
}
