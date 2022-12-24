@echo off

javac -cp "./FONTS;./lib/forms_rt.jar" -d ./EXE -encoding UTF-8 ./FONTS/presentacio/Main.java

java -cp "./EXE;./lib/forms_rt.jar" presentacio.Main

goto :EOF