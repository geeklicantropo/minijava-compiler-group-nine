package IRTree2;

import IRTree2.Stm;
import IRTree2.StmList;


public class StmList {

        public Stm     head;
          public StmList tail;

          /**
         * @param h
         * @param t
         */
        public StmList(Stm h, StmList t)
          {
            head = h;
            tail = t;
          }

}