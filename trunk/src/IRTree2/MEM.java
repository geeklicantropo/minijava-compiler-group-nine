package IRTree2;

import IRvisitor.IntVisitor;
import IRvisitor.StringVisitor;
import IRvisitor.TempVisitor;
import temp.Temp;
import IRTree2.Exp;
import IRTree2.ExpList;
import IRTree2.MEM;

public class MEM extends Exp {
        public Exp exp;

        public MEM(Exp e) {
                exp = e;
        }

        public ExpList kids() {
                return new ExpList(exp, null);
        }

        public Exp build(ExpList kids) {
                return new MEM(kids.head);
        }

        public String accept(StringVisitor v) {
                return v.visit(this);
        }

        public void accept(IntVisitor v, int d) {
                v.visit(this, d);
        }

        public Temp accept(TempVisitor v) {
                return v.visit(this);
        }

}