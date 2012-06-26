package Canon;

import IRTree2.*;
import temp.Label;

public class TraceSchedule {

	public StmList stms;
	BasicBlocks theBlocks;
	java.util.Dictionary table = new java.util.Hashtable();

	StmList getLast(StmList block) {
		StmList l = block;
		while (l.tail.tail != null)
			l = l.tail;
		return l;
	}

	void trace(StmList l) {

	}

	StmList getNext() {
		if (theBlocks.blocks == null)
			return new StmList(new LABEL(theBlocks.done), null);

		else {
			theBlocks.blocks = theBlocks.blocks.tail;
			return getNext();
		}
	}

	public TraceSchedule(BasicBlocks b) {
		theBlocks = b;
		for (StmList l = b.blocks; l != null; l = l.tail)
			table.put(((LABEL) l.head).label, l.head);
		stms = getNext();
		table = null;
	}
}
