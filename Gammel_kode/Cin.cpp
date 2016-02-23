#include <iostream.h>
#include <stdio.h>
#include <string.h>

void main()
{
	
	char navn[30];
	printf("Hallo\n");
	printf("%s\n","Skriv inn ditt fulle navn:");
	cin.getline(navn,30);
	cout << navn << endl;
	
	char passord[8];
	
	cout << "Skriv inn ditt passord:";
	
	cin.getline(passord,9);
	
	cout << "strlen(passord): " << strlen(passord) << endl;
	cout << "passord: " << passord << endl;

	cin.ignore();

	printf("%s\n","Skriv inn ditt fulle navn:");
	
	cin.getline(navn,30);
	cout << navn << endl;	
	
}