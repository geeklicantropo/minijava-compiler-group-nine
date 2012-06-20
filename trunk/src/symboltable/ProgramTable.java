package symboltable;

import java.util.Hashtable;

public class ProgramTable {

		private final Hashtable<Symbol, ClassTable> programClasses = new Hashtable<Symbol, ClassTable>();

        public boolean addClass(Symbol className, ClassTable value) {
                if (programClasses.containsKey(className))
                        return false;
                else {
                        programClasses.put(className, value);
                        return true;
                }
        }

        public ClassTable getClass(Symbol className) {
                if (programClasses.containsKey(className))
                        return programClasses.get(className);
                else
                        return null;
        }

}
