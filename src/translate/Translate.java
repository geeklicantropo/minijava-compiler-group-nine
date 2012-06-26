package translate;

import syntaxtree.*;
import visitor.ExpVisitor;
import visitor.TypeVisitor;
import visitor.Visitor;

import java.util.HashMap;
import Frame.Access;
import temp.Label;
import temp.Temp;
import IRTree2.ESEQ;
import IRTree2.ExpList;
import IRTree2.MOVE;
import IRTree2.TEMP;

public class Translate implements ExpVisitor
{
  IRTree2.Exp e1 = null;
  
  private Frag   frags     = null;
  private Frag   frags_tail = null;
  private Frame.Frame              currFrame = null;
  private IRTree2.Exp                 objPtr    = null;
  private int                      offset;
  private HashMap<String, Integer> fieldVars = null;
  private HashMap<String, Access>  vars      = null;

  public Translate(TProgram p, Frame.Frame f)
  {
    currFrame = f;
    p.accept(this);
  }

  public void procEntryExit(IRTree2.Stm body)
  {
    ProcFrag newfrag = new ProcFrag(body, currFrame);
    if (frags == null)
      frags = newfrag;
    else
      frags_tail.next = newfrag;
    frags_tail = newfrag;
    
  }

  public Frag getResults()
  {
    return frags;
  }

  public void printResults()
  {
    IRTree.Print p = new IRTree.Print(System.out);
    Frag f = frags;
    while (f != null)
      {
        System.out.println();
        System.out.println("Function: " + ((ProcFrag) f).frame.name.toString());
        p.prStm(((ProcFrag) f).body);
        f = f.next;
      }
  }

