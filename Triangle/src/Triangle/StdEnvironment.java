/*
 * @(#)StdEnvironment.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle;

import TAM.Machine;
import Triangle.AbstractSyntaxTrees.BinaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryOperatorDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.CodeGenerator.PrimitiveRoutine;
import Triangle.SyntacticAnalyzer.SourcePosition;

public final class StdEnvironment {

  // These are small ASTs representing standard types.

  public static TypeDenoter
    booleanType, charType, integerType, anyType, errorType;

  public static TypeDeclaration
    booleanDecl, charDecl, integerDecl;

  // These are small ASTs representing "declarations" of standard entities.

  public static ConstDeclaration
    falseDecl, trueDecl, maxintDecl;

  public static UnaryOperatorDeclaration
    notDecl;

  public static BinaryOperatorDeclaration
    andDecl, orDecl,
    addDecl, subtractDecl, multiplyDecl, divideDecl, moduloDecl,
    equalDecl, unequalDecl, lessDecl, notlessDecl, greaterDecl, notgreaterDecl;

  public static ProcDeclaration
    getDecl, putDecl, getintDecl, putintDecl, geteolDecl, puteolDecl, getcharDecl;

  public static FuncDeclaration
    chrDecl, ordDecl, eolDecl, eofDecl;

  public static void establishStdEnvironment() {

  integerType = new Triangle.AbstractSyntaxTrees.SimpleTypeDenoter(
    new Triangle.AbstractSyntaxTrees.Identifier("Integer", null),
    null
  );

  charType = new Triangle.AbstractSyntaxTrees.SimpleTypeDenoter(
    new Triangle.AbstractSyntaxTrees.Identifier("Char", null),
    null
  );
  SourcePosition dummyPos = new SourcePosition();

  Identifier integerId = new Identifier("Integer", dummyPos);
  integerDecl = new TypeDeclaration(integerId, integerType, dummyPos);

  Identifier charId = new Identifier("Char", dummyPos);
  charDecl = new TypeDeclaration(charId, charType, dummyPos);

  FormalParameterSequence getcharParams = new SingleFormalParameterSequence(
    new VarFormalParameter(
      new Identifier("c", dummyPos),
      charType,
      dummyPos
    ),
    dummyPos
  );

  PrimitiveRoutine getcharEntity = new PrimitiveRoutine(0, Machine.getDisplacement);

  getcharDecl = new ProcDeclaration(
    new Identifier("getchar", dummyPos),
    getcharParams,
    null,
    dummyPos
  );
  getcharDecl.entity = getcharEntity;

}
  
}
