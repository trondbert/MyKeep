//
//  MaxSubArray.h
//  Cplusplus
//
//  Created by Trond Valen on 30/03/15.
//  Copyright (c) 2015 Trond Valen. All rights reserved.
//

#ifndef Cplusplus_MaxSubArray_h
#define Cplusplus_MaxSubArray_h

#include <algorithm>
#include <iostream>
#include <vector>

class MaxSubArray {
    
    const int CONTIGUOUS = 0;
    const int NON_CONTIGUOUS = 1;
    
    long maxSubArray(std::vector<int> numbers, int mode);
    
    void solveTestcase(int numberCount);
    
public:
    void solve();
};

#endif
