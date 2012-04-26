struct point {
  float x, y;
};

void main() {

  struct point p[2];
  float sd;

  p[0].x = 2.0; p[0].y = 2.0;
  p[1].x = 3.0; p[1].y = 2.0;
  

  sd = (p[1].x - p[0].x)*(p[1].x - p[0].x) +
    (p[1].y - p[0].y)*(p[1].y - p[0].y);


  printFloat("[CHKPT3]: distance between points (1.0) =  ", sd);
  println();
}
