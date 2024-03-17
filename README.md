# Avvimpa
Avvita e stampa

## Descrizione
Ascolta la porta seriale in attesa di una riga che termina con `program end`.
A quel punto crea un PDF con la data e l'ora indicata sulla riga appena letta. Il PDF viene stampato sulla stampante indicata come parametro di avvio programma.

## Prerequisiti
### Sviluppo
Sono necessari **Java Development Kit** e **Maven**.
### Esecuzione
È necessario **Java Runtime Environment**

## Build
Eseguire
````
mvn install
````
Nella cartella _target_ vengono creati i file **avvimpa-0.5.jar** e **avvimpa-0.5-jar-with-dependencies.jar** dove _0.5_ è il numero di versione.

## Esecuzione
Eseguire il jar grazie al comando
````
java -jar avvimpa-0.5-jar-with-dependencies.jar "nome della Stampante"
````
Dove **"nome della stampante"** è il nome della stampante etichettatrice. Se non viene specificato verrà usato quello indicato nel codice.
