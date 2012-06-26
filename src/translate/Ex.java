package translate;

import temp.Label;
import IRTree2.CJUMP;
import IRTree2.CONST;
import IRTree2.Stm;

public class Ex extends Exp
{
    IRTree2.Exp exp;
    
    Ex(IRTree2.Exp e)
    {
        super();        
        exp = e;
    }
    
     public   IRTree2.Exp unEx()
    {    
        return exp;
    }

    
     public  Stm unNx()
    {
        return new IRTree2.ExpStm(exp);
    }

    
     public Stm unCx(Label t, Label f)
    {
        return new CJUMP(CJUMP.EQ, exp, new CONST(0), f, t);
    }


}
