package dev.async.glint.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class GlintClassTransformer implements IClassTransformer {
    private static final String TARGET =
            "net.minecraft.client.renderer.entity.layers.LayerArmorBase";
    private static final String HOOK_OWNER =
            "dev/async/glint/client/GlintRenderHook";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || !TARGET.equals(transformedName)) {
            return basicClass;
        }

        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (isGlintMethod(method)) {
                replaceMethod(method);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);
                return writer.toByteArray();
            }
        }

        System.err.println("[ASync_glint] LayerArmorBase glint method was not found; transformer skipped.");
        return basicClass;
    }

    private static boolean isGlintMethod(MethodNode method) {
        if ((method.access & Opcodes.ACC_STATIC) == 0
                || Type.getReturnType(method.desc).getSort() != Type.VOID) {
            return false;
        }
        Type[] arguments = Type.getArgumentTypes(method.desc);
        if (arguments.length != 10) {
            return false;
        }
        for (int index = 0; index < 3; index++) {
            if (arguments[index].getSort() != Type.OBJECT) {
                return false;
            }
        }
        for (int index = 3; index < arguments.length; index++) {
            if (arguments[index].getSort() != Type.FLOAT) {
                return false;
            }
        }
        return true;
    }

    private static void replaceMethod(MethodNode method) {
        InsnList replacement = new InsnList();
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 0));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 1));
        replacement.add(new VarInsnNode(Opcodes.ALOAD, 2));
        for (int local = 3; local <= 9; local++) {
            replacement.add(new VarInsnNode(Opcodes.FLOAD, local));
        }
        replacement.add(new MethodInsnNode(
                Opcodes.INVOKESTATIC,
                HOOK_OWNER,
                "renderOptimized",
                method.desc,
                false
        ));
        replacement.add(new InsnNode(Opcodes.RETURN));

        method.instructions.clear();
        method.instructions.add(replacement);
        method.tryCatchBlocks.clear();
        method.localVariables = null;
        method.maxLocals = 10;
        method.maxStack = 10;
    }
}
