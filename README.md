# Gestor de documents #

## Membres: ##
* Calaf Martí, Tomàs (tomas.calaf.marti@estudiantat.upc.edu)
* Grau Dominguez, Paula (paula.grau@estudiantat.upc.edu)
* Mayol Alcaraz, Neus (neus.mayol@estudiantat.upc.edu)
* Vazquez Gonzalez, Joan (joan.vazquez.gonzalez@estudiantat.upc.edu)

## Instruccions execució terminal ##

    // lib
        - hamcrest-core-1.3.jar
        - junit-4.12.jar
    
    // COMPILAR TEST
    
    javac -cp ./FONTS:./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar ./FONTS/test/utils/TestTrie.java
    
    // EXECUTAR TEST
     
    java -cp ./FONTS:./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.utils.TestTrie
    
    // COMPILAR DRIVER (dins de FONTS)
    
    javac drivers/DriverCtrlDomini.java
    
    // EXECUTAR DRIVER (dins de FONTS)
    
    java drivers/DriverCtrlDomini

    ______________________________________________________________________________________

    // COMPILAR CTRLPRESENTACIO (crea carpeta prova a EXE)
    
    javac -cp ./FONTS:./lib/forms_rt.jar -d ./EXE/prova ./FONTS/presentacio/Main.java
    
    // EXECUTAR CTRLPRESENTACIO
    
    java -cp ./EXE/prova:./lib/forms_rt.jar presentacio.Main
