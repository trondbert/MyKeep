#include <iostream.h>
const int defaultLengde = 10;
// Dyr-klasse slik at vi kan lage ein
// array av Dyr


class Dyr
{
public:
	Dyr(int eiVekt = 3, int dagar = 5):vekt(eiVekt), dagarILive(dagar){}
	~Dyr(){}
	int hentVekt() const { return vekt; }
	void settVekt(int eiVekt) { vekt = eiVekt; }
	int hentDagar() const { return dagarILive; }
	void settDagar(int dagar) { dagarILive = dagar; }
private:
	int vekt;
	int dagarILive;
};

template <class T>
class Array
{
public:
	//konstruktorar
	Array(int lengde = defaultLengde);
	Array(const Array& rhs);
	~Array(){ delete[] pType; }

	//operatorar
	Array& operator=(const Array& rhs);
	T& operator[] (int indeks) { return pType[indeks]; }
	const T& operator[](int indeks) const { return pType[indeks]; }

	//aksessorar
	int hentLengde() const { return lengde; }

private:

	int lengde;
	T* pType;
};

//implementasjon...

//konstruktor
template <class T>
Array<T>::Array(int eiLengde):lengde(eiLengde)
{
	pType = new T[lengde];
	for(int i = 0; i < lengde; i++)
		pType[i] = 0;
}

//kopi-konstruktor
template <class T>
Array<T>::Array(const Array<T>& rhs):lengde(rhs.hentLengde())
{
	pType = new T[lengde];
	for(int i = 0; i < lengde; i++)
		pType[i] = rhs[i];
}

//operator =
template <class T>
Array<T>& Array<T>::operator=(const Array<T>& rhs)
{
	if(this==&rhs)
		return *this;
	
	delete[] pType;
	lengde = rhs.hentLengde();
	pType = new T[lengde];
	for(int i = 0; i < lengde; i++)
		pType[i] = rhs[i];
	return *this;
}

void main()
{
	Array<int> arrayen;
	Array<Dyr> dyrehage;

	Dyr * pDyr;

	for(int i = 0; i < arrayen.hentLengde(); i++)
	{
		arrayen[i] = i*2;
		pDyr = new Dyr(i*3,i*2);
		dyrehage[i] = *pDyr;
	}
	
	for(i =0; i < arrayen.hentLengde(); i++)
	{
		cout << "arrayen[" << i << "]: " << arrayen[i] << "\t";
		cout << "dyrehage[" << i << "]: " 
		<< dyrehage[i].hentVekt() << "\n";
	}

	for(i = 0; i < arrayen.hentLengde(); i++)
		delete &dyrehage[i];

}

