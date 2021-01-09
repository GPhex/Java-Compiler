/* Generated By:JavaCC: Do not edit this line. Parser.java */
import ast.JasvaASTNode;
import ast.arithmetic.*;
import ast.block.*;
import ast.logical.*;
import ast.relational.*;
import ast.specific.*;
import ast.types.*;
import ast.variable.*;

import compilation.*;
import codeGeneration.environment.LinkedEnvironment;
import codeGeneration.programGeneration.*;
import compilation.fileGeneration.CachedProgramGenerator;
import types.*;
import util.Pair;
import values.Value;

import java.util.List;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Parser implements ParserConstants {

    private static final String EXTENSION = ".jsv";
    private static final String INTERPRET_FLAG = "-i";
    private static final String FILE_FLAG = "-f";
    private static final String AST_FLAG = "-ast";

    public static void main(String[] args) {
        try {
            boolean interpretFlag = false;
            boolean printASTFlag = false;
            String fileName = null;
            InputStream readFrom = System.in;

            int i = 0;
            while (i < args.length & i < 4) {
                switch (args[i++]) {
                    case INTERPRET_FLAG:
                        interpretFlag = true;
                        break;
                    case AST_FLAG:
                        printASTFlag = true;
                        break;
                    case FILE_FLAG:
                        if (i < args.length && (fileName = args[i++]).endsWith(EXTENSION))
                            readFrom = new FileInputStream(fileName);
                        else {
                            System.err.println("-f requires a valid " + EXTENSION +  " filename");
                            throw new IllegalStateException();
                        }
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
            if (readFrom == System.in) interpretFlag = true;
            if(printASTFlag)
                System.out.println("Printing Abstract Syntax Tree");
            if(interpretFlag)
                Parser.interpretMode(readFrom, printASTFlag);
            else
                Parser.compilerMode(fileName, readFrom, printASTFlag);

        } catch (IOException e) {
            System.err.println("Could not load the file.");
            printHelp();
        } catch (IllegalStateException e) {
            System.err.println("Bad argument.");
            printHelp();
        }
    }

    private static void interpretMode(final InputStream readFrom, final boolean printAST) {
        do {
            Parser parser = new Parser(readFrom);
            try {
                JasvaASTNode node = parser.Start();
                if(printAST) node.printNode("", "");
                System.out.println("Output> " + node.evaluate(new LinkedEnvironment<Value<?>>(null)));
            }
            catch (ParseException e) {
                System.err.println("Syntax error!");
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } while(readFrom == System.in);
    }

    private static void compilerMode(final String fileName, final InputStream readFrom, final boolean printAST) {
        final String outFileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf("."));
        Parser parser = new Parser(readFrom);
        try {
            JasvaASTNode node = parser.Start();
            node.typeCheck(new LinkedEnvironment<Type>(null));
            ProgramGenerator<JasminCompiler> programGenerator = new CachedProgramGenerator(outFileName, "j");
            CompiledClass<JasminCompiler> mainClass = programGenerator.getClass(outFileName);
            MethodBuilder<JasminCompiler> mainMethod = mainClass.addMethod("main", BaseTypes.VOID, Modifiers.PUBLIC, Modifiers.STATIC);
            mainMethod.addParam("args", CompilerType.array(BaseTypes.STRING));
            JasminCompiler compiler = mainMethod.getCompiler();
            compiler.startStaticLinker();
            if(printAST)
                node.printNode("", "");
            System.out.println("Compiling " + fileName);
            node.compile(mainClass, compiler, new LinkedEnvironment<Pair<Integer, String>>(null));
            programGenerator.generateFiles();
        }
        catch (ParseException e) {
            System.err.println("Syntax error!");
        }
        catch (IOException e) {
            System.err.println("Could not generate files");
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar Jasva.jar [-i] [-f file] [-ast]");
        System.out.println("-f file: compiles the provided file");
        System.out.println("-i: force interpreter mode. If a valid -f flag is present, interprets given file and exits");
        System.out.println("-ast: prints the syntax tree associated with the program");
    }

  final public JasvaASTNode Start() throws ParseException {
 JasvaASTNode statements;
    statements = CompoundStatement();
    jj_consume_token(TERM);
      {if (true) return statements;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode CompoundStatement() throws ParseException {
 JasvaASTNode leftUnit, rightUnit;
    leftUnit = TranslationUnit();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DLMT:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      jj_consume_token(DLMT);
      rightUnit = TranslationUnit();
     leftUnit = new ASTCompoundNode(leftUnit, rightUnit);
    }
      {if (true) return leftUnit;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode TranslationUnit() throws ParseException {
 JasvaASTNode leftExp, rightExp;
    leftExp = LogicORExpression();
    if (jj_2_1(2)) {
      jj_consume_token(ASSIGN);
      rightExp = LogicORExpression();
                                                           leftExp = new ASTAssignNode(leftExp, rightExp);
    } else {
      ;
    }
      {if (true) return leftExp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode LogicORExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
    l_exp = LogicANDExpression();
    label_2:
    while (true) {
      if (jj_2_2(2)) {
        ;
      } else {
        break label_2;
      }
      jj_consume_token(LO_OR);
      r_exp = LogicANDExpression();
                                                                                   l_exp = new ASTOrNode(l_exp, r_exp);
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode LogicANDExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
    l_exp = ComparisonExpression();
    label_3:
    while (true) {
      if (jj_2_3(2)) {
        ;
      } else {
        break label_3;
      }
      jj_consume_token(LO_AND);
      r_exp = ComparisonExpression();
                                                                                        l_exp = new ASTAndNode(l_exp, r_exp);
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode ComparisonExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
Token op;
    l_exp = RelationalExpression();
    if (jj_2_4(2)) {
      jj_consume_token(CMP_EQ);
      r_exp = RelationalExpression();
 l_exp = new ASTEqualsNode(l_exp, r_exp);
    } else {
      ;
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode RelationalExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
Token op;
    l_exp = AdditiveExpression();
    if (jj_2_5(2)) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CMP_LT:
        op = jj_consume_token(CMP_LT);
        r_exp = AdditiveExpression();
                                                 l_exp = new ASTLessThanNode(l_exp, r_exp);
        break;
      case CMP_LE:
        op = jj_consume_token(CMP_LE);
        r_exp = AdditiveExpression();
                                                 l_exp = new ASTLessEqNode(l_exp, r_exp);
        break;
      case CMP_GT:
        op = jj_consume_token(CMP_GT);
        r_exp = AdditiveExpression();
                                                 l_exp = new ASTGreatThanNode(l_exp, r_exp);
        break;
      case CMP_GE:
        op = jj_consume_token(CMP_GE);
        r_exp = AdditiveExpression();
                                                 l_exp = new ASTGreatEqNode(l_exp, r_exp);
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    } else {
      ;
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode AdditiveExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
Token op;
    l_exp = MultiplicativeExpression();
    label_4:
    while (true) {
      if (jj_2_6(2)) {
        ;
      } else {
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MO_POS:
        op = jj_consume_token(MO_POS);
        break;
      case MO_NEG:
        op = jj_consume_token(MO_NEG);
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      r_exp = MultiplicativeExpression();
 switch(op.kind) {
  case MO_POS: l_exp = new ASTAddNode(l_exp, r_exp); break;
  case MO_NEG: l_exp = new ASTSubNode(l_exp, r_exp); break;
}
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode MultiplicativeExpression() throws ParseException {
 JasvaASTNode l_exp, r_exp;
Token op;
    l_exp = UnaryExpression();
    label_5:
    while (true) {
      if (jj_2_7(2)) {
        ;
      } else {
        break label_5;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MO_PRO:
        op = jj_consume_token(MO_PRO);
        break;
      case MO_DIV:
        op = jj_consume_token(MO_DIV);
        break;
      case MO_MOD:
        op = jj_consume_token(MO_MOD);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      r_exp = UnaryExpression();
 switch(op.kind) {
  case MO_PRO: l_exp = new ASTMulNode(l_exp, r_exp); break;
  case MO_DIV: l_exp = new ASTDivNode(l_exp, r_exp); break;
  case MO_MOD: l_exp = new ASTModNode(l_exp, r_exp); break;
}
    }
   {if (true) return l_exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode UnaryExpression() throws ParseException {
 JasvaASTNode exp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case MO_NEG:
      jj_consume_token(MO_NEG);
      exp = UnaryExpression();
                                      exp = new ASTNegNode(exp);
      break;
    case MO_POS:
      jj_consume_token(MO_POS);
      exp = UnaryExpression();
                                      exp = new ASTPosNode(exp);
      break;
    case LO_NOT:
      jj_consume_token(LO_NOT);
      exp = UnaryExpression();
                                      exp = new ASTNotNode(exp);
      break;
    case L_PAR:
      jj_consume_token(L_PAR);
      exp = CompoundStatement();
      jj_consume_token(R_PAR);
      break;
    case DEF:
    case IF:
    case WHL:
    case NEW:
    case PRT:
    case PRL:
    case DEREF:
    case INTEGER_LITERAL:
    case BOOLEAN_LITERAL:
    case IDENTIFIER:
      exp = PrimaryExpression();
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode PrimaryExpression() throws ParseException {
 JasvaASTNode exp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
    case BOOLEAN_LITERAL:
      exp = Literal();
      break;
    case IDENTIFIER:
      exp = Var();
      break;
    case DEF:
      exp = ScopeExpression();
      break;
    case NEW:
    case DEREF:
      exp = ReferenceExpression();
      break;
    case IF:
      exp = BranchingExpression();
      break;
    case WHL:
      exp = IterationExpression();
      break;
    case PRT:
    case PRL:
      exp = PrintExpression();
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode Literal() throws ParseException {
 Token value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
      value = jj_consume_token(INTEGER_LITERAL);
                                {if (true) return new ASTIntNode(Integer.parseInt(value.image));}
      break;
    case BOOLEAN_LITERAL:
      value = jj_consume_token(BOOLEAN_LITERAL);
                                {if (true) return new ASTBoolNode(Boolean.parseBoolean(value.image));}
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode Var() throws ParseException {
 Token value;
JasvaASTNode node;
    value = jj_consume_token(IDENTIFIER);
                          node = new ASTIdNode(value.image);
      {if (true) return node;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode ScopeExpression() throws ParseException {
 List<ASTDefineNode> defines;
JasvaASTNode scope;
    jj_consume_token(DEF);
    defines = DefinitionList();
    jj_consume_token(IN);
    scope = CompoundStatement();
    jj_consume_token(END);
   {if (true) return new ASTLetNode(defines, scope);}
    throw new Error("Missing return statement in function");
  }

  final public List<ASTDefineNode> DefinitionList() throws ParseException {
 List<ASTDefineNode> defs = new LinkedList<ASTDefineNode>();
ASTDefineNode cur_def;
    cur_def = Definition();
                          defs.add(cur_def);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COM:
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_6;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COM:
        jj_consume_token(COM);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
      cur_def = Definition();
                                                                               defs.add(cur_def);
    }
   {if (true) return defs;}
    throw new Error("Missing return statement in function");
  }

  final public ASTDefineNode Definition() throws ParseException {
 Token varID;
Type type;
JasvaASTNode varVal;
    varID = jj_consume_token(IDENTIFIER);
    jj_consume_token(COLN);
    type = ExpType();
    jj_consume_token(DEFINE);
    varVal = CompoundStatement();
   {if (true) return new ASTDefineNode(varID.image, type, varVal);}
    throw new Error("Missing return statement in function");
  }

  final public Type ExpType() throws ParseException {
 Type innerType;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
            {if (true) return new IntType();}
      break;
    case BOL:
      jj_consume_token(BOL);
            {if (true) return new BoolType();}
      break;
    case REF:
      jj_consume_token(REF);
      innerType = ExpType();
                                  {if (true) return new RefType(innerType);}
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode ReferenceExpression() throws ParseException {
 JasvaASTNode exp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NEW:
      jj_consume_token(NEW);
      exp = PrimaryExpression();
                                     exp = new ASTNewRefNode(exp);
      break;
    case DEREF:
      jj_consume_token(DEREF);
      exp = PrimaryExpression();
                                       exp = new ASTDerefNode(exp);
      break;
    default:
      jj_la1[10] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode BranchingExpression() throws ParseException {
 JasvaASTNode cond, t_block, f_block = null;
    jj_consume_token(IF);
    cond = CompoundStatement();
    jj_consume_token(THN);
    t_block = CompoundStatement();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ELS:
      jj_consume_token(ELS);
      f_block = CompoundStatement();
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    jj_consume_token(END);
   {if (true) return new ASTIfNode(cond, t_block, f_block);}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode IterationExpression() throws ParseException {
 JasvaASTNode cond, block;
    jj_consume_token(WHL);
    cond = CompoundStatement();
    jj_consume_token(DO);
    block = CompoundStatement();
    jj_consume_token(END);
   {if (true) return new ASTWhileNode(cond, block);}
    throw new Error("Missing return statement in function");
  }

  final public JasvaASTNode PrintExpression() throws ParseException {
 JasvaASTNode node = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PRL:
      jj_consume_token(PRL);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DEF:
      case IF:
      case WHL:
      case NEW:
      case PRT:
      case PRL:
      case MO_POS:
      case MO_NEG:
      case DEREF:
      case LO_NOT:
      case L_PAR:
      case INTEGER_LITERAL:
      case BOOLEAN_LITERAL:
      case IDENTIFIER:
        node = TranslationUnit();
        break;
      default:
        jj_la1[12] = jj_gen;
        ;
      }
                                                    {if (true) return new ASTPrintNode(node, true);}
      break;
    case PRT:
      jj_consume_token(PRT);
      node = TranslationUnit();
                                     {if (true) return new ASTPrintNode(node, false);}
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_3R_41() {
    if (jj_scan_token(DEREF)) return true;
    return false;
  }

  private boolean jj_3R_40() {
    if (jj_scan_token(NEW)) return true;
    return false;
  }

  private boolean jj_3R_34() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_40()) {
    jj_scanpos = xsp;
    if (jj_3R_41()) return true;
    }
    return false;
  }

  private boolean jj_3_3() {
    if (jj_scan_token(LO_AND)) return true;
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3R_17() {
    if (jj_3R_15()) return true;
    return false;
  }

  private boolean jj_3R_14() {
    if (jj_scan_token(CMP_GE)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3R_13() {
    if (jj_scan_token(CMP_GT)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_scan_token(LO_OR)) return true;
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3R_12() {
    if (jj_scan_token(CMP_LE)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3R_11() {
    if (jj_scan_token(CMP_LT)) return true;
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3_5() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_11()) {
    jj_scanpos = xsp;
    if (jj_3R_12()) {
    jj_scanpos = xsp;
    if (jj_3R_13()) {
    jj_scanpos = xsp;
    if (jj_3R_14()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_3R_17()) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_3R_10()) return true;
    return false;
  }

  private boolean jj_3R_33() {
    if (jj_scan_token(DEF)) return true;
    return false;
  }

  private boolean jj_3R_8() {
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3R_32() {
    if (jj_scan_token(IDENTIFIER)) return true;
    return false;
  }

  private boolean jj_3R_7() {
    if (jj_3R_8()) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(ASSIGN)) return true;
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_39() {
    if (jj_scan_token(BOOLEAN_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_38() {
    if (jj_scan_token(INTEGER_LITERAL)) return true;
    return false;
  }

  private boolean jj_3R_31() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_38()) {
    jj_scanpos = xsp;
    if (jj_3R_39()) return true;
    }
    return false;
  }

  private boolean jj_3R_30() {
    if (jj_3R_37()) return true;
    return false;
  }

  private boolean jj_3R_29() {
    if (jj_3R_36()) return true;
    return false;
  }

  private boolean jj_3R_28() {
    if (jj_3R_35()) return true;
    return false;
  }

  private boolean jj_3R_27() {
    if (jj_3R_34()) return true;
    return false;
  }

  private boolean jj_3R_26() {
    if (jj_3R_33()) return true;
    return false;
  }

  private boolean jj_3_7() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(21)) {
    jj_scanpos = xsp;
    if (jj_scan_token(22)) {
    jj_scanpos = xsp;
    if (jj_scan_token(23)) return true;
    }
    }
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_25() {
    if (jj_3R_32()) return true;
    return false;
  }

  private boolean jj_3R_24() {
    if (jj_3R_31()) return true;
    return false;
  }

  private boolean jj_3R_23() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_24()) {
    jj_scanpos = xsp;
    if (jj_3R_25()) {
    jj_scanpos = xsp;
    if (jj_3R_26()) {
    jj_scanpos = xsp;
    if (jj_3R_27()) {
    jj_scanpos = xsp;
    if (jj_3R_28()) {
    jj_scanpos = xsp;
    if (jj_3R_29()) {
    jj_scanpos = xsp;
    if (jj_3R_30()) return true;
    }
    }
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3_6() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(19)) {
    jj_scanpos = xsp;
    if (jj_scan_token(20)) return true;
    }
    if (jj_3R_15()) return true;
    return false;
  }

  private boolean jj_3R_22() {
    if (jj_3R_23()) return true;
    return false;
  }

  private boolean jj_3R_21() {
    if (jj_scan_token(L_PAR)) return true;
    return false;
  }

  private boolean jj_3R_20() {
    if (jj_scan_token(LO_NOT)) return true;
    return false;
  }

  private boolean jj_3R_19() {
    if (jj_scan_token(MO_POS)) return true;
    return false;
  }

  private boolean jj_3R_18() {
    if (jj_scan_token(MO_NEG)) return true;
    return false;
  }

  private boolean jj_3R_43() {
    if (jj_scan_token(PRT)) return true;
    return false;
  }

  private boolean jj_3R_42() {
    if (jj_scan_token(PRL)) return true;
    return false;
  }

  private boolean jj_3R_37() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_42()) {
    jj_scanpos = xsp;
    if (jj_3R_43()) return true;
    }
    return false;
  }

  private boolean jj_3R_16() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_18()) {
    jj_scanpos = xsp;
    if (jj_3R_19()) {
    jj_scanpos = xsp;
    if (jj_3R_20()) {
    jj_scanpos = xsp;
    if (jj_3R_21()) {
    jj_scanpos = xsp;
    if (jj_3R_22()) return true;
    }
    }
    }
    }
    return false;
  }

  private boolean jj_3R_36() {
    if (jj_scan_token(WHL)) return true;
    return false;
  }

  private boolean jj_3R_15() {
    if (jj_3R_16()) return true;
    return false;
  }

  private boolean jj_3R_35() {
    if (jj_scan_token(IF)) return true;
    return false;
  }

  private boolean jj_3_4() {
    if (jj_scan_token(CMP_EQ)) return true;
    if (jj_3R_10()) return true;
    return false;
  }

  /** Generated Token Manager. */
  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[14];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x0,0xf0000000,0x180000,0xe00000,0x41e14a0,0x40614a0,0x0,0x0,0x0,0x1c000,0x4001000,0x200,0x41e14a0,0x60000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x10,0x0,0x0,0x0,0x1a44,0x1a00,0xa00,0x1020,0x20,0x0,0x0,0x0,0x1a44,0x0,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[7];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 14; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        exists = true;
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.add(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[47];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 14; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 47; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 7; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
