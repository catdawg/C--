void main() {

  struct {
    float x;
    float y;
  } p;

  p.x = 2; p.y = 2;

  print("[CHKPT3]: struct point ( 2, 2) = ( ", p.x);
  print(", ", p.y);
  printStr(")");
  println();
}
