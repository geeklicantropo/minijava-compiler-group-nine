package translate;

public abstract class Cx extends Exp {

        public IRTree2.Exp unEx() {
                temp.Temp r = new temp.Temp();
                temp.Label t = new temp.Label();
                temp.Label f = new temp.Label();

                return new IRTree2.ESEQ(new IRTree2.SEQ(new IRTree2.MOVE(
                                new IRTree2.TEMP(r), new IRTree2.CONST(1)), new IRTree2.SEQ(unCx(
                                t, f), new IRTree2.SEQ(new IRTree2.LABEL(f), new IRTree2.SEQ(
                                new IRTree2.MOVE(new IRTree2.TEMP(r), new IRTree2.CONST(0)),
                                new IRTree2.LABEL(t))))), new IRTree2.TEMP(r));
        }

        public abstract IRTree2.Stm unCx(temp.Label t, temp.Label f);

        public IRTree2.Stm unNx() {
                System.err.println("ERROR:  In well-typed MiniJava, (Cx c).unNx() should never be used.");
                return null;
        }
}
