int fact(int n) {
  int f = 1;

  while( n > 1 ) {
    f *= n;
    n--;
  }

  return f;
}

void main() {

  print("[CHKPT 3]: 5! = ", fact(5));

}
