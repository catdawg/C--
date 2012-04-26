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
  struct matrix4by4 m44 = {{{{0,1,2,3}},{{4,5,6,7}},
			    {{8,9,10,11}},{{12,13,14,15}}}};
  struct matrix4by4 t44;

  t44 = transpose44(m44);

  print("[CHKPT3]: t44[0][3] (3) = ", t44.r[3].c[0]);
  println();

}

