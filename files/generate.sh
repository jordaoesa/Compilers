#!/bin/bash

clear

echo ">>> Starting File Generation <<<"
echo
java -jar ./../lib/jflex-1.6.1.jar Lexer.flex
echo
echo ">>> JFlex Executed <<<"
echo
java -jar ./../lib/java-cup-11a.jar -expect 2 -parser Parser -symbols sym Parser.cup
echo
echo ">>> JCUP Executed <<<"
echo
echo ">>> Moving Files <<<"
echo

mv -f Lexer.java ./../src/compiler/analysis/generated/
mv -f Parser.java ./../src/compiler/analysis/generated/
mv -f sym.java ./../src/compiler/analysis/generated/
echo ">>> Done <<<"
