struct point {
  float x, y;
};

struct line {
  struct point p[2];
};

float sqr(float x) {
  float y;
  y = x*x;
  return y;
}

void main() {

  struct line l; 
  float l2;

  l.p[0].x = 0; l.p[0].y = 0;
  l.p[1].x = 0; l.p[1].y = 1;
  
  l2= sqr(l.p[0].x -l.p[1].x) + sqr(l.p[0].y-l.p[1].y);

  printFloat("[CHKPT3]: line length is (1.0) = ", l2);
  println();
}
