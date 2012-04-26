struct point {
  int x;
  int y;
};

struct point init_point(int x, int y) {
  struct point p;
  p.x = x;
  p.y = y;
  return p;
}

void main() {

  struct point p;
  struct point q;
  int sd;

  p = init_point(2,2);
  q = init_point(3,2);

  sd = (q.x - p.x)*(q.x - p.x) + (q.y - p.y)*(q.y - p.y);

  print("[CHKPT3]: distance between points (1) = ",  sd);
  println();
}
