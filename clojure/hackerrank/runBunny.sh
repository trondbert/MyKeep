#!/bin/sh

echo $1

echo $1 | java -cp /usr/local/clojure-1.7.0/clojure-1.7.0.jar clojure.main -i jumpingbunnies.clj 
