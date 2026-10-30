#!/usr/bin/env bash

# 1. Compile the files
javac -d out src/*.java

# 2. Run the program
java -cp out Main
