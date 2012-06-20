
package symboltable;

import java.util.Hashtable;

import syntaxtree.Type;

public class ClassTable {
	
        private final Hashtable<Symbol, Type> classFields = new Hashtable<Symbol, Type>();

        private final Hashtable<Symbol, MethodTable> classMethods = new Hashtable<Symbol, MethodTable>();

        public boolean addField(Symbol field, Type value) {
                if (classFields.containsKey(field))
                        return false;
                classFields.put(field, value);
                return true;
        }

        public boolean addMethod(Symbol methodName, MethodTable value) {
                if (classMethods.containsKey(methodName))
                        return false;
                classMethods.put(methodName, value);
                return true;
        }

        public MethodTable getMethod(Symbol methodName) {
                if (classMethods.containsKey(methodName))
                        return classMethods.get(methodName);
                else
                        return null;
        }

}
