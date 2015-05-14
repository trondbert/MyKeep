#include <iostream>
#include <cstring>

#include "DiwaliLights.h"
#include "MaxSubArray.h"

using namespace std;

int main(int argc, char **argv) {
    char* assignment;
    
    if (argc > 1) {
        assignment = argv[1];
        
        if (strcmp(assignment, "Diwali") == 0) {
            DiwaliLights dl;
            dl.solve();
        }
        else if (strcmp(assignment, "MaxSubArray") == 0) {
            MaxSubArray maximus;
            maximus.solve();
        }
    }
}