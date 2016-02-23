#include <iostream.h>

enum FARGE { RAUD, BLAA, GROENN };
enum BOOL { FALSE, TRUE };

class Dyr
{
public:
	Dyr(int);
	virtual ~Dyr() { cout << "Dyr-destruktor\n"; }
	virtual int hentAlder() const { return alder; }
private:
	int alder;
};

Dyr::Dyr(int enAlder):
alder(enAlder)
{
	cout << "Dyr-konstruktor\n";
}

class Hest:public Dyr
{
public:
	Hest(FARGE enFarge, int eiHogde, int alder);
	virtual ~Hest() { cout << "Hest-destruktor\n"; }
	virtual void vrinsk() const { cout << "Vriinsk!\n"; }
	virtual int hentHogde() const { return hogde; }
	virtual int hentAlder() const { return Dyr::hentAlder(); }
	virtual FARGE hentFarge() const { return farge; }
	 
protected:
	int hogde;
	FARGE farge;
};

Hest::Hest(FARGE enFarge, int eiHogde, int alder):
Dyr(alder), farge(enFarge), hogde(eiHogde)
{
	cout << "Hest-konstruktor\n";
}

class Fugl:public Dyr
{
public:
	Fugl(FARGE enFarge, BOOL migrerer, int alder);
	virtual ~Fugl() { cout << "Fugl-destruktor\n"; }
	virtual void kvitre() const { cout << "Kvitre, kvitre...\n"; }
	virtual void fly() const{ cout << "Eg kan fly, eg kan fly!\n"; }
	virtual FARGE hentFarge() const { return farge; }
	virtual BOOL hentMigrering() const { return migrerer; }

protected:
	BOOL migrerer;
	FARGE farge;
};

Fugl::Fugl(FARGE enFarge, BOOL migrering, int alder):
Dyr(alder), migrerer(migrering), farge(enFarge)
{
	cout << "Fugl-konstruktor\n";
}

class Pegasus:public Hest, public Fugl
{
public:
	~Pegasus(){cout << "Pegasus-destruktor\n";}
	Pegasus(FARGE, int, BOOL, long, int);
	void kvitre() const { cout << "Vriinsk!\n"; }
	virtual long hentAntOverbeviste() const 
	{
		return antallOverbeviste;
	}
	virtual FARGE hentFarge() const { return Hest::farge; }
	virtual int hentAlder() const { return Hest::hentAlder(); }
	
private:
	long antallOverbeviste;
};

Pegasus::Pegasus(FARGE enFarge,
				 int eiHogde,
				 BOOL migrering,
				 long antOverbeviste,
				 int enAlder):
Hest(enFarge, eiHogde, enAlder),
Fugl(enFarge, migrering, enAlder),
antallOverbeviste(antOverbeviste)
{
	cout << "Pegasus-konstruktor\n";
}

void main()
{
	Pegasus * pPeg = new Pegasus(GROENN, 5, TRUE, 20, 3);
	int alder = pPeg->hentAlder();
	cout << "Denne pegasusen er " << alder << " aar gamal "
		<< "og " << pPeg->hentAntOverbeviste() 
		<< " personar trur at den finst.\n";
	delete pPeg;	
}
