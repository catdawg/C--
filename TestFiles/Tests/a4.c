int aver(int a[], int l) {
  int s = 0;
  int i = 0;
  while(i < l ) {
    s = s + a[i];
    i = i + 1;
  }
  return s/l;
}

void main() {
  int a[4];
  int i = 0;

  while( i < 4 ) {
    a[i] = i;
    i = i + 1;
  }

  print("[CHKPT3]: average[0,1,2,3]  (1) = ", aver(a,4));
  println();
}
  
