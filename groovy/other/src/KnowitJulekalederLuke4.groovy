def substIndex = 0
def leList = (1..1337).inject([]) { list, num ->
    if (num % 7 == 0 || num % 10 == 7 || ((int)(num % 100 / 10)) == 7 || ((int)(num % 1000 / 100)) == 7)
        list.push list.get(substIndex++)
    else
        list.push num
    list
}

println( leList[1337 - 1] )
