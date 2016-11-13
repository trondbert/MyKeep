#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "stringutils.h"

int main(int argc, char **argv) {
	int arr1[100];
	int arr2[200];

	printf("%p\n", arr1);
	printf("%p\n", arr2);
	printf("%d\n", (int)(&arr1[0] - &arr2[0]));

	char string[] = "foæo";
	printf("%s\n", uppercased(string));
	printf("%p\n", &string[1]);
	printf("%p\n", &string[3]);

	char* letter = &string[0];
	printf("%04x\n", string[1]);
	printf("%04x\n", string[2]);

	printf("---\n");
	for (int i = 0; i < 12; i++) {
		printf("%01x\n", *letter);
		letter += 1;
	}
	

	return EXIT_SUCCESS;
}
