package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;
import temp.Temp;
import IRTree2.Exp;
import IRTree2.ExpList;
import temp.Label;

public class NAME extends Exp
{
  public Label label;

  public NAME(Label l)
  {
    label = l;
  }

  public ExpList kids()
  {
    return null;
  }

  public Exp build(ExpList kids)
  {
    return this;
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