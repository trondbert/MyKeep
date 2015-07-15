#!/bin/bash


for INDEX in {1..100}
do
    curl -O http://www.imdb.com/random/title

    url=`grep HREF title | perl -pe "s/.*\"(.*)\".*/\1/g"`

    echo "http://www.imdb.com/$url"
    curl -o "../resources/title$INDEX.html" http://www.imdb.com/$url
done