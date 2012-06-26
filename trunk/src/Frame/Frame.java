package Frame;

import temp.*;

public abstract class Frame implements TempMap
{

  public Label name;
  public AccessList formals;
  public abstract Frame newFrame(String name, int argCount);
  public abstract int wordSize();
  public abstract Access allocLocal(boolean escape);
  public abstract Temp FP();
  public abstract Temp RV();
  public abstract Temp RA();
  public abstract IRTree2.Exp externalCall(String func, IRTree2.ExpList args);
  public abstract String tempMap(Temp temp);
  //public abstract Assem.InstrList codegen(IRTree2.Stm s);
}