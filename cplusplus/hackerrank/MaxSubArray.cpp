#include "MaxSubArray.h"

using namespace std;

long MaxSubArray::maxSubArray(vector<int> numbers, int mode) {
    long bestSum =     -10000000;
    long currentSum =  -10000000;

    for ( int i = 0; i < numbers.size(); i++ ) {
        if (numbers[i] < 0 && mode == CONTIGUOUS && currentSum > bestSum) // We have peaked...
            bestSum = currentSum;
        
        if (numbers[i] >= 0) {          // Add to sum, or start fresh if current sum is negative and this number is positive
            currentSum = max(currentSum, 0L) + numbers[i];
        }
        else if (numbers[i] > currentSum) { // If this number is negative and still greater than current sum, cut off
            currentSum = numbers[i];
        }
        else if (mode == CONTIGUOUS) {      // We take a negative hit, hoping for positives down the road
            currentSum += numbers[i];
        }
    }
    return max(currentSum, bestSum);
};

void MaxSubArray::solveTestcase(int numberCount) {
    vector<int> numbers;
    for (int i = 0; i < numberCount; i++) {
        int temp; cin >> temp;
        numbers.push_back(temp);
    }
    cout << maxSubArray(numbers, CONTIGUOUS) << " ";
    cout << maxSubArray(numbers, NON_CONTIGUOUS) << endl;
};

void MaxSubArray::solve() {
    int testcaseCount = 0, numberCount = 0;
    
    cin >> testcaseCount;
    for (int tc = 0; tc < testcaseCount; tc++) {
        cin >> numberCount;
        solveTestcase(numberCount);
    }
};