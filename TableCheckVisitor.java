package visitors;

import java.util.ArrayList;
import symboltable.*;
import syntaxtree.*;


//Visitor que checa a tabela de simbolos.
public class TableCheckVisitor implements TypeVisitor {
	private ProgramTable programTable;
	private ClassTable currentClass;
	private MethodTable currentMethod;

	private ArrayList erros;
	
	public TableCheckVisitor(){
		erros = new ArrayList();
		programTable = ProgramTable.getInstance();
		currentClass = null;
		currentMethod = null;
	}

	private void TipoErros(String msg) {
	    erros.add(msg);
	}


	private void checkType(Type type) {
	    if (!(type instanceof IdentifierType))
	      return;

	    IdentifierType it = (IdentifierType)type;

	    if (programTable.getClass(it.s) == null) {
	      System.out.println("Unknown class: " + it.s);
	      System.exit(1);
	    }
	  }

	private Type getVarType(Identifier i) {
	    return programTable.getVarType(currentMethod, currentClass, i.s);
	  }
	
	
	@Override
	public Type visit(Program n) {
		n.mainClass.accept(this);
		
		for (int i = 0; i < n.classDeclList.size(); i++) {
			n.classDeclList.elementAt(i).accept(this);
		}
		return null;
	}

	@Override
	public Type visit(MainClass n) {
		currentClass = programTable.getClass(n.idClass.toString());
		currentMethod = currentClass.getMethod("main");
		n.stm.accept(this);
		
		currentMethod = null;
		currentClass = null;
		return null;
	}

