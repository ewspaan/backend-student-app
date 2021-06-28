Studenten App
=
Deze app is ontwikkeld om de financiële handelingen van een studentenhuis te vergemakkelijken.
Vaste kosten en boodschappen gedaan door huisgenoten kunnen ingevoerd worden en aan de eind van de maand komt er een
individueel overzicht van wat iedereen moet betalen.

Benodigde programma's:
-
- IDE (Integrated development environment) Download een IDE naar keuze. Bij deze app is gebruikgemaakt van WebStorm en IntelliJ van jetbrains. Download die via www.jetbrains.com of kies er zelf een IDE.
- Om de datebase te beheren maken we gebruik van pgAdmin. Te downloaden via https://www.postgresql.org/ftp/pgadmin/pgadmin4/v4.27/windows/ Instaleer de app.  Maak gebruik van de installatie wizard en voor je op finish knop druk vink het vakje uit van de Stack Builder. Die hebben we niet nodig.
- Download Node.js op https://nodejs.org/en/download/ en instaleer het op je PC.

Installatie:
=
Frontend installatie:
-
Open de zip bestanden van de frontend. Sla de directories op je harde schijf.
Voor de frontend open WebStorm of je eigen IDE. Open het frontend project.
Installeer eerst Node.js en NPM door in de terminal in te typen “node -v” en daarna "npm –v".  Instaleer daarna Nodemon door “npm install- g nodemon”in de terminal in te typen.
Haal alle dependencies op door “npm install” in de terminal in te typen.
Type in de terminal "npm install react-router-dom" in om de dependencies voor de routing binnen te halen.
Om het project te starten typ "npm start" in de terminal. Het project wordt nu gestart in je webbrowser.

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

Gebruik website:
=
Log in als 1 van de testuser die de ROLE_MODERATOR heeft. Druk bij macbla eerst op de knop "Huisrekening overzicht". Zo
wordt er voor die user een rekening gemaakt. Gebruik de buttons om te navigeren door de website.