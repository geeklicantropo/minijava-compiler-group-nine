package Canon;
import IRTree2.*;
import temp.Temp;

abstract class MoveCall extends Stm {
  TEMP dst;
  CALL src;
  MoveCall(TEMP d, CALL s) {dst=d; src=s;}
  public ExpList kids() {return src.kids();}
  public Stm build(ExpList kids) {
	return new MOVE(dst, src.build(kids));
  }
}   
  
abstract class ExpCall extends Stm {
  CALL call;
  ExpCall(CALL c) {call=c;}
  public ExpList kids() {return call.kids();}
  public Stm build(ExpList kids) {
	return new ExpStm(call.build(kids));
  }
}   
  
class StmExpList {
  Stm stm;
  ExpList exps;
  StmExpList(Stm s, ExpList e) {stm=s; exps=e;}
}

public class Canon {
  
 static boolean isNop(Stm a) {
   return a instanceof ExpStm
          && ((ExpStm)a).exp instanceof CONST;
 }

 static Stm seq(Stm a, Stm b) {
    if (isNop(a)) return b;
    else if (isNop(b)) return a;
    else return new SEQ(a,b);
 }

 static boolean commute(Stm a, Exp b) {
    return isNop(a)
        || b instanceof NAME
        || b instanceof CONST;
 }

 static Stm do_stm1(ExpStm s) { 
	return seq(do_stm1(s.left), do_stm1(s.right));
 }

 static Stm do_stm(MOVE s) { 
	if (s.dst instanceof TEMP 
	     && s.src instanceof CALL) 
		return reorder_stm( MoveCall((TEMP)s.dst,
						(CALL)s.src));
	else if (s.dst instanceof ESEQ)
	    return do_stm(new SEQ(((ESEQ)s.dst).stm,
					new MOVE(((ESEQ)s.dst).exp,
						  s.src)));
	else return reorder_stm(s);
 }

 private static Stm MoveCall(TEMP dst, CALL src) {

	return null;
}

static Stm do_stm(ExpStm s) { 
	if (s.exp instanceof CALL)
	       return reorder_stm( ExpCall((CALL)s.exp));
	else return reorder_stm(s);
 }

 private static Stm ExpCall(CALL exp) {

	return null;
}

static Stm do_stm(Stm s) {
     if (s instanceof SEQ) return do_stm((SEQ)s);
     else if (s instanceof MOVE) return do_stm((MOVE)s);
     else if (s instanceof ExpStm) return do_stm1((ExpStm)s);
     else return reorder_stm(s);
 }

 static Stm reorder_stm(Stm s) {
     StmExpList x = reorder(s.kids());
     return seq(x.stm, s.build(x.exps));
 }

 static ESEQ do_exp(ESEQ e) {
      Stm stms = do_stm(e.stm);
      ESEQ b = do_exp(e.exp);
      return new ESEQ(seq(stms,b.stm), b.exp);
  }

 static ESEQ do_exp (Exp e) {
       if (e instanceof ESEQ) return do_exp((ESEQ)e);
       else return reorder_exp(e);
 }
         
 static ESEQ reorder_exp (Exp e) {
     StmExpList x = reorder(e.kids());
     return new ESEQ(x.stm, e.build(x.exps));
 }

 static StmExpList nopNull = new StmExpList(new ExpStm(new CONST(0)),null);

 static StmExpList reorder(ExpList exps) {
     if (exps==null) return nopNull;
     else {
       Exp a = exps.head;
       if (a instanceof CALL) {
         Temp t = new Temp();
	 Exp e = new ESEQ(new MOVE(new TEMP(t), a),
				    new TEMP(t));
         return reorder(new ExpList(e, exps.tail));
       } else {
	 ESEQ aa = do_exp(a);
	 StmExpList bb = reorder(exps.tail);
	 if (commute(bb.stm, aa.exp))
	      return new StmExpList(seq(aa.stm,bb.stm), 
				    new ExpList(aa.exp,bb.exps));
	 else {
	   Temp t = new Temp();
	   return new StmExpList(
			  seq(aa.stm, 
			    seq(new MOVE(new TEMP(t),aa.exp),
				 bb.stm)),
			  new ExpList(new TEMP(t), bb.exps));
	 }
       }
     }
 }
        
 static StmList linear(SEQ s, StmList l) {
      return linear(s.left,linear(s.right,l));
 }
 static StmList linear(Stm s, StmList l) {
    if (s instanceof SEQ) return linear((SEQ)s, l);
    else return new StmList(s,l);
 }

 static public StmList linearize(Stm s) {
    return linear(do_stm(s), null);
 }
}