	@Override
	public Type visit(ClassDeclSimple n) {
		String nome = n.i.toString();
		currentClass = programTable.getClass(nome);

		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}

		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
		return null;
	}

	@Override
	public Type visit(ClassDeclExtends n) {
		String nome = n.i.toString();
		currentClass = programTable.getClass(nome);
		
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
		return null;
	}

	
	@Override
	public Type visit(VarDecl n) {
		checkType(n.t);
		return null;
	}

	@Override
	public Type visit(MethodDecl n) {
		checkType(n.t);
		currentMethod = currentClass.getMethod(n.i.toString());
		
		for (int i = 0; i < n.fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		if(!(n.e.accept(this).toString().equals(n.t.toString()))){
			TipoErros("Erro de tipo no MethodDecl ");
		}
		currentMethod = null;
		return null;
	}

	@Override
	public Type visit(Formal n) {
		checkType(n.t);
		return null;
	}

	@Override
	public Type visit(IntArrayType n) {
		return n;
	}

	@Override
	public Type visit(BooleanType n) {
		return n;
	}

	@Override
	public Type visit(IntegerType n) {
		return n;
	}

	@Override
	public Type visit(IdentifierType n) {
		return n;
	}

	@Override
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	@Override
	public Type visit(If n) {
		Type expT = n.e.accept(this);
		Type boolT = BooleanType.instance();
		
		if (!programTable.compareTypes(boolT, expT))
			TipoErros("Erro: If retorno valor diferente do esperado. ");
		
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	@Override
	public Type visit(While n) {
		Type expT = n.e.accept(this);
		Type boolT = BooleanType.instance();
		
		if (!programTable.compareTypes(boolT, expT))
			TipoErros("Erro: WHILE retorno valor diferente do esperado. ");
		
		n.s.accept(this);
		return null;
	}

	@Override
	public Type visit(Print n) {
	    Type expT = n.e.accept(this);
	    Type intT = IntegerType.instance();

	    if (!programTable.compareTypes(intT, expT))
	    	TipoErros("Erro: Print retorno valor diferente do esperado. ");
	    
	    return null;
	}

	@Override
	public Type visit(Assign n) {
		Type varT = getVarType(n.i);
		Type expT = n.e.accept(this);

		if (!programTable.compareTypes(varT, expT))
			TipoErros("Erro: Tipo incompativel com o esperado. ");

		return null;
	}

	@Override
	public Type visit(ArrayAssign n) {
		Type intT = IntegerType.instance();
	    Type indexT = n.e1.accept(this);
	    Type expT = n.e2.accept(this);

	    if (!programTable.compareTypes(intT, indexT))
	    	TipoErros("Erro: Indice nao compativel. ");

	    if (!programTable.compareTypes(intT, expT))
	    	TipoErros("Erro: Indice nao compativel. ");

	    return null;
	}

	@Override
	public Type visit(And n) {
		Type expA = n.e1.accept(this);
	    Type expB = n.e2.accept(this);
	    Type boolT = BooleanType.instance();

	    if (!programTable.compareTypes(boolT, expA))
	    	TipoErros("Erro: AND . ");

	    if (!programTable.compareTypes(boolT, expB))
	    	TipoErros("Erro: AND . ");
	    
	    return boolT;
	}

	@Override
	public Type visit(LessThan n) {
		Type expA = n.e1.accept(this);
	    Type expB = n.e2.accept(this);
	    Type intT = IntegerType.instance();

	    if (!programTable.compareTypes(intT, expA))
	    	TipoErros("Erro: LessThan . ");

	    if (!programTable.compareTypes(intT, expB))
	    	TipoErros("Erro: LessThan . ");

	    Type boolT = BooleanType.instance();
	    return boolT;
	}

	@Override
	public Type visit(Plus n) {
		Type expA = n.e1.accept(this);
	    Type expB = n.e2.accept(this);
	    Type intT = IntegerType.instance();

	    if (!programTable.compareTypes(intT, expA))
	    	TipoErros("Erro: Plus . ");

	    if (!programTable.compareTypes(intT, expB))
	    	TipoErros("Erro: Plus . ");

	    return intT;
	}

	@Override
	public Type visit(Minus n) {
		Type expA = n.e1.accept(this);
	    Type expB = n.e2.accept(this);
	    Type intT = IntegerType.instance();

	    if (!programTable.compareTypes(intT, expA))
	    	TipoErros("Erro: Minus . ");

	    if (!programTable.compareTypes(intT, expB))
	    	TipoErros("Erro: Minus . ");

	    return intT;
	}

	@Override
	public Type visit(Times n) {
		Type expA = n.e1.accept(this);
		Type expB = n.e2.accept(this);
		Type intT = IntegerType.instance();

		if (!programTable.compareTypes(intT, expA))
			TipoErros("Erro: Minus . ");

		if (!programTable.compareTypes(intT, expB))
			TipoErros("Erro: Minus . ");

		return intT;
	}

	@Override
	public Type visit(ArrayLookup n) {
		Type expArr = n.e2.accept(this);
	    Type expInd = n.e1.accept(this);
	    Type intT = IntegerType.instance();
	    Type arrayT = IntArrayType.instance();

	    if (!programTable.compareTypes(arrayT, expArr))
	    	TipoErros("Erro: ArrayLookup . ");

	    if (!programTable.compareTypes(intT, expInd))
	    	TipoErros("Erro: ArrayLookup . ");

	    return intT;
	}

	@Override
	public Type visit(ArrayLength n) {
		Type expArr = n.e.accept(this);
	    Type arrayT = IntArrayType.instance();

	    if (!programTable.compareTypes(arrayT, expArr))
	    	TipoErros("Erro: ArrayLength . ");

	    Type intT = IntegerType.instance();
	    return intT;
	}

	
	
	@Override
	public Type visit(Call n) {
		Type objT = n.e.accept(this);

	    if (!(objT instanceof IdentifierType))
	    	TipoErros("Erro: Call . ");


	    String classNameStr = ((IdentifierType)objT).getClass().getName();
	    String methodName = n.i.s;
	    MethodTable method = programTable.getMethod(methodName, classNameStr);

	    if (method.getParameters().size() != n.el.size()) {
	    	System.out.println("Assinatura do Metodo errada: tamanho diferente.");
	    	System.exit(1);
	    }
	    for (int i = 0; i < method.getParameters().size(); ++i) {
	        Variable param = method.getParameterAt(i);
	        Exp exp = n.el.elementAt(i);

	        Type paramT = param.type();
	        Type expT = exp.accept(this);

	        if (!programTable.compareTypes(paramT, expT))
	        	TipoErros("Erro: parametros com tipos diferentes. ");
	    }
	    return method.getReturnType();
	}

	@Override
	public Type visit(IntegerLiteral n) {
		Type intT = IntegerType.instance();
		return intT;
	}

	@Override
	public Type visit(True n) {
	    Type boolT = BooleanType.instance();
	    return boolT;
	}

	@Override
	public Type visit(False n) {
		Type boolT = BooleanType.instance();
		return boolT;	
	}

	@Override
	public Type visit(IdentifierExp n) {
		return n.accept(this);
	}

	@Override
	public Type visit(This n) {
		return currentClass.getType();
	}

	@Override
	public Type visit(NewArray n) {
		Type arrType = IntArrayType.instance();
		return arrType;
	}

	@Override
	public Type visit(NewObject n) {
		Type identT = new IdentifierType(n.i.s);
		checkType(identT);
		return identT;
	}

	@Override
	public Type visit(Not n) {
	    Type expT = n.e.accept(this);
	    Type boolT = BooleanType.instance();

	    if (!programTable.compareTypes(boolT, expT))
	    	TipoErros("Erro: tipos incompativeis NOT. ");

	    return boolT;
	}

	@Override
	public Type visit(Identifier n) {
		Type t = programTable.getVarType(currentMethod, currentClass, n.toString());
		return t;
	}

}
