void main() {
  int i = 3;
  int j = 0;
  int s = 0;

  while( i > 0 ) {
    j = 0;
    if( i + j + s > 0 ) 
      while( j < 3 ) {
	s = s + 1;
	j = j + 1;
      }
    i = i - 1;
  }

  print("[CHKPT3] s (9) = ", s);
  println();

}
