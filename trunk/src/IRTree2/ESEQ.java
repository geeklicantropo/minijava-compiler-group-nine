package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;
import temp.Temp;
import IRTree2.Exp;
import IRTree2.ExpList;
import IRTree2.Stm;

public class ESEQ extends Exp
{
  public Stm stm;
  public Exp exp;

  public ESEQ(Stm s, Exp e)
  {
    stm = s;
    exp = e;
  }

  public ExpList kids()
  {
    throw new Error("kids() not applicable to ESEQ");
  }

  public Exp build(ExpList kids)
  {
    throw new Error("build() not applicable to ESEQ");
  }

  public String accept(StringVisitor v)
  {
    return v.visit(this);
  }
  
  public void accept(IntVisitor v, int d)
  {
    v.visit(this, d);
  }

  public Temp accept(TempVisitor v)
  {
    return v.visit(this);
  }

}