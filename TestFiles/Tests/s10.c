struct point {
  float x;
  float y;
};

struct line {
  struct point p, q;
};
    

void main() {

  struct line u = {{0.0,0.0},{1.0,0.0}};

  float sd = (u.q.x - u.p.x)*(u.q.x - u.p.x) + (u.q.y - u.p.y)*(u.q.y - u.p.y);

  printFloat("[CHKPT3]: unit line length (1.0) = ",  sd);
  println();
}
