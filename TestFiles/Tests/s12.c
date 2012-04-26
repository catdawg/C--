struct point {
  int x;
  int y;
};

int distance(struct point p, struct point q) {
  int sd;
  sd = (q.x - p.x)*(q.x - p.x) + (q.y - p.y)*(q.y - p.y);
  return sd;

}
void main() {

  struct point p;
  struct point q;
  int sd;

  p.x = 2; p.y = 2; q.x = 3; q.y =2;

  sd = distance(p,q);

  print("[CHKPT3]: distance between points (1) = ",  sd);
  println();
}
