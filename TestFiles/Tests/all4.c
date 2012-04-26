int scalar_product(int v1[], int v2[], int n) {
  int i = 0;
  int p = 0;

  while( i < n ) {
    p += v1[i]*v2[i];
    i++;
  }
  return p;
}


void main() {
  int u[5] = {0, 1, 2, 3, 4};
  int v[5] = {0, 1, 2, 3, 4};
  
  print("[CHKPT3]: scalar_product (30) = ", scalar_product(u,v,5));
  println();
}
