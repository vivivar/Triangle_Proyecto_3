declare i32 @scanf(i8*, ...)
@.fmtReadInt = private constant [3 x i8] c"%d\00"
@.fmtChar = private constant [4 x i8] c"%c\0A\00"
@.fmtReadChar = private constant [4 x i8] c" %c\00"
declare i32 @printf(i8*, ...)
@.fmt = private constant [4 x i8] c"%d\0A\00"
define i32 @main() {
  %x = alloca i32
  store i32 42, i32* %x
  %t0 = load i32, i32* %x
  call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.fmt, i32 0, i32 0), i32 %t0)
  ret i32 0
}