package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;
import IRTree2.Exp;
import IRTree2.ExpList;
import temp.Temp;

public abstract class Exp {

        abstract public ExpList kids();

        abstract public Exp build(ExpList kids);

        abstract public String accept(StringVisitor v);

        abstract public void accept(IntVisitor v, int d);

        abstract public Temp accept(TempVisitor v);

}