#include <stdio.h>

int x = 1;
printf("XXXXX");

void main(){
  int x = 0;
  float y = 0.0;
  double z = 0.0;
  char c = 'a';
  if(1 == 2.0)
    printf("ok 1\n");
  if(1 == 'a')
    printf("ok 2\n");
  if(z == c)
    printf("ok 3\n");
  else printf("comparou int char\n");
  if(x == z) printf("ok 4\n"); else printf("comparou int double\n");
  char a = 1;

}
