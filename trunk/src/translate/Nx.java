package translate;

public class Nx extends Exp
{
  IRTree2.Stm stm;
  public Nx(IRTree2.Stm s)
  {
    stm = s;
  }
  public IRTree2.Stm unNx()
  {
    return stm;
  }
  public IRTree2.Exp unEx()
  {
    System.err.println("ERROR:  In well-typed MiniJava, (Nx n).unEx() should never be used.");
    return null;
  }
  public IRTree2.Stm unCx(temp.Label t, temp.Label f)
  {
    System.err.println("ERROR:  In well-typed MiniJava, (Nx n).unCx() should never be used.");
    return null;
  }
}

