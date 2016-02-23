#include <iostream.h>
#include "Katt.hpp"

class Gud
{
public:	
	void taLiv(int * pLiv){*pLiv -= 1;}
};
	
inline int Katt::hentAlder() const
{
	return alder;
}

void Katt::settAlder(int nyAlder)
{	
	this->alder = nyAlder;
}

int Katt::hentLiv()
{
	return liv;
}

void Katt::mistLiv()
{
	Gud gud;
	int * pLiv = &liv;
	gud.taLiv(pLiv);
}

Katt::Katt():
	alder(0), liv(9)
{}

Katt::Katt(int startAlder)
{
	alder = startAlder;
	liv = 9;
}

Katt::~Katt()
{
	cout << "Kattedestruktor\n";
}

void mistLiv(int * pLiv);

void main()
{
	Katt sofus(12);
	cout << "Katten er " << sofus.hentAlder() << " år." << endl;
	cout << "Katten har " << sofus.hentLiv() << " liv." << endl;
	
	sofus.mistLiv();

	cout << "Katten har " << sofus.hentLiv() << " liv." << endl;

	cout << "\n";
}

