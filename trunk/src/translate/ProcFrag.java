package translate;

public class ProcFrag extends Frag
{
  public IRTree2.Stm    body;
  public Frame.Frame frame;
  public ProcFrag(IRTree2.Stm b, Frame.Frame f)
  {
    body = b;
    frame = f;
  }
}
