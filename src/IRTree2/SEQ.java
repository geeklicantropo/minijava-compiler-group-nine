package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;
import IRTree2.ExpList;
import IRTree2.Stm;

public class SEQ extends Stm
{
  public Stm left, right;

  public SEQ(Stm l, Stm r)
  {
    left = l;
    right = r;
  }

  public ExpList kids()
  {
    throw new Error("kids() not applicable to SEQ");
  }

  public Stm build(ExpList kids)
  {
    throw new Error("build() not applicable to SEQ");
  }

  public String accept(StringVisitor v)
  {
    return v.visit(this);
  }

  public void accept(IntVisitor v, int d)
  {
    v.visit(this, d);
  }

  public void accept(TempVisitor v)
  {
    v.visit(this);
  }

}
