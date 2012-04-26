void main() {
  int i = 0;
  int j = 1;
  int s = 0;

  if( j > 0 ) 
    while( i < 3 ) {
      s = s + j;
      i = i + 1;
    }

  print("[CHKPT3] s (3) = ", s);
  println();

}
