declare i32 @scanf(i8*, ...)
@.fmtReadInt = private constant [3 x i8] c"%d\00"
@.fmtChar = private constant [4 x i8] c"%c\0A\00"
@.fmtReadChar = private constant [4 x i8] c" %c\00"
declare i32 @printf(i8*, ...)
@.fmt = private constant [4 x i8] c"%d\0A\00"
@c = dso_local global i32 0, align 4
@continuar = dso_local global i32 0, align 4
define i32 @main() {
  store i32 0, i32* @continuar
  br label %cond0
cond0:
%t0 = load i32, i32* @continuar
  %t1 = icmp eq i32 %t0, 0
  br i1 %t1, label %body1, label %end2
body1:
  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmtReadChar, i32 0, i32 0), i32* @c)
  %t2 = load i32, i32* @c
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmtChar, i32 0, i32 0), i32 %t2)
%t3 = load i32, i32* @c
  %t4 = add i32 0, 120
  %t5 = icmp eq i32 %t3, %t4
  br i1 %t5, label %L3, label %L4
L3:
  store i32 1, i32* @continuar
  br label %L5
L4:
  br label %L5
L5:
  br label %cond0
end2:
  ret i32 0
}