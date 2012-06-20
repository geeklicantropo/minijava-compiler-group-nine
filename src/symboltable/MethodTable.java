package symboltable;

import java.util.Hashtable;

import syntaxtree.Type;

public class MethodTable {
	
		private String nome;
		private Type tipo;
		
		public MethodTable (String nome, Type tipo) {
			this.setNome(nome);
			this.setTipo(tipo);
		}

        public MethodTable() {
			// TODO Auto-generated constructor stub
		}

		private final Hashtable<Symbol, Type> params = new Hashtable<Symbol, Type>();

        private final Hashtable<Symbol, Type> locals = new Hashtable<Symbol, Type>();

        public boolean addParam(Symbol param, Type value) {
                if (params.containsKey(param))
                        return false;
                params.put(param, value);
                return true;
        }

        public boolean addLocal(Symbol local, Type value) {
                if (locals.containsKey(local))
                        return false;
                locals.put(local, value);
                return true;
        }

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		public Type getTipo() {
			return tipo;
		}

		public void setTipo(Type tipo) {
			this.tipo = tipo;
		}

}
