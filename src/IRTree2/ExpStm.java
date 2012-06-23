package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;

public class ExpStm extends Stm {

    public Exp exp; 

    public ExpStm(Exp e) {
            exp=e;
    }

    @Override
    public ExpList kids() {
            return new ExpList(exp,null);
    }

    @Override
    public Stm build(ExpList kids) {
            return new ExpStm(kids.head);
    }

   

        @Override
        public String accept(StringVisitor v) {
                // TODO Auto-generated method stub
                return null;
        }

        @Override
        public void accept(IntVisitor v, int d) {
                // TODO Auto-generated method stub
                
        }

        @Override
        public void accept(TempVisitor v) {
                // TODO Auto-generated method stub
                
        }
}
