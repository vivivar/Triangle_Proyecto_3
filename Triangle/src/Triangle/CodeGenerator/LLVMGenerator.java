package Triangle.CodeGenerator;

import Triangle.AbstractSyntaxTrees.*;
import Triangle.SyntacticAnalyzer.SourcePosition;
import java.util.HashSet;
import java.util.Set;

public class LLVMGenerator implements Visitor {

  private StringBuilder output = new StringBuilder();
  private StringBuilder header = new StringBuilder();
  private int tempCount = 0;
  private int labelCount = 0;
  private Set<String> declaredExternalFunctions = new HashSet<>();

  public LLVMGenerator() {
    header.append("declare i32 @scanf(i8*, ...)\n");
    header.append("@.fmtReadInt = private constant [3 x i8] c\"%d\\00\"\n");
    header.append("@.fmtChar = private constant [4 x i8] c\"%c\\0A\\00\"\n");
    header.append("@.fmtReadChar = private constant [4 x i8] c\" %c\\00\"\n");
    header.append("declare i32 @printf(i8*, ...)\n");
    header.append("@.fmt = private constant [4 x i8] c\"%d\\0A\\00\"\n");
    output.append("; Código LLVM generado desde Triangle\n\n");
    output.append("define i32 @main() {\n");
  }

public String getOutput() {
    output.append("  ret i32 0\n");
    output.append("}\n");
    return header.toString() + output.toString();
}

  private String newTemp() {
    return "%t" + (tempCount++);
  }

  private String newLabel(String prefix) {
    return "L" + (labelCount++);
  }
  
  private String newLabel() {
    return "L" + (labelCount++);
  }

    // ---------- Ejemplo: Programa completo ----------
    public Object visitProgram(Program ast, Object o) {
        ast.C.visit(this, o);
        return null;
    }

    // ---------- Ejemplo: comando Begin ----------
    public Object visitSequentialCommand(SequentialCommand ast, Object o) {
        ast.C1.visit(this, o);
        ast.C2.visit(this, o);
        return null;
    }

    public Object visitAssignCommand(AssignCommand ast, Object obj) {
      String varName = ((SimpleVname) ast.V).I.spelling;
      String exprTemp = (String) ast.E.visit(this, obj);
      output.append("  store i32 " + exprTemp + ", i32* %" + varName + "\n");
      return null;
    }

    // ---------- Resto de visitadores ----------
    // Los demás métodos retornan null mientras se van implementando


    public Object visitSimpleVname(SimpleVname ast, Object obj) {
      return ast.I.spelling;
    }
    
    public Object visitIntegerLiteral(IntegerLiteral ast, Object obj) {
      return ast.spelling;
    }
    public Object visitCharacterLiteral(CharacterLiteral ast, Object obj) {
    // Obtiene el valor del carácter (como char) y lo convierte a su código ASCII (int)
    char ch = ast.spelling.charAt(0);
    int ascii = (int) ch;
    String temp = newTemp();
    output.append("  " + temp + " = add i32 0, " + ascii + "\n");
    return temp;
}
 
