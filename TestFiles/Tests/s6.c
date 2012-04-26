struct point {
    int x;
    int y;
};

void main() {

  struct point p;
  struct point q;

  p.x = 2; p.y = 2;

  q = p;

  print("[CHKPT3]: q ( 2, 2)  = ( ", q.x);
  print(", ", q.y);
  printStr(")");
  println();
}
