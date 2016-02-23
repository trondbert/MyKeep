#include <iostream.h>

void main()
{
	//int * numbers = new int[20];

	for(int i = 0; i < 20; i++)
	{
	//	cin >> numbers[i];
		//cout << "-> " << numbers[i] << endl;
	}

	int startIndex = 0, stopIndex = 0;

	int j = 0, sum = 0, newSum = 0;

	while(j < 19)
	{
	//	newSum += numbers[j];
		
		if(newSum > sum)
		{
			sum = newSum;
			stopIndex = j;
		}

		if(newSum < 0)
		{
			sum=0;
			startIndex = j + 1;
		}
		j++;
	}

	cout << startIndex << "\t" << stopIndex << endl;

	//delete [] numbers;

}