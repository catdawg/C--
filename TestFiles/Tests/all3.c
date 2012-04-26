int fibonacci(int n) {
  int i;
  int fm2 = 1;
  int fm1 = 1;
  int f = 1;
  
  i = 2;
  while( i <= n ) {
    f = fm2 + fm1;
    fm2 = fm1;
    fm1 = f;
    i++;
  }
  return f;
}


void main() {
  int n;
  int fib;
  n = 9;

  fib = fibonacci(n);
  print("[CHKPT3]: fibonacci(9) (55) = ", fib);
  println();
}
