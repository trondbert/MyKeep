#include <iostream.h>
#include <string.h>

void main()
{
	char buffer[10];

	cout << "Skriv inn ein streng:\n";
	cin.get(buffer,9);
	cout << buffer << endl;

	cout << strlen("khfdkshfksd") << endl;

	strncpy(buffer, "lkgfjdfg",9);

	cout << buffer;
}