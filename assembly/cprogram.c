#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(int argc, char *argv[]) {
	time_t now;
  	time(&now);

  	printf("%s", ctime(&now));

  	return 0;
}

