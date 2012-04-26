struct row4 {
  int c[4];
};

struct matrix4by4 {
  struct row4 r[4];
};

struct matrix4by4 transpose44(struct matrix4by4 in) {
  struct matrix4by4 out;
  int i;
  int j;

  i = 0;
  while(i < 4) {
    j = 0;
    while( j < 4) {
      out.r[j].c[i] = in.r[i].c[j];
      j++;
    }
    i++;
  }
  return out;
}

void main() {
  struct matrix4by4 m44;
  struct matrix4by4 t44;
  int i;
  int e;

  i = 0; e = 0;
  while( i < 4 ) {
    int j = 0;
    while(j < 4) {
      m44.r[i].c[j] = e;
      j++; e++;
    }
    i++;
  }

  t44 = transpose44(m44);

  print("[CHKPT3]: t44[0][3] (12) = ", t44.r[0].c[3]);
  println();

}

