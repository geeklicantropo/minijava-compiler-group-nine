package translate;

public abstract class Exp {

	abstract IRTree2.Exp unEx();
    abstract IRTree2.Stm unNx();
    abstract IRTree2.Stm unCx(temp.Label t, temp.Label f);

}