int f[10];

void fibonacci(int n) {
  int i;
  f[0] = 1;
  f[1] = 1;
  i = 2;
  while( i <= n ) {
    f[i] = f[i-1] + f[i-2];
    i++;
  }
}


void main() {
  int n;
  n = 9;

  fibonacci(n);
  print("[CHKPT3]: fibonacci(9) (55) = ", f[9]);
  println();
}
