class Katt
{
public:
	Katt(int startAlder);
	Katt();
	int hentAlder() const;
	void settAlder(int nyAlder);
	void mjau() const{ cout << "Mjauu...";} //inline
	
	int hentLiv();
	void mistLiv();
	~Katt();

private:
	int alder;
	int liv;
};