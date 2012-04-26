struct point {
  float x[2];
};

void main() {

  struct point p;
  struct point q;

  float sd;

  p.x[0] = 2.0; p.x[1] = 2.0;
  q.x[0] = 3.0; q.x[1] = 2.0;
  
  sd = (q.x[0] - p.x[0])*(q.x[0] - p.x[0]) + 
    (q.x[1] - p.x[1])*(q.x[1] - p.x[1]);

  printFloat("[CHKPT3]: distance between points (1.0) = ", sd);
  println();
}
