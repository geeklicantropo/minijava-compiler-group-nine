package translate;

public class RelCx extends Cx
{

  private int relop;
  IRTree2.Exp    left;
  IRTree2.Exp    right;

  public RelCx(int o, IRTree2.Exp l, IRTree2.Exp r)
  {
    relop = o;
    left = l;
    right = r;
  }

  public IRTree2.Stm unCx(temp.Label t, temp.Label f)
  {
    return new IRTree2.CJUMP(relop, left, right, t, f);
  }
}
