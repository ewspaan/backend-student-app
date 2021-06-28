Studenten App React Native
=
In de React Native is het mogelijk om een declaratie te doen via de telefoon.

Benodigde programma's:
-
- IDE (Integrated development environment) Download een IDE naar keuze. Bij deze app is gebruikgemaakt van WebStorm en IntelliJ van jetbrains. Download die via www.jetbrains.com of kies er zelf een IDE.
- Om de datebase te beheren maken we gebruik van pgAdmin. Te downloaden via https://www.postgresql.org/ftp/pgadmin/pgadmin4/v4.27/windows/ Instaleer de app.  Maak gebruik van de installatie wizard en voor je op finish knop druk vink het vakje uit van de Stack Builder. Die hebben we niet nodig.
- Download Node.js op https://nodejs.org/en/download/ en instaleer het op je PC.

Installatie:
=
React Native installatie:
-
- Instaleer expo met 'npm install -g --unsafe-perms expo-cli'.
- Instaleer de camera elementen met 'expo install expo-camera'.
- Instaleer Axios met 'npm install axios'.
- Instaleer de AsyncStorage met 'npm install @react-native-async-storage/async-storage'.
- Instaleer FileSystem met 'expo install expo-file-system'.
- Instaleer React Native Elements met 'npm install react-native-elements'.  
- Open het zip bestand en verplaats de folder naar je projecten volder van je IDE.
- Open de directory waar je project in staat en typ in de console 'npm run start'. Het project opent in de webbrowser.
- Instaleer expo op je telefoon en scan de QR-code die op de webbrowser staat.


Test gebruikers:
-

Username : "ews"
Password : "password"
Role : ROLE_MODERATOR

Username : "bla"
Password : "password"
Role : ROLE_MODERATOR

Username : "macbla"
Password : "password"
Role : ROLE_MODERATOR

Username : "macblaat"
Password : "password"
Role : ROLE_USER

Gebruik app:
=
Login in als één van de test gebruikers. Zorg dat de app je camara kan gebruiken. Het is nu mogelijk een foto te maken van een bonnetje en een bedrag in te vullen en die declaratie te versturen naar de backend. 