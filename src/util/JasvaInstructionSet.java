package util;

import codeGeneration.FormattedInstruction;
import codeGeneration.Instruction;
import codeGeneration.programGeneration.*;
import compilation.JasminCompiler;

public enum JasvaInstructionSet implements Instruction<String, JasvaInstructionSet> {

    ADD("iadd", 2, 1),
    SUB("isub", 2, 1),
    MUL("imul", 2, 1),
    DIV("idiv", 2, 1),
    MOD("irem", 2, 1),
    NEG("ineg", 1, 1),
    AND("iand", 2, 1),
    BOR("ior", 2, 1),
    DUP("dup", 1, 2),
    POP("pop", 1, 0),
    SWP("swap", 2, 2),
    RET("return", 1, 0),
    NUL("aconst_null", 0, 1),
    CST("iconst_%d", 0, 1, true),
    PSH("sipush %d", 0, 1, true),
    LDA("aload_%d", 0, 1, true),
    STA("astore_%d", 1, 0, true),
    GET("get%s %s %s", 1, 1) {
        @Override
        public final FormattedInstruction<String, JasvaInstructionSet> instance(Object... args) {
            final Slot slot = (Slot) args[0];
            final String mode = slot.isStatic() ? "static" : "field";
            final CompilerType slotType = slot.getType();
            final CompilerType type = args.length == 2 && (boolean) args[1] && !slotType.isPrimitive() ? BaseTypes.OBJECT : slotType;
            final String formatted = String.format(getCompilerFormattingAlias(), mode, slot.getPath(), type.asDescriptor());
            return JasvaInstructionSet.format(this, formatted);
        }
    },
    PUT("put%s %s %s", 2, 0) {
        @Override
        public final FormattedInstruction<String, JasvaInstructionSet> instance(Object... args) {
            final Slot slot = (Slot) args[0];
            final String mode = slot.isStatic() ? "static" : "field";
            final CompilerType slotType = slot.getType();
            final CompilerType type = args.length == 2 && (boolean) args[1] && !slotType.isPrimitive() ? BaseTypes.OBJECT : slotType;
            final String formatted = String.format(getCompilerFormattingAlias(), mode, slot.getPath(), type.asDescriptor());
            return JasvaInstructionSet.format(this, formatted);
        }
    },
    NEW("new %s", 0, 1) {
        @Override
        public final FormattedInstruction<String, JasvaInstructionSet> instance(Object... args) {
            return JasvaInstructionSet.format(this, String.format(getCompilerFormattingAlias(), ((CompiledClass<?>)args[0]).getType().asLiteral()));
        }
    },
    IVK("invoke%s %s", 1, 1) {
        @Override
        public final FormattedInstruction<String, JasvaInstructionSet> instance(Object... args) {
            final CompiledMethod<JasminCompiler> method = (CompiledMethod<JasminCompiler>) args[0];
            final String mode = method.isSpecial() ? "special" : method.isStatic() ? "static" : "virtual";
            return JasvaInstructionSet.format(this, String.format(getCompilerFormattingAlias(), mode, method));
        }
    },
    IVK_SUPER("invokenonvirtual java/lang/Object/<init>()V", 1, 0),
    IFE("ifeq %s", 1, 0, true),
    CHC("checkcast %s", 2, 0, true),
    CEQ("if_icmpeq %s", 2, 0, true),
    CGT("if_icmpgt %s", 2, 0, true),
    CGE("if_icmpge %s", 2, 0, true),
    CLT("if_icmplt %s", 2, 0, true),
    CLE("if_icmpge %s", 2, 0, true),
    GOT("goto %s", 0, 0, true);

    private final String _rawInstruction;
    private final int _stackImpact;
    private final boolean _simpleFormat;

    JasvaInstructionSet(final String rawInstruction,
                        final int stackConsume,
                        final int stackProduce,
                        final boolean simpleFormat) {
        _rawInstruction = rawInstruction;
        _stackImpact = stackProduce - stackConsume;
        _simpleFormat = simpleFormat;
    }

    JasvaInstructionSet(final String rawInstruction,
                        final int stackConsume,
                        final int stackProduce) {
        this(rawInstruction, stackConsume, stackProduce, false);
    }

    @Override
    public FormattedInstruction<String, JasvaInstructionSet> instance(Object... args) {
        if(!_simpleFormat)
            return () -> this;
        return JasvaInstructionSet.format(this, String.format(_rawInstruction, args));
    }

    @Override
    public final String getCompilerFormattingAlias() {
        return _rawInstruction;
    }

    @Override
    public final int getEvaluationStackImpact() {
        return _stackImpact;
    }

    private static FormattedInstruction<String, JasvaInstructionSet> format(final JasvaInstructionSet instruction,
                                                                            final String formatted) {
        return format(instruction, formatted, instruction.getEvaluationStackImpact());
    }

    private static FormattedInstruction<String, JasvaInstructionSet> format(final JasvaInstructionSet instruction,
                                                                            final String formatted,
                                                                            final int impact) {
        return new FormattedInstruction<String, JasvaInstructionSet>() {
            @Override
            public final JasvaInstructionSet getReference() {
                return instruction;
            }

            @Override
            public final String getTransformedType() {
                return formatted;
            }

            @Override
            public final int getUpdatedStackImpact() {
                return impact;
            }
        };
    }
}
