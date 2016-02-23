//Listing 10.1 Overloading class member functions
#include <iostream.h>

typedef unsigned short int USHORT;
enum BOOL { FALSE, TRUE };

// Rectangle class declaration
class Rectangle
{
public:
	//constructors
	Rectangle(USHORT width, USHORT height);
	~Rectangle(){}

	//overloaded class function drawShape
	void drawShape() const;
	void drawShape(USHORT aWidth, USHORT aHeight) const;

private:
	USHORT itsWidth;
	USHORT itsHeight;
};

//Constructor implementation
Rectangle::Rectangle(USHORT width, USHORT height)
{
	itsWidth = width;
	itsHeight = height;
}

//overloaded drawShape-takes no values
//draws shapes based on current class member values
void Rectangle::drawShape() const
{
	drawShape(itsWidth, itsHeight);	
}

// overloaded drawShape - takes two values 
// draws shape based on the parameters
void Rectangle::drawShape(USHORT width, USHORT height) const
{
	for (USHORT i = 0; i < height; i++)
	{
		for(USHORT j = 0; j < width; j++)
		{
			cout  << "*";
		}

		cout << "\n";
	}
}

// Driver program to demonstrate overloaded functions
void main()
{
	//initialize a rectangle to 30,5
	Rectangle theRect(30,5);
	cout << "drawShape():\n";
	theRect.drawShape();
	cout << "\ndrawShape(40,2): \n";
	theRect.drawShape(40,2);
}


