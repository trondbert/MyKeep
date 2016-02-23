#include <iostream.h>

char fargar[5][6] = {"kvit","svart","raud","blaa","groen"};
enum FARGE {KVIT, SVART, RAUD, BLAA, GROEN};

class Bil
{
public:
	Bil(FARGE farge):farge(farge)
	{
		//cout << "Bil-konstruktor...\n";
		antall++;
	}
	~Bil(){ cout << "Bil-destruktor...\n"; antall--; }			
	FARGE hentFarge(){ return farge; }
	static long hentAntall() { return antall; }
private:
	FARGE farge;
	static long antall;

};

long Bil::antall = 0;

void main()
{
	long (*pAntall)() = Bil::hentAntall;

	const antallBilar = 5;
	Bil * bilArray[antallBilar];
	for (int i = 0; i < antallBilar; i++)
	{
		bilArray[i] = new Bil(FARGE(i));
		cout << "Ny bil!\tfarge: " << fargar[i] << "\t";
		cout << "antall bilar: " << pAntall() << "\n\n"; 
	}
	for (i = 0; i < antallBilar; i++)
	{
		delete bilArray[i];
		bilArray[i] = 0;
		cout << "Antall bilar igjen: " << pAntall() << endl;  
	}
}
