struct point {
  float x;
  float y;
};

  
struct line {
  struct point p, q;
};
    
void main() {

  struct line u;
  float sd;

  u.p.x = 0; u.p.y = 0;
  u.q.x = 1; u.q.y = 0;

  sd = (u.q.x - u.p.x)*(u.q.x - u.p.x) + (u.q.y - u.p.y)*(u.q.y - u.p.y);


  printFloat("[CHKPT3]: unit line length (1.0) = ", sd);
  println();
}
