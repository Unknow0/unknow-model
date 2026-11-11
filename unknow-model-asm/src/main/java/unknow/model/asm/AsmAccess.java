package unknow.model.asm;

import org.objectweb.asm.Opcodes;

import unknow.model.api.WithMod;

public interface AsmAccess extends WithMod {
	int access();

	@Override
	default boolean isTransient() {
		return (access() & Opcodes.ACC_TRANSIENT) != 0;
	}

	@Override
	default boolean isStatic() {
		return (access() & Opcodes.ACC_STATIC) != 0;
	}

	@Override
	default boolean isPublic() {
		return (access() & Opcodes.ACC_PUBLIC) != 0;
	}

	@Override
	default boolean isProtected() {
		return (access() & Opcodes.ACC_PROTECTED) != 0;
	}

	@Override
	default boolean isPrivate() {
		return (access() & Opcodes.ACC_PRIVATE) != 0;
	}

	@Override
	default boolean isAbstract() {
		return (access() & Opcodes.ACC_ABSTRACT) != 0;
	}
}
