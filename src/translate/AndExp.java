package translate;

public class AndExp extends Exp
{

  temp.Label label = new temp.Label();
  Exp        left;
  Exp        right;

  public AndExp(Exp l, Exp r)
  {
    left = l;
    right = r;
  }

  public IRTree2.Exp unEx()
  {
    temp.Temp r = new temp.Temp();
    temp.Label t = new temp.Label();
    temp.Label f = new temp.Label();

    return new IRTree2.ESEQ(new IRTree2.SEQ(new IRTree2.MOVE(new IRTree2.TEMP(r), new IRTree2.CONST(1)), new IRTree2.SEQ(left.unCx(label,
        f), new IRTree2.SEQ(new IRTree2.LABEL(label), new IRTree2.SEQ(right.unCx(t, f), new IRTree2.SEQ(new IRTree2.LABEL(f),
        new IRTree2.SEQ(new IRTree2.MOVE(new IRTree2.TEMP(r), new IRTree2.CONST(0)), new IRTree2.LABEL(t))))))), new IRTree2.TEMP(r));
  }

  public IRTree2.Stm unCx(temp.Label t, temp.Label f)
  {
    return new IRTree2.SEQ(left.unCx(label, f), new IRTree2.SEQ(new IRTree2.LABEL(label), right.unCx(t, f)));
  }

  public IRTree2.Stm unNx()
  {
    System.err.println("ERROR:  In well-typed MiniJava, (AndExp a).unNx() should never be used.");
    return null;
  }
}

