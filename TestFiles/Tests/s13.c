void main() {

  struct point {
    float x;
    float y;
  };

  struct point p;

  p.x = 2; p.y = 2;

  print("[CHKPT3]: struct point ( 2, 2) = ( ", p.x);
  print(", ", p.y);
  printStr(")");
  println();
}