    public Object visitBinaryExpression(BinaryExpression ast, Object obj) {
    String left = (String) ast.E1.visit(this, obj);
    String right = (String) ast.E2.visit(this, obj);
    String temp = newTemp();

    String op = ast.O.spelling;
    switch (op) {
      case "+":
        output.append("  " + temp + " = add i32 " + left + ", " + right + "\n");
        break;
      case "-":
        output.append("  " + temp + " = sub i32 " + left + ", " + right + "\n");
        break;
      case "*":
        output.append("  " + temp + " = mul i32 " + left + ", " + right + "\n");
        break;
      case "/":
        output.append("  " + temp + " = sdiv i32 " + left + ", " + right + "\n");
        break;
      case "<":
        output.append("  " + temp + " = icmp slt i32 " + left + ", " + right + "\n");
        break;
      case "<=":
        output.append("  " + temp + " = icmp sle i32 " + left + ", " + right + "\n");
        break;
      case ">":
        output.append("  " + temp + " = icmp sgt i32 " + left + ", " + right + "\n");
        break;
      case ">=":
        output.append("  " + temp + " = icmp sge i32 " + left + ", " + right + "\n");
        break;
      case "=":
        output.append("  " + temp + " = icmp eq i32 " + left + ", " + right + "\n");
        break;
      case "!=":
        output.append("  " + temp + " = icmp ne i32 " + left + ", " + right + "\n");
        break;
      default:
        output.append("  ; Operador no soportado: " + op + "\n");
    }
    return temp;
  }
     
public Object visitIfCommand(IfCommand ast, Object obj) {
    // Crear etiquetas únicas para los bloques
    String thenLabel = newLabel();
    String elseLabel = newLabel();
    String endLabel = newLabel();

    // Evaluar la condición del if
    String condValue = (String) ast.E.visit(this, obj);

    // La condición ya debe ser i1 si viene de BinaryExpression con operadores relacionales
    output.append("  br i1 " + condValue + ", label %" + thenLabel + ", label %" + elseLabel + "\n");

    // Then block
    output.append(thenLabel + ":\n");
    ast.C1.visit(this, obj);
    output.append("  br label %" + endLabel + "\n");

    // Else block
    output.append(elseLabel + ":\n");
    ast.C2.visit(this, obj);
    output.append("  br label %" + endLabel + "\n");

    // End block
    output.append(endLabel + ":\n");

    return null;
}

public Object visitWhileCommand(WhileCommand ast, Object obj) {
    String condLabel = newLabel("cond");
    String bodyLabel = newLabel("body");
    String endLabel = newLabel("end");

    // Salto a la evaluación de la condición
    output.append("  br label %" + condLabel + "\n");

    // Etiqueta de la condición
    output.append(condLabel + ":\n");
    String condTemp = (String) ast.E.visit(this, obj); // debe ser una variable tipo i1
    output.append("  br i1 " + condTemp + ", label %" + bodyLabel + ", label %" + endLabel + "\n");

    // Etiqueta del cuerpo del while
    output.append(bodyLabel + ":\n");
    ast.C.visit(this, obj);
    output.append("  br label %" + condLabel + "\n");

    // Etiqueta de fin
    output.append(endLabel + ":\n");

    return null;
}
public Object visitLetCommand(LetCommand ast, Object obj) {
  ast.D.visit(this, obj);   // Primero procesar las declaraciones
  ast.C.visit(this, obj);   // Luego el cuerpo del comando
  return null;
}

public Object visitVarDeclaration(VarDeclaration ast, Object obj) {
  String varName = ast.I.spelling;
  output.append("  %" + varName + " = alloca i32\n");
  return null;
}
    public Object visitConstDeclaration(ConstDeclaration ast, Object o) { return null; }
    public Object visitFuncDeclaration(FuncDeclaration ast, Object o) { return null; }
    public Object visitProcDeclaration(ProcDeclaration ast, Object o) { return null; }

    // Y así todos los métodos del Visitor...

    public Object visitEmptyCommand(EmptyCommand ast, Object o) { return null; }

    // ... otros métodos omitidos por ahora ...

 public Object visitVname(Vname ast, Object obj) {
    return ast.visit(this, obj);
  }

    // Puedes implementar más según lo que vayas necesitando

