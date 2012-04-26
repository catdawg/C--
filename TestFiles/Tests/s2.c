struct point {
  float x;
  float y;
};

void main() {

  struct point p = {2,2};

  print("[CHKPT3]: struct point ( 2, 2) = ( ", p.x);
  print(", ", p.y);
  printStr(")");
  println();
}
