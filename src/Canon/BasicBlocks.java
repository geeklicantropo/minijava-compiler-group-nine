package Canon;

import IRTree2.*;
import temp.Label;

public class BasicBlocks {
  public StmList blocks;
  public Label done;

  private StmList lastBlock;
  private StmList lastStm;

  private void addStm(Stm s) {
	lastStm = lastStm.tail = new StmList(s,null);
  }

  private void doStms(StmList l) {
      if (l==null) 
	doStms(new StmList(new JUMP(done), null));
      else if (l.head instanceof JUMP 
	      || l.head instanceof CJUMP) {
	addStm(l.head);
	mkBlocks(l.tail);
      } 
      else if (l.head instanceof LABEL)
           doStms(new StmList(new JUMP(((LABEL)l.head).label), 
	  			   l));
      else {
	addStm(l.head);
	doStms(l.tail);
      }
  }

  void mkBlocks(StmList l) {
     if (l==null) return;
     else if (l.head instanceof LABEL) {
	lastStm = new StmList(l.head,null);
        if (lastBlock==null)
  	   lastBlock= blocks= new StmList(null,lastStm);
        else
  	   lastBlock = lastBlock.tail = new StmList(null,lastStm);
	doStms(l.tail);
     }
     else mkBlocks(new StmList(new LABEL(new Label()), l));
  }
   

  public BasicBlocks(StmList stms) {
    done = new Label();
    mkBlocks(stms);
  }
}
