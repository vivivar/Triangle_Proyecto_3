declare i32 @scanf(i8*, ...)
@.fmtReadInt = private constant [3 x i8] c"%d\00"
@.fmtChar = private constant [4 x i8] c"%c\0A\00"
@.fmtReadChar = private constant [4 x i8] c" %c\00"
declare i32 @printf(i8*, ...)
@.fmt = private constant [4 x i8] c"%d\0A\00"
; Código LLVM generado desde Triangle

define i32 @main() {
  %c = alloca i32
  %c_ptr = alloca i32
  call i32 (i8*, ...) @scanf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmtReadChar, i32 0, i32 0), i32* %c_ptr)
  %t0 = load i32, i32* %c_ptr
  store i32 %t0, i32* %c
  %t1 = load i32, i32* %c
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmtChar, i32 0, i32 0), i32 %t1)
  ret i32 0
}
