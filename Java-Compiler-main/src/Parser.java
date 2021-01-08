/* Generated By:JavaCC: Do not edit this line. Parser.java */
import ast.*;
import ast.binary.*;
import ast.special.*;
import ast.unary.*;
import util.Pair;

import java.util.LinkedList;
import java.util.List;

public class Parser implements ParserConstants {

  static final public ASTNode Start() throws ParseException {
 ASTNode root;
    root = TranslationUnit();
    jj_consume_token(EXP_DEL);
     {if (true) return root;}
    throw new Error("Missing return statement in function");
  }

  static final public ASTNode TranslationUnit() throws ParseException {
 ASTNode tu_node;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ADD:
    case SUB:
    case LPAR:
    case INTEGER_LITERAL:
    case IDENTIFIER:
      tu_node = AdditiveExpression();
      break;
    case DEF:
      tu_node = Declaration();
      break;
        case NEW:
            tu_node = Declaration();
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
     {if (true) return tu_node;}
    throw new Error("Missing return statement in function");
  }

  static final public ASTNode AdditiveExpression() throws ParseException {
 Token op;
ASTNode lhs, rhs;
    lhs = MultiplicativeExpression();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ADD:
      case SUB:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ADD:
        op = jj_consume_token(ADD);
        break;
      case SUB:
        op = jj_consume_token(SUB);
        break;
      default:
        jj_la1[2] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      rhs = MultiplicativeExpression();
         lhs = op.kind == ADD ? new ASTAddNode(lhs, rhs) : new ASTSubNode(lhs, rhs);
    }
     {if (true) return lhs;}
    throw new Error("Missing return statement in function");
  }

  static final public ASTNode MultiplicativeExpression() throws ParseException {
 Token op;
ASTNode lhs, rhs;
    lhs = BooleanExpression();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DIV:
      case MOD:
      case MULT:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case MULT:
        op = jj_consume_token(MULT);
        break;
      case DIV:
        op = jj_consume_token(DIV);
        break;
      case MOD:
        op = jj_consume_token(MOD);
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      rhs = BooleanExpression();
 switch(op.kind) {
    case MULT: lhs = new ASTMultNode(lhs, rhs); break;
    case DIV : lhs = new ASTDivNode(lhs, rhs); break;
    case MOD : lhs = new ASTModNode(lhs, rhs); break;
}
    }
     {if (true) return lhs;}
    throw new Error("Missing return statement in function");
  }
    static final public ASTNode BooleanExpression() throws ParseException {
        Token op;
        ASTNode lhs, rhs;
        lhs = UnaryExpression();
        label_3:
        while (true) {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
                case DIV:
                case MOD:
                case MULT:
                    ;
                    break;
                default:
                    jj_la1[3] = jj_gen;
                    break label_3;
            }
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
                case GREATER:
                    op = jj_consume_token(GREATER);
                    break;
                case GREATEROREQ:
                    op = jj_consume_token(GREATEROREQ);
                    break;
                case LOWER:
                    op = jj_consume_token(LOWER);
                    break;
                case LOWEROREQ:
                    op = jj_consume_token(LOWEROREQ);
                    break;
                case AND:
                    op = jj_consume_token(AND);
                    break;
                case OR:
                    op = jj_consume_token(OR);
                    break;
                case NEW:
                    op = jj_consume_token(NEW);
                default:
                    jj_la1[4] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            rhs = UnaryExpression();
            switch(op.kind) {
                case GREATER: lhs = new ASTGreat(lhs, rhs); break;
                case GREATEROREQ : lhs = new ASTOr(new ASTGreat(lhs, rhs),new ASTEq(lhs,rhs)); break;
                case LOWER : lhs = new ASTLower(lhs, rhs); break;
                case LOWEROREQ : lhs = new ASTOr(new ASTLower(lhs, rhs),new ASTEq(lhs,rhs)); break;
                case AND : lhs = new ASTAnd(lhs, rhs); break;
                case OR: lhs = new ASTOr(lhs, rhs); break;
                case NEW: lhs=new ASTAssign(lhs,rhs);break;
            }
        }
        {if (true) return lhs;}
        throw new Error("Missing return statement in function");
    }
  static final public ASTNode UnaryExpression() throws ParseException {
 ASTNode exp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SUB:
      jj_consume_token(SUB);
      exp = UnaryExpression();
                                    exp = new ASTNegNode(exp);
      break;
    case ADD:
      jj_consume_token(ADD);
      exp = UnaryExpression();
                                    exp = new ASTPosNode(exp);
      break;
    case LPAR:
    case INTEGER_LITERAL:
    case IDENTIFIER:
      exp = PrimaryExpression();
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  static final public ASTNode PrimaryExpression() throws ParseException {
 Token val;
ASTNode exp;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_LITERAL:
      val = jj_consume_token(INTEGER_LITERAL);
                              exp = new ASTNumNode(Integer.parseInt(val.image));
      break;
    case IDENTIFIER:
      val = jj_consume_token(IDENTIFIER);
                         exp = new ASTVarRefNode(val.image);
      break;
    case LPAR:
      jj_consume_token(LPAR);
      exp = TranslationUnit();
      jj_consume_token(RPAR);
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return exp;}
    throw new Error("Missing return statement in function");
  }

  static final public ASTNode Declaration() throws ParseException {
 ASTNode scope;
Pair<String, ASTNode> curAssign;
List<Pair<String, ASTNode>> assignments = new LinkedList();
    jj_consume_token(DEF);
    curAssign = AssignmentExpression();
                                              assignments.add(curAssign);
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IDENTIFIER:
      case 21:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_3;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
        jj_consume_token(21);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
      curAssign = AssignmentExpression();
                                                assignments.add(curAssign);
    }
    jj_consume_token(IN);
    scope = TranslationUnit();
    jj_consume_token(END);
     {if (true) return new ASTScopeNode(assignments, scope);}
    throw new Error("Missing return statement in function");
  }




  static final public Pair<String, ASTNode> AssignmentExpression() throws ParseException {
 Token symbol;
ASTNode value;
    symbol = jj_consume_token(IDENTIFIER);
    jj_consume_token(ASSIGN);
    value = TranslationUnit();
     {if (true) return new Pair<String, ASTNode>(symbol.image, value);}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public ParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[9];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x64460,0x60,0x60,0x380,0x380,0x60460,0x60400,0x240000,0x200000,};
   }

  /** Constructor with InputStream. */
  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Parser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Parser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Parser(ParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 9; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[22];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 9; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 22; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

                      }
