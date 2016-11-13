#include <stdlib.h>
#include <ctype.h>

char* uppercased(char* source) {
	int length = 0;
	while (source[length] != 0) {
		length++;
	}
	length += 1;

	char* result = malloc(length);
	for (int i = 0; i < length; i++) {
		result[i] = toupper(source[i]);
	}
	result[length-1] = 0;
	return result;
}