    @Override
public Object visitCallCommand(CallCommand ast, Object o) {
    String procName = ast.I.spelling;

    // ---------------- getint / getchar ----------------
    if (procName.equals("getint") || procName.equals("getchar")) {
        String fmt = procName.equals("getint") ? "@.fmtReadInt" : "@.fmtReadChar";
        String type = "i32";

    if (ast.APS instanceof SingleActualParameterSequence) {
        ActualParameter ap = ((SingleActualParameterSequence) ast.APS).AP;

        if (ap instanceof VarActualParameter) {
            Vname vname = ((VarActualParameter) ap).V;

            if (vname instanceof SimpleVname) {
                String varName = ((SimpleVname) vname).I.spelling;
                String userVarPtr = "%" + varName;

                // NO usamos tempPtr ni alloca extra aquí
                output.append("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds (");
                output.append(procName.equals("getint") ? "[3 x i8], [3 x i8]* " : "[4 x i8], [4 x i8]* ");
                output.append(fmt).append(", i32 0, i32 0), ").append(type).append("* ").append(userVarPtr).append(")\n");
            }
        }
    }

        return null;
    }

    // ---------------- put (para Integer o Char) ----------------
    if (procName.equals("put") || procName.equals("putint")) {
        if (ast.APS instanceof SingleActualParameterSequence) {
            ActualParameter ap = ((SingleActualParameterSequence) ast.APS).AP;

            if (ap instanceof ConstActualParameter) {
                Expression expr = ((ConstActualParameter) ap).E;

                if (expr instanceof VnameExpression) {
                    Vname vname = ((VnameExpression) expr).V;

                    if (vname instanceof SimpleVname) {
                        String varName = ((SimpleVname) vname).I.spelling;

                        // Cargar valor
                        String temp = newTemp();
                        output.append("  ").append(temp).append(" = load i32, i32* %").append(varName).append("\n");

                        // Decidir formato según nombre (truco temporal)
                        String format = varName.equals("c") ? "@.fmtChar" : "@.fmt";

                        // Llamar a printf
                        output.append("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ");
                        output.append("([4 x i8], [4 x i8]* ").append(format).append(", i32 0, i32 0), i32 ").append(temp).append(")\n");
                    }
                }
            }
        }

        return null;
    }

    return null;
}

    @Override
    public Object visitArrayExpression(ArrayExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitCallExpression(CallExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
        String temp = newTemp();
        char value = ast.CL.spelling.charAt(1); // Esto debe extraer 'x' de "'x'"
        int ascii = (int) value;

        output.append("  " + temp + " = add i32 0, " + ascii + "\n");
        return temp;
    }

    @Override
    public Object visitEmptyExpression(EmptyExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitIfExpression(IfExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitIntegerExpression(IntegerExpression ast, Object obj) {
        return ast.IL.visit(this, obj); // Delegamos al visitIntegerLiteral
    }

    @Override
    public Object visitLetExpression(LetExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitRecordExpression(RecordExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitVnameExpression(VnameExpression ast, Object obj) {
        // Obtener el nombre de la variable
        String varName = (String) ast.V.visit(this, obj);

        // Crear un temporal para el valor cargado
        String temp = newTemp();
        output.append("  " + temp + " = load i32, i32* %" + varName + "\n");

        return temp;
    }

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
        ast.D1.visit(this, o);
        ast.D2.visit(this, o);
        return null;
    }
    
    @Override
    public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitProcFormalParameter(ProcFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitVarFormalParameter(VarFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitConstActualParameter(ConstActualParameter ast, Object o) {
        return ast.E.visit(this, o); // Visita la expresión constante (ej: el 1 en putint(1))
    }

    @Override
    public Object visitFuncActualParameter(FuncActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitProcActualParameter(ProcActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitVarActualParameter(VarActualParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o) {
        return ""; // No hay parámetros
    }

    @Override
    public Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o) {
        // Puedes usar una lista si soportas múltiples args en futuro
        String first = (String) ast.AP.visit(this, o);
        String rest = (String) ast.APS.visit(this, o);

        return first + ", " + rest;
    }

    @Override
    public Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o) {
        return ast.AP.visit(this, o); // Solo hay un parámetro, así que simplemente lo visitamos
    }

    @Override
    public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o) {
        return "i1";
    }

    @Override
    public Object visitCharTypeDenoter(CharTypeDenoter ast, Object o) {
        return "i8";
    }

    @Override
    public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitIntTypeDenoter(IntTypeDenoter ast, Object o) {
        return "i32";
    }

    @Override
    public Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitIdentifier(Identifier ast, Object o) {
        return ast.spelling;
    }

    @Override
    public Object visitOperator(Operator ast, Object o) {
        return ast.spelling;
    }

    @Override
    public Object visitDotVname(DotVname ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitSubscriptVname(SubscriptVname ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitEmptyDeclaration(EmptyDeclaration ed, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
