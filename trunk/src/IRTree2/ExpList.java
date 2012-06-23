package IRTree2;

import IRTree2.Exp;
import IRTree2.ExpList;

public class ExpList {

        public Exp head;
        public ExpList tail;

        public ExpList(Exp h, ExpList t) {
                head = h;
                tail = t;
        }
}