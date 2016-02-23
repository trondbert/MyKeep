#include <iostream.h>
#include <math.h>
#include <string.h>

enum STATUS { BORTE, HEIME };

class Tull
{
public:

	Tull():status(1){}
	unsigned status;
};

char* intToBitString(char*, unsigned int);
int erDelelige(int, int);

void main()
{
	Tull tulla;
	unsigned int stats = tulla.status;

	cout << sizeof stats << endl;
	cout << powl(2,8) << endl;

	char string[8];
	
	cout << "154:\t\t" << intToBitString(string,154) << endl;
	cout << "234:\t\t" << intToBitString(string,234) << endl << endl;
	cout << "154&234:\t";
	int tal = 154 & 234;
	cout << intToBitString(string,tal) << endl;
	
	cout << "154|234:\t";
	tal = 154 | 234;
	cout << intToBitString(string,tal) << endl;
	
	cout << "154^234:\t";
	tal = 154 ^ 234;
	cout << intToBitString(string,tal) << endl;


}

char* intToBitString(char buffer[], unsigned int num)
{
	int length = (int) (ceill(log(num)/log(2)));
	//cout << "length: " << length << endl;
  	int buflen = strlen(buffer);
	if( buflen < length+1 )
		return '\0';
	
	for(int i = 0; i < buflen; i++)
	{
		buffer[i] = '0';
	}

	for(i = 0; i < length; i++)
	{
		if (num==0)
			break;
		else if (erDelelige(num,(int)powl(2,i+1)))
		{
			//cout << powl(2,i+1) << " deler " << num << endl;
		}
		else
		{
			//cout << powl(2,i+1) << " deler ikkje " << num << endl;
			buffer[buflen-1-i] = '1';
			num = num - (int)powl(2,i);
		}

	}

	buffer[buflen] = '\0';
	return buffer;
	
}

int erDelelige(int stortTal, int liteTal)
{
	double kvotient = (double)stortTal / liteTal;
	if ( kvotient == ceill(kvotient))
		return 1;
	else 
		return 0;
}