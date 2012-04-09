package visitor;

import symboltable.*;
import syntaxtree.*;

public class CreateTableVisitor implements Visitor {
	private ProgramTable programTable;
	private ClassTable currentClass;
	private MethodTable currentMethod;

	public CreateTableVisitor() {
		currentClass = null;
		currentMethod = null;
		programTable = new ProgramTable();
	}

	public ProgramTable getProgramTable() {
		return programTable;
	}

	private void hasError(String msg) {
		System.out.println(msg);
		System.exit(1);
	}

	@Override
	public void visit(Program n) {
		n.m.accept(this);
		for (int i = 0; i < n.cl.size(); i++) {
			n.cl.elementAt(i).accept(this);
		}

	}

	@Override
	public void visit(MainClass n) {
		String name = n.i1.toString();
		String param = n.i2.toString();

		programTable.addClass(name, null);
		currentClass = programTable.getClass(Symbol.symbol(name));

		currentClass.addMethod("main", new IdentifierType("void"));

		currentMethod = currentClass.getMethod(Symbol.symbol(name));
		currentMethod.addParam(param, new IdentifierType("String[]"));

		currentClass = null;
		currentMethod = null;

	}

	@Override
	public void visit(ClassDeclSimple n) {
		String name = n.i.toString();
		VarDeclList vl = n.vl;
		MethodDeclList ml = n.ml;

		if (!programTable.addClass(name, null)) {
			hasError("classe " + n.i.toString() + " j� declarada.");
		}
		currentClass = programTable.getClass(Symbol.symbol(name));

		for (int i = 0; i < vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}

		for (int i = 0; i < ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
	}

	@Override
	public void visit(ClassDeclExtends n) {
		String name = n.i.toString();
		String baseClass = n.j.toString();
		VarDeclList vl = n.vl;
		MethodDeclList ml = n.ml;
		ClassTable currentBaseClass;

		currentBaseClass = programTable.getClass(Symbol.symbol(baseClass));

		if (currentBaseClass == null) {
			hasError("Superclasse " + baseClass + " ainda n�o declarada.");
		}

		if (!programTable.addClass(name, currentBaseClass)) {
			hasError("Classe " + name + " j� declarada. ");
		}

		currentClass = programTable.getClass(Symbol.symbol(name));

		for (int i = 0; i < vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}

		for (int i = 0; i < ml.size(); i++) {
			n.ml.elementAt(i).accept(this);
		}
		currentClass = null;
	}

	@Override
	public void visit(VarDecl n) {
		String varName = n.i.toString();
		Type varType = n.t;

		if (currentMethod == null) {
			if (!currentClass.addField(varName, varType)) {
				hasError("Vari�vel" + varName + " j� definida.");
			} else {
				if (!currentMethod.addLocal(varName, varType)) {
					hasError("Vari�vel Local " + varName + " j� declarada.");
				}
			}
		}
	}

	@Override
	public void visit(MethodDecl n) {
		Type tipo = n.t;
		String nome = n.i.toString();
		VarDeclList vl = n.vl;
		FormalList fl = n.fl;

		if (!currentClass.addMethod(nome, tipo)) {
			hasError("M�todo" + nome + " j� existe na classe "
					+ currentClass.toString());
		}

		currentMethod = currentClass.getMethod(Symbol.symbol(nome));
		for (int i = 0; i < vl.size(); i++) {
			n.vl.elementAt(i).accept(this);
		}
		for (int i = 0; i < fl.size(); i++) {
			n.fl.elementAt(i).accept(this);
		}
		currentMethod = null;

	}

	@Override
	public void visit(Formal n) {
		Type tipo = n.t;
		String nome = n.i.toString();

		if (!currentMethod.addParam(nome, tipo)) {
			hasError("Par�metro " + nome + " j� existe no m�todo "
					+ currentMethod.toString());
		}
	}

	@Override
	public void visit(IntArrayType n) {
	}

	@Override
	public void visit(BooleanType n) {
	}

	@Override
	public void visit(IntegerType n) {
	}

	@Override
	public void visit(IdentifierType n) {
	}

	@Override
	public void visit(Block n) {

	}

	@Override
	public void visit(If n) {

	}

	@Override
	public void visit(While n) {
		n.s.accept(this);
	}

	@Override
	public void visit(Print n) {
		n.e.accept(this);
	}

	@Override
	public void visit(Assign n) {
	}

	@Override
	public void visit(ArrayAssign n) {
	}

	@Override
	public void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	@Override
	public void visit(ArrayLength n) {
		n.e.accept(this);

	}

	@Override
	public void visit(Call n) {
		n.e.accept(this);
		for (int i = 0; i < n.el.size(); i++) {
			n.el.elementAt(i).accept(this);
		}

	}

	@Override
	public void visit(IntegerLiteral n) {
	}

	@Override
	public void visit(True n) {
	}

	@Override
	public void visit(False n) {
	}

	@Override
	public void visit(IdentifierExp n) {
	}

	@Override
	public void visit(This n) {
	}

	@Override
	public void visit(NewArray n) {
		n.e.accept(this);
	}

	@Override
	public void visit(NewObject n) {
	}

	@Override
	public void visit(Not n) {
		n.e.accept(this);
	}

	@Override
	public void visit(Identifier n) {

	}

}
