package Triangle.CodeGenerator;

import Triangle.AbstractSyntaxTrees.*;
import Triangle.SyntacticAnalyzer.SourcePosition;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class LLVMGenerator implements Visitor {

  private StringBuilder output = new StringBuilder();
  private StringBuilder header = new StringBuilder();
  private StringBuilder functions = new StringBuilder();
  private StringBuilder mainCode = new StringBuilder();
  private StringBuilder globalDecls = new StringBuilder();
  private int tempCount = 0;
  private int labelCount = 0;
  private Set<String> declaredExternalFunctions = new HashSet<>();
  private Set<String> globalVars = new HashSet<>();
  private boolean isGlobalScope = true;
  private Set<String> functionParameters = new HashSet<>();

  public LLVMGenerator() {
    header.append("declare i32 @scanf(i8*, ...)\n");
    header.append("@.fmtReadInt = private constant [3 x i8] c\"%d\\00\"\n");
    header.append("@.fmtChar = private constant [4 x i8] c\"%c\\0A\\00\"\n");
    header.append("@.fmtReadChar = private constant [4 x i8] c\" %c\\00\"\n");
    header.append("declare i32 @printf(i8*, ...)\n");
    header.append("@.fmt = private constant [4 x i8] c\"%d\\0A\\00\"\n");
    output.append("; Código LLVM generado desde Triangle\n\n");

  }

    public String getOutput() {
        StringBuilder output = new StringBuilder();
        output.append(header);
        output.append(globalDecls);
        output.append(functions.toString());
        output.append("define i32 @main() {\n");
        output.append(mainCode);
        output.append("  ret i32 0\n}");
        return output.toString();
    }

  private String newTemp() {
    return "%t" + (tempCount++);
  }

  private String newLabel(String prefix) {
        return prefix + labelCount++;
  }
  
  private String newLabel() {
    return "L" + (labelCount++);
  }
  
    private void emit(String line) {
        if (isGlobalScope) {
            System.out.println("[EMIT] Global scope → " + line); //
            globalDecls.append(line).append("\n");
        } else {
            System.out.println("[EMIT] Main scope → " + line); 
            mainCode.append(line).append("\n");
        }
    }
    
private String getVariableReference(String name) {
    if (functionParameters.contains("%" + name)) {
        return "%" + name;
    }
    return globalVars.contains(name) ? "@" + name : "%" + name;
}

    // ---------- Ejemplo: Programa completo ----------
public Object visitProgram(Program ast, Object o) {
    // Primero: declarar globales si fuera necesario (por ahora nada)
    isGlobalScope = true;

    // (Aquí irían las globales si tienes variables globales explícitas)
    // Ejemplo:
    // globalVars.append("@x = global i32 0\n");

    isGlobalScope = false;
    System.out.println("[Scope] Entrando a main");

    // Luego: generar el cuerpo de main (código ejecutable)
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
      String ref = getVariableReference(varName);
      
      emit("  store i32 " + exprTemp + ", i32* " + ref);
      return null;
    }

    // ---------- Resto de visitadores ----------
    // Los demás métodos retornan null mientras se van implementando


public Object visitSimpleVname(SimpleVname ast, Object o) {
    return ast.I.spelling; // Solo el nombre limpio, como "a" o "x"
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
 
public Object visitBinaryExpression(BinaryExpression ast, Object o) {
        String left = (String) ast.E1.visit(this, o);
        String right = (String) ast.E2.visit(this, o);
        String temp = newTemp();

        String op = ast.O.spelling;
        switch (op) {
            case "+":
                emit("  " + temp + " = add i32 " + left + ", " + right);
                break;
            case "-":
                emit("  " + temp + " = sub i32 " + left + ", " + right);
                break;
            case "*":
                emit("  " + temp + " = mul i32 " + left + ", " + right);
                break;
            case "/":
                emit("  " + temp + " = sdiv i32 " + left + ", " + right);
                break;
            case "<":
                emit("  " + temp + " = icmp slt i32 " + left + ", " + right);
                break;
            case "<=":
                emit("  " + temp + " = icmp sle i32 " + left + ", " + right);
                break;
            case ">":
                emit("  " + temp + " = icmp sgt i32 " + left + ", " + right);
                break;
            case ">=":
                emit("  " + temp + " = icmp sge i32 " + left + ", " + right);
                break;
            case "=":
                emit("  " + temp + " = icmp eq i32 " + left + ", " + right);
                break;
            case "!=":
                emit("  " + temp + " = icmp ne i32 " + left + ", " + right);
                break;
            default:
                emit("  ; Operador no soportado: " + op);
                break;
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
    mainCode.append("  br i1 " + condValue + ", label %" + thenLabel + ", label %" + elseLabel + "\n");

    // Then block
    mainCode.append(thenLabel + ":\n");
    ast.C1.visit(this, obj);
    mainCode.append("  br label %" + endLabel + "\n");

    // Else block
    mainCode.append(elseLabel + ":\n");
    ast.C2.visit(this, obj);
    mainCode.append("  br label %" + endLabel + "\n");

    // End block
    mainCode.append(endLabel + ":\n");

    return null;
}

public Object visitWhileCommand(WhileCommand ast, Object obj) {
    String condLabel = newLabel("cond");
    String bodyLabel = newLabel("body");
    String endLabel = newLabel("end");

    // Salto a la evaluación de la condición
    mainCode.append("  br label %" + condLabel + "\n");

    // Etiqueta de la condición
    mainCode.append(condLabel + ":\n");
    String condTemp = (String) ast.E.visit(this, obj); // debe ser una variable tipo i1
    mainCode.append("  br i1 " + condTemp + ", label %" + bodyLabel + ", label %" + endLabel + "\n");

    // Etiqueta del cuerpo del while
    mainCode.append(bodyLabel + ":\n");
    ast.C.visit(this, obj);
    mainCode.append("  br label %" + condLabel + "\n");

    // Etiqueta de fin
    mainCode.append(endLabel + ":\n");

    return null;
}
public Object visitLetCommand(LetCommand ast, Object obj) {
  ast.D.visit(this, obj);   // Primero procesar las declaraciones
  ast.C.visit(this, obj);   // Luego el cuerpo del comando
  return null;
}

public Object visitVarDeclaration(VarDeclaration ast, Object o) {
        String name = ast.I.spelling;
        if (isGlobalScope) {
            globalVars.add(name);
            emit("@" + name + " = global i32 0");
        } else {
            emit("  %" + name + " = alloca i32");
        }
        return null;
    }
    public Object visitConstDeclaration(ConstDeclaration ast, Object o) { return null; }
 
public Object visitFuncDeclaration(FuncDeclaration ast, Object o) {
    boolean previousScope = isGlobalScope;
    isGlobalScope = false;

    // Guardar estado de output y mainCode
    StringBuilder previousOutput = output;
    StringBuilder previousMainCode = mainCode;

    // Nuevo output y mainCode para esta funcion
    output = new StringBuilder();
    mainCode = new StringBuilder();

    String funcName = ast.I.spelling;
    List<String> paramsList = new ArrayList<>();
    ast.FPS.visit(this, paramsList); // genera: ["i32 %a"]

    String returnType = "i32";

    if (!funcName.equals("main")) {
        // Encabezado de la funcion
        output.append("define ").append(returnType).append(" @").append(funcName).append("(");
        output.append(String.join(", ", paramsList));
        output.append(") {\n");

        // Guardar los nombres de los parametros para evitar loads
        for (String param : paramsList) {
            String paramName = param.split(" ")[1].substring(1);  // "i32 %a" -> "%a"
            functionParameters.add(paramName);
            System.out.println("[DEBUG] Registrado parámetro: " + paramName);
        }

        // Visitar cuerpo
        String returnValue = (String) ast.E.visit(this, o);

        // Agregar cuerpo y retorno
        output.append(mainCode.toString());
        output.append("  ret i32 ").append(returnValue).append("\n");
        output.append("}\n\n");
    }

    // Agregar funcion al bloque de funciones y restaurar
    functions.append(output.toString()).append("\n");
    output = previousOutput;
    mainCode = previousMainCode;
    isGlobalScope = previousScope;
    functionParameters.clear();

    return null;
}
    
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
                    String userVarPtr = getVariableReference(varName); // 🔧 Aquí se usa correctamente

                    mainCode.append("  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds (");
                    mainCode.append(procName.equals("getint") ? "[3 x i8], [3 x i8]* " : "[4 x i8], [4 x i8]* ");
                    mainCode.append(fmt).append(", i32 0, i32 0), ").append(type).append("* ").append(userVarPtr).append(")\n");
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
                            String ref = getVariableReference(varName);

                            // Cargar valor
                            String temp = newTemp();
                            mainCode.append("  ").append(temp).append(" = load i32, i32* ").append(ref).append("\n");

                            // Decidir formato según nombre (truco temporal)
                            String format = varName.equals("c") ? "@.fmtChar" : "@.fmt";

                            // Llamar a printf
                            mainCode.append("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ");
                            mainCode.append("([4 x i8], [4 x i8]* ").append(format).append(", i32 0, i32 0), i32 ").append(temp).append(")\n");

                            return null;
                        }
                    }
                }

                // ✅ Soporte adicional para expresiones generales (como suma(3))
                Object argValue = ap.visit(this, o);
                String format = "@.fmt";
                mainCode.append("  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ");
                mainCode.append("([4 x i8], [4 x i8]* ").append(format).append(", i32 0, i32 0), i32 ").append(argValue).append(")\n");
            }

        return null;
    }

    // Otros procedimientos (por ahora no implementados)
    return null;
}

    @Override
    public Object visitArrayExpression(ArrayExpression ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitCallExpression(CallExpression ast, Object o) {
        String functionName = ast.I.spelling;

        // Asumimos un solo parámetro
        Object argValue = ast.APS.visit(this, o); // puede ser String

        String temp = newTemp();
        emit(temp + " = call i32 @" + functionName + "(i32 " + argValue + ")");
        return temp;
    }

    @Override
    public Object visitCharacterExpression(CharacterExpression ast, Object obj) {
        String temp = newTemp();
        char value = ast.CL.spelling.charAt(1); // Esto debe extraer 'x' de "'x'"
        int ascii = (int) value;

        mainCode.append("  " + temp + " = add i32 0, " + ascii + "\n");
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
        ast.D.visit(this, o);
        return ast.E.visit(this, o);
    }

    @Override
    public Object visitRecordExpression(RecordExpression ast, Object o) {
        String structTemp = newTemp();
        Object val = ast.RA.visit(this, o);
        return structTemp; 
    }

    @Override
    public Object visitUnaryExpression(UnaryExpression ast, Object o) {
        String exprTemp = (String) ast.E.visit(this, o);
        String resultTemp = newTemp();

        switch (ast.O.spelling) {
            case "-":
                output.append("  ").append(resultTemp).append(" = sub i32 0, ").append(exprTemp).append("\n");
                break;
            case "\\":
                mainCode.append("  ").append(resultTemp).append(" = icmp eq i32 ").append(exprTemp).append(", 0\n");
                break;
            default:
                System.err.println("Operador unario no soportado: " + ast.O.spelling);
                break;
        }

        return resultTemp;
    }
    @Override
public Object visitVnameExpression(VnameExpression ast, Object obj) {
    // Obtener el nombre de la variable sin prefijo
    String varName = (String) ast.V.visit(this, obj);

    // Usar getVariableReference para obtener la referencia correcta (% o @)
    String ref = getVariableReference(varName);
    System.out.println("[DEBUG] visitVnameExpression → varName: " + varName + ", ref: " + ref);
    System.out.println("[DEBUG] functionParameters contiene " + varName + "? → " + functionParameters.contains(varName));
    if (functionParameters.contains(varName)) {
        return ref; // ya es un valor i32
    }
    // Crear un temporal para cargar el valor
    String temp = newTemp();
    emit(temp + " = load i32, i32* " + ref);

    return temp;
}

    @Override
    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
        return null; 
    }

    @Override
    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
        ast.D1.visit(this, o);
        ast.D2.visit(this, o);
        return null;
    }
    
    @Override
    public Object visitTypeDeclaration(TypeDeclaration ast, Object o) {
        return null; 
    }

    @Override
    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o) {
        String prev = (String) ast.AA.visit(this, o);
        String val = (String) ast.E.visit(this, o);
        return prev + ", " + val;
    }

    @Override
    public Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o) {
        return ast.E.visit(this, o); 
    }

    @Override
    public Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o) {
        String left = (String) ast.RA.visit(this, o); 
        String right = (String) ast.E.visit(this, o); 
        return left + ", " + right;
    }

    @Override
    public Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o) {
        String val = (String) ast.E.visit(this, o);
        return val;
    }

    @Override
    public Object visitConstFormalParameter(ConstFormalParameter ast, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Object visitFuncFormalParameter(FuncFormalParameter ast, Object o) {
        @SuppressWarnings("unchecked")
        List<String> paramsList = (List<String>) o;
        String llvmType = "i32";
        paramsList.add(llvmType + " %" + ast.I.spelling);

        return null;
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
        List<String> paramList = (List<String>) o;

        ast.FPS.visit(this, paramList);

        if (ast.FP instanceof ConstFormalParameter) {
            paramList.add("i32 %" + ((ConstFormalParameter) ast.FP).I.spelling);
        } else if (ast.FP instanceof VarFormalParameter) {
            paramList.add("i32 %" + ((VarFormalParameter) ast.FP).I.spelling);
        } else if (ast.FP instanceof ProcFormalParameter) {
            paramList.add("i32 (%i32)* %" + ((ProcFormalParameter) ast.FP).I.spelling); // función tipo proc
        } else if (ast.FP instanceof FuncFormalParameter) {
            paramList.add("i32 (i32)* %" + ((FuncFormalParameter) ast.FP).I.spelling); // función que retorna i32
        } else {
            paramList.add("i32 %unknown_param");
        }

        return null;
    }

    @Override
    public Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o) {
        List<String> paramList = (List<String>) o;

        String paramName = "%";
        String paramType = "i32"; // Por defecto

        if (ast.FP instanceof ConstFormalParameter) {
            paramName += ((ConstFormalParameter) ast.FP).I.spelling;
        } else if (ast.FP instanceof VarFormalParameter) {
            paramName += ((VarFormalParameter) ast.FP).I.spelling;
        } else if (ast.FP instanceof ProcFormalParameter) {
            paramName += ((ProcFormalParameter) ast.FP).I.spelling;
        } else if (ast.FP instanceof FuncFormalParameter) {
            paramName += ((FuncFormalParameter) ast.FP).I.spelling;
        } else {
            paramName += "unknown_param";
        }

        paramList.add(paramType + " " + paramName);
        return null;
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
        switch (ast.I.spelling) {
            case "Integer": return "i32";
            case "Char": return "i32"; // aunque en realidad podría ser i8, por ahora usamos i32 para simplificar
            default: return "i32"; // fallback
        }
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
        String arrayPtr = (String) ast.V.visit(this, o); 
        String index = (String) ast.E.visit(this, o);    

        String elementPtr = newTemp();
        output.append("  ").append(elementPtr)
              .append(" = getelementptr inbounds i32, i32* ")
              .append(arrayPtr).append(", i32 ").append(index).append("\n");

        return elementPtr;
    }

    @Override
    public Object visitEmptyDeclaration(EmptyDeclaration ed, Object o) {
        return null;
    }

}
