void main() {
  int i = 0;
  int s = 0;

  while( i < 3 ) {
    if( i > 0 )
      s = s + i;
    i = i + 1;
  }

  print("[CHKPT3] s (3) = ", s);
  println();

}
