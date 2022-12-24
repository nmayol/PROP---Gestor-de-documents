# Gestor de documents #

## Membres: ##
* Calaf Martí, Tomàs
* [Grau Dominguez, Paula](https://github.com/paulagrauu)
* [Mayol Alcaraz, Neus](https://github.com/nmayol)
* Vazquez Gonzalez, Joan

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
