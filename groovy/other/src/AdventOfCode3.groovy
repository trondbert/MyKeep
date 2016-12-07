
def numberOfPossibleTriangles = 0
new FileInputStream("./inputAoC3.txt").eachLine { line->

    def nums = line.split(" +").collect {Integer.valueOf(it)}
    nums.sort()
    if (nums[2] < nums[0] + nums[1])
        numberOfPossibleTriangles += 1
}

println numberOfPossibleTriangles