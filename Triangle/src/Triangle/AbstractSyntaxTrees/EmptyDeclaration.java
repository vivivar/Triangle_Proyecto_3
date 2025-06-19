package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

public class EmptyDeclaration extends Declaration {

  public EmptyDeclaration(SourcePosition thePosition) {
    super(thePosition);
  }

  public Object visit(Visitor v, Object o) {
    return v.visitEmptyDeclaration(this, o);
  }
}
