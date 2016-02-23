// Listing 11.13
// Linked list simple implementation



#include <iostream.h>

//object to add to list
class Cat
{
public:
	Cat() { itsAge = 1; }
	Cat(int age, char * name)
	{		
		itsAge = age;
		itsName = name;
		#ifdef DEBUG
			cout << "Cat constructor done" << endl;
		#endif
	}
	~Cat();
	int getAge() const { return itsAge; }
	char * getName() const{ return itsName; } 
private:
	int itsAge;
	char * itsName;
};

Cat::~Cat()
{	
	#ifdef DEBUG
		cout << "I'll kill that cat!\n";
	#endif
	delete[] itsName;
	itsName = 0;
}

// manages list, orders by cat's age!

class Node
{
public:
	Node (Cat *);
	Node(){}
	~Node();
	void setNext(Node * node) { itsNext = node; }
	Node * getNext() const { return itsNext; }
	Cat * getCat() const { return itsCat; }
	void insert(Node *);
	void display();
private:
	Cat * itsCat;
	Node * itsNext;
};

Node::Node(Cat * pCat):
itsCat(pCat),
itsNext(0)
{ 
	#ifdef DEBUG
		cout << "Node Constructor!\n";
	#endif
}

Node::~Node()
{
	#ifdef DEBUG
		cout << "Deleting node...\n";
	#endif
	delete itsCat;
	itsCat = 0;
	delete itsNext;
	itsNext = 0;
}

//Insert
//Orders cats based on their ages
//Algorithm: If the cat is younger than you, insert it 
//before this one.
//Otherwise, if last in line, or if the new cat is older than you,
//and also younger than next in line, insert it after this one.
//Otherwise call insert in the next in line
void Node::insert(Node * newNode)
{	
	int newAge = newNode->getCat()->getAge();
	int thisCatsAge = itsCat->getAge();

	if ( newAge < thisCatsAge)
	{
		Cat * newCat = newNode->getCat();
		Node * oldNode = new Node(itsCat);
		oldNode->setNext(itsNext);
		itsNext = oldNode;
		itsCat = newCat;

		delete oldNode; oldNode = 0;
		return;
	}
	if(!itsNext)
	{
		itsNext = newNode;
		return;
	}
	int nextCatsAge = itsNext->getCat()->getAge();

	if ( newAge >= thisCatsAge && newAge < nextCatsAge )
	{
		newNode->setNext(itsNext);
		itsNext = newNode;
		return;
	}

	itsNext->insert(newNode);
}

void Node::display()
{
	cout << "Name: " << itsCat->getName() << ", ";
	cout << "age: " << itsCat->getAge() << endl;

	if(itsNext)
		itsNext->display();			
}

void main()
{
	int age;
	
	Node * pHead = 0; 
	Node * pNode = 0;
	Cat * pCat;

	while(1)
	{
		#ifdef DEBUG
			cout << "In while loop!" << endl;
		#endif
		cout << "New cat's age? (0 to quit): " << endl;
		cin >> age;
		if (!age)
			break;

		cin.get();

		char * name = new char[20];
		cout << "New cat's name: " << endl;		
		cin.getline(name,20);

		#ifdef DEBUG
			cout << "name: " << name << endl;
		#endif
						
		pCat = new Cat(age,name);
		pNode = new Node(pCat);
		
		if(!pHead)
			pHead = pNode;
		else
			pHead->insert(pNode);		
	}
	pHead->display();
	delete pHead;
	pHead = 0;
	cout << "Exiting...\n\n";
}