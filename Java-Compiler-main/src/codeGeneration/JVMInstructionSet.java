package codeGeneration;

public final class JVMInstructionSet {

    public static final Instruction ADD = simpleInstruction("iadd", -1);
    public static final Instruction SUB = simpleInstruction("isub", -1);
    public static final Instruction MUL = simpleInstruction("imul", -1);
    public static final Instruction DIV = simpleInstruction("idiv", -1);
    public static final Instruction MOD = simpleInstruction("irem", -1);
    public static final Instruction NEG = simpleInstruction("ineg", 0);
    public static final Instruction DUP = simpleInstruction("dup", 1);
    public static final Instruction RET = simpleInstruction("return", 0);
    public static final Instruction NULL_CONST = simpleInstruction("aconst_null", 1);
    public static final Instruction PUSH_INT = parameterizedInstruction("sipush %d", 1);
    public static final Instruction LVA_LOAD = parameterizedInstruction("aload_%d", 1);
    public static final Instruction LVA_STORE = parameterizedInstruction("astore_%d", -1);
    public static final Instruction FIELD_PUT = parameterizedInstruction("putfield %s", -2);
    public static final Instruction FIELD_GET = parameterizedInstruction("getfield %s", 0);
    public static final Instruction OBJECT_NEW = parameterizedInstruction("new %s", 1);
    public static final Instruction DEFAULT_CONSTRUCTOR = parameterizedInstruction("invokespecial %s/<init>()V", -1);

    private static Instruction parameterizedInstruction(final String code, final int impact) {
        return new Instruction() {
            @Override
            public final String getFormattedInstruction(Object... args) {
                return String.format(code, args);
            }

            @Override
            public final int getStackImpact() {
                return impact;
            }
        };
    }

    private static Instruction simpleInstruction(final String code, final int impact) {
        return new Instruction() {
            @Override
            public final String getFormattedInstruction(Object... args) {
                return code;
            }

            @Override
            public final int getStackImpact() {
                return impact;
            }
        };
    }
}