  public Exp visit(Program n)
  {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++)
      n.cl.elementAt(i).accept(this);
    return null;
  }

  public Exp visit(TMainClass n)
  {  
        Frame.Frame newFrame = currFrame.newFrame("main", 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    IRTree2.Stm s = (n.s.accept(this)).unNx();

    IRTree2.Exp retExp = new IRTree2.CONST(0);
    IRTree2.Stm body = new IRTree2.MOVE(new IRTree2.TEMP(currFrame.RV()), new IRTree2.ESEQ(s, retExp));

    procEntryExit(body);
    currFrame = oldFrame;

    return null;
  }

  public Exp visit(TClassDeclSimple n)
  {
    for (int i=0; i < n.ml.size(); i++)
      n.ml.elementAt(i).accept(this);
    return null;
  }

  public Exp visit(TClassDeclExtends n)
  {
    for (int i=0; i < n.ml.size(); i++)
      n.ml.elementAt(i).accept(this);
    
    return null;
  }

  public Exp visit(TVarDecl n)
  {
    Access ac = currFrame.allocLocal(false);
    return new Nx(new IRTree2.MOVE(ac.exp(new TEMP(currFrame.FP())),new IRTree2.CONST(0)));
  }

  public Exp visit(TMethodDecl n)
  {
    Frame.Frame newFrame = currFrame.newFrame(n.i.toString(), n.fl.size() + 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    for (int i = 0; i < n.vl.size(); i++)
      n.vl.elementAt(i).accept(this);

    /* ADD CODE: move formals to fresh temps and add them to the HashMap vars */

    for (int i = 0; i < n.sl.size(); i++)
      n.sl.elementAt(i).accept(this);
    
    /* ADD CODE: set value of IRTree.Exp objPtr
     Recall that objPtr is a pointer to the address in memory at which 
     instance variables are stored for the current class 
     (i.e., it is "this").
     In the MiniJava compiler, it is passed as an argument during all
     calls to MiniJava methods. */

    /* ADD CODE: visit each statement in method body, 
     creating new IRTree.SEQ nodes as needed */
    IRTree2.Stm body = null; // FILL IN

    /* ADD CODE: get return expression and group with statements of body,
     then create IRTree.MOVE to store return value */

    /* create new procedure fragment for method and add to list */
    procEntryExit(body);
    currFrame = oldFrame;
    vars = null;
    objPtr = null;

    return null;
  }

  public Exp visit(TFormal n)
  {
    return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TTypeIntegerArray n)
  {
    return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TTypeBoolean n)
  {
    return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TTypeInteger n)
  {
    return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TTypeIdentifier n)
  {
    return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TStatementBlock n)
  {
          for ( int i = 0; i < n.sl.size(); i++ ) {
                n.sl.elementAt(i).accept(this);
          }
          return null;
      //return new Nx(stm);
  }

  public Exp visit(TStatementIf n)
  {
          Label T = new Label();
      Label F = new Label();
      Label D = new Label();
      Exp exp =  n.e.accept(this);
      Exp stmT =  n.s1.accept(this);
      Exp stmF =  n.s2.accept(this);
      return new Nx(new IRTree2.SEQ
                     (new IRTree2.SEQ
                      (new IRTree2.SEQ
                       (new IRTree2.SEQ
                        (new IRTree2.CJUMP(IRTree2.CJUMP.EQ, exp.unEx(),new IRTree2.CONST(1),T,F),
                                        new IRTree2.SEQ(new IRTree2.LABEL(T),stmT.unNx())),
                                        new IRTree2.JUMP(D)),
                                        new IRTree2.SEQ(new IRTree2.LABEL(F),stmF.unNx())),
                                        new IRTree2.LABEL(D)));

  }

  public Exp visit(TStatementWhile n)
  {
          Label test = new Label();
      Label T = new Label();
      Label F = new Label();
      Exp exp = n.e.accept(this);
      Exp body = n.s.accept(this);
      
      return new Nx(new IRTree2.SEQ
                     (new IRTree2.SEQ
                      (new IRTree2.SEQ(new IRTree2.LABEL(test),
                           (new IRTree2.CJUMP(IRTree2.CJUMP.EQ, exp.unEx(), 
                                           new IRTree2.CONST(1),T,F))),
                       (new IRTree2.SEQ( new IRTree2.LABEL(T),body.unNx()))),
                       new IRTree2.LABEL(F)));

  }

  public Exp visit(TStatementPrint n)
  {
          if (e1 != null)

          {
          e1 = (n.e.accept(this)).unEx();
          }
          return new Ex(currFrame.externalCall("printInt", new IRTree2.ExpList(e1,null) ) );
  }

  public Exp visit(TStatementAssign n)
  {  
    IRTree2.Exp i = n.i.accept(this).unEx();
    IRTree2.Exp e = n.e.accept(this).unEx();
    
    return new Nx(new IRTree2.MOVE(i, e));
  }

  public Exp visit(TStatementArrayAssign n)
  {
        IRTree2.Exp e1 = (n.e1.accept(this)).unEx();
        IRTree2.Exp e2 = (n.e2.accept(this)).unEx();
        IRTree2.Exp expId = (n.i.accept(this)).unEx();
        return new Nx(new IRTree2.MOVE(new IRTree2.BINOP(IRTree2.BINOP.PLUS,new IRTree2.MEM(expId), new IRTree2.BINOP(IRTree2.BINOP.MUL,e1,new IRTree2.CONST(4))), e2)); 
  }

  public Exp visit(AndExp n)
  {
          Temp t1 = new Temp();
      Label done = new Label();
      Label ok1 = new Label();
      Label ok2 = new Label();
      
      IRTree2.Exp left =  n.e1.accept(this).unEx();
      IRTree2.Exp right = n.e2.accept(this).unEx();
      return new Ex
          (new IRTree2.ESEQ(new IRTree2.SEQ
                (new IRTree2.SEQ
                 (new IRTree2.SEQ
                  (new IRTree2.SEQ (new IRTree2.SEQ (new IRTree2.MOVE(new IRTree2.TEMP(t1),new IRTree2.CONST(0)),
                                  new IRTree2.CJUMP(IRTree2.CJUMP.EQ, left, new IRTree2.CONST(1), ok1, done)),
                                  new IRTree2.SEQ(new IRTree2.LABEL(ok1), 
                                                  new IRTree2.CJUMP(IRTree2.CJUMP.EQ, right, new IRTree2.CONST(1), ok2, done))), 
                            new IRTree2.SEQ(new IRTree2.LABEL(ok2),  new IRTree2.MOVE(new IRTree2.TEMP(t1),new IRTree2.CONST(1)))),
                            new IRTree2.JUMP(done)),
                            new IRTree2.LABEL(done)),
                            new IRTree2.TEMP(t1)));

  }

  public Exp visit(TExpOpLessThan n)
  {
          Exp expl= n.e1.accept(this);
      Exp expr= n.e2.accept(this);
      Label T = new Label();
      Label F = new Label();
      Temp t = new Temp();
      return new Ex
          (new IRTree2.ESEQ(new IRTree2.SEQ
                (new IRTree2.SEQ
                 (new IRTree2.SEQ
                  (new IRTree2.MOVE(new IRTree2.TEMP(t),new IRTree2.CONST(0)),
                                  new IRTree2.CJUMP(IRTree2.CJUMP.LT,expl.unEx(),expr.unEx(),T,F)),
                                  new IRTree2.SEQ(new IRTree2.LABEL(T), new IRTree2.MOVE(new IRTree2.TEMP(t),new IRTree2.CONST(1)))),
                                  new IRTree2.LABEL(F)),
                                  new IRTree2.TEMP(t)))  ;
  }

  public Exp visit(TExpOpPlus n)
  {
        IRTree2.Exp exp1 = (n.e1.accept(this)).unEx();
        IRTree2.Exp exp2 = (n.e2.accept(this)).unEx();
        return new Ex(new IRTree2.BINOP(IRTree2.BINOP.PLUS, exp1, exp2));
  }

  public Exp visit(TExpOpMinus n)
  {
        IRTree2.Exp exp1 = (n.e1.accept(this)).unEx();
        IRTree2.Exp exp2 = (n.e2.accept(this)).unEx();
        return new Ex(new IRTree2.BINOP(IRTree2.BINOP.MINUS, exp1, exp2));
  }

  public Exp visit(TExpOpTimes n)
  {
        IRTree2.Exp exp1 = (n.e1.accept(this)).unEx();
        IRTree2.Exp exp2 = (n.e2.accept(this)).unEx();
        return new Ex(new IRTree2.BINOP(IRTree2.BINOP.MUL, exp1, exp2));
  }

  public Exp visit(TExpArrayLookup n)
  {
          Temp t_index = new Temp();
      Temp t_size = new Temp();
      IRTree2.Exp e1 = n.e1.accept(this).unEx();
      IRTree2.Exp e2 = n.e2.accept(this).unEx();

      Label F = new Label();
      Label T = new Label();
      
      IRTree2.ExpList args1 = new ExpList(e2, null);      
      
      IRTree2.Stm s1 = 
          new IRTree2.SEQ
          (new IRTree2.SEQ
           (new IRTree2.SEQ
            (new IRTree2.SEQ
             (new IRTree2.SEQ
              (new IRTree2.MOVE(new IRTree2.TEMP(t_index),new IRTree2.BINOP(IRTree2.BINOP.MUL,e2,new IRTree2.CONST(4))),
                          new IRTree2.MOVE(new IRTree2.TEMP(t_size),new IRTree2.MEM(e1))),
                          new IRTree2.CJUMP(IRTree2.CJUMP.GE,new IRTree2.TEMP(t_index),new IRTree2.TEMP(t_size),T,F)),
                          new IRTree2.LABEL(T)),
                          new IRTree2.MOVE(new IRTree2.TEMP(new Temp()),
                                          new IRTree2.CALL(new IRTree2.NAME(new Label("_error")),args1))),
                                          new IRTree2.LABEL(F));
          
      Temp t = new Temp();
      IRTree2.Stm s2 = new IRTree2.SEQ
          (s1,new IRTree2.MOVE(new IRTree2.TEMP(t),new IRTree2.MEM
                   (new IRTree2.BINOP(IRTree2.BINOP.PLUS,e1,new IRTree2.BINOP
                          (IRTree2.BINOP.PLUS,
                                          new IRTree2.BINOP(IRTree2.BINOP.MUL,e2,new IRTree2.CONST(4))
                           ,new IRTree2.CONST(4))))));
      return new Ex(new IRTree2.ESEQ(s2,new IRTree2.TEMP(t)));

  }

  public Exp visit(TExpArrayLength n)
  {
      n.e.accept(this);
      
      return null;
  }

  // TODO: checar retorno
  public Exp visit(TExpCall n)
  {
    ExpList el = null;
    for (int i=0; i< n.el.size(); i++){
      Exp ex = n.el.elementAt(i).accept(this);
      el = new ExpList (ex.unEx(),el);
    }

    return new Ex(new IRTree2.CALL(new IRTree2.NAME(new Label(n.e.accept(this).toString())),el)); 
  }

  public Exp visit(TExpIntegerLiteral n)
  {
        return new Ex(new IRTree2.CONST(n.i));
  }

  public Exp visit(TExpTrue n)
  {
         return new Ex(new IRTree2.CONST(1));
  }

  public Exp visit(TExpFalse n)
  {
          return new Ex(new IRTree2.CONST(0));
  }

  public Exp visit(TExpId n)
  {
    return new Ex(getIdTree(n.s));
  }

  public Exp visit(TExpThis n)
  {
    return new Ex(objPtr);
  }

  public Exp visit(TExpNewArray n)
  {
          IRTree2.Exp e = n.e.accept(this).unEx();
      ExpList params = new ExpList(e, null);
      Temp t = new Temp();
      
      return new Ex(new ESEQ(new MOVE(new TEMP(t), currFrame.externalCall("newArray", params)),
              new TEMP(t)));
  }

  public Exp visit(TExpNewObject n)
  {
    //TODO
    return null;
  }

  public Exp visit(TExpNot n)
  {
    return new Ex
           (new IRTree2.BINOP(IRTree2.BINOP.MINUS, new IRTree2.CONST(1), 
                  (n.e.accept(this)).unEx()));
  }

  public Exp visit(TIdentifier n)
  {
    return new Ex(new TEMP(currFrame.FP()));
  }

  private IRTree2.Exp getIdTree(String id)
  {
    Frame.Access a = null;
    
    try
      {
        a = vars.get(id);
        
        if (a == null)
        {
          int offset = fieldVars.get(id).intValue();
          return new IRTree2.MEM(new IRTree2.BINOP(IRTree2.BINOP.PLUS, objPtr, new IRTree2.CONST(offset)));
        }
        
        return a.exp(new IRTree2.TEMP(currFrame.FP()));
    }
    catch(Exception ex) { 
      return new IRTree2.MEM(new IRTree2.BINOP(IRTree2.BINOP.PLUS, objPtr, new IRTree2.CONST(0)));
    }    
  }
}
