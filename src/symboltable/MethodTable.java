package symboltable;

import java.util.Hashtable;

import syntaxtree.Type;

public class MethodTable {
	
	private final Hashtable<Symbol, Type> params = new Hashtable<Symbol, Type>();

    private final Hashtable<Symbol, Type> locals = new Hashtable<Symbol, Type>();
    
    public boolean addParam(String param, Type value){
            if (params.containsKey(param)) return false;
            params.put(Symbol.symbol(param), value);
            return true;
    }
    public boolean addLocal(String local, Type value){
            if (locals.containsKey(local)) return false;
            locals.put(Symbol.symbol(local), value);
            return true;
    }


}
