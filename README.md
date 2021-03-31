
Studenten App
=
Deze app is ontwikkeld om de financiële handelingen van een studentenhuis te vergemakkelijken.
Vaste kosten en boodschappen gedaan door huisgenoten kunnen ingevoerd worden en aan de eind van de maand komt er een
individueel overzicht van wat iedereen moet betalen.

Benodigde programma's:
-
- JDK Download van https://www.oracle.com/java/technologies/javase-jdk11-downloads.html en instaleer.
- IDE (Integrated development environment) Download een IDE naar keuze. Bij deze app is gebruikgemaakt van WebStorm en IntelliJ van jetbrains. Download die via www.jetbrains.com of kies er zelf een IDE.
- Om de datebase te beheren maken we gebruik van pgAdmin. Te downloaden via https://www.postgresql.org/ftp/pgadmin/pgadmin4/v4.27/windows/ Instaleer de app.  Maak gebruik van de installatie wizard en voor je op finish knop druk vink het vakje uit van de Stack Builder. Die hebben we niet nodig.
- Om te communiceren met de backend kunnen we de frontend gebruiken of Postmen. Download Postmen op https://www.postman.com/downloads/ en instaleer het op je PC
- Download Node.js op https://nodejs.org/en/download/ en instaleer het op de je PC.
- Download Maven door de instructies op de volgende pagina te volgen https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html. Om Maven in de terminal te gebruiken volg de instructies in de filmpje https://www.youtube.com/watch?v=dlPjiYyVSlc.

Installatie:
=
Frontend installatie:
-
Open de zip bestanden van de frontend en backend projecten. Sla de directories op je harde schijf.
Voor de frontend open WebStorm of je eigen IDE. Open het frontend project.
Installeer eerst Node.js en NPM door in de terminal in te typen “node -v” en daarna "npm –v".  Instaleer daarna Nodemon door “npm install- g nodemon”in de terminal in te typen.
Haal alle dependencies op door “npm install” in de terminal in te typen.
Type in de terminal "npm install react-router-dom" in om de dependencies voor de routing binnen te halen.
Om het project te starten typ "npm start" in de terminal. Het project wordt nu gestart in je webbrowser.

Backend installatie:
-
Open je eigen IDE of IntelliJ en open het backend project. Druk op de play knop om het project te starten.
Maak in een PGadmin een database aan met de naam studentapp op port “5432”
Vul bij de application.properties je eigen username en password in. Bij jwtsecret je eigen secretkey. Vul bij de email je eigen email gegevens in. Je kunt gebruik maken van Mailtrap. Via https://mailtrap.io/

Rollen project:
-
USER_ROLE deze gebruiker kan declaraties toevoegen, zijn eigen profiel wijzigen, gedane declaraties in zien, te betalen rekeningen zien, vaste kosten huis zien en huisgenoten zien.
USER_MODERATOR kan alles van de USER_ROLE en huisgenoten toevoegen, declaraties keuren, rekeningen op betaalt zetten, huisgenoten verwijderten/promoveren en profiel/kosten van het huis aanpassen.

Test gebruikers:
-
FirstName : "Bla"
LastName : "Blatenstein"
Username : "ews"
Email : "kansloos@gmail.com"
DateOfBirth :"01-02-2000"
Password : "password"
Role : ROLE_MODERATOR

FirstName : "Bladibla"
LastName : "van Bla"
Username : "bla"
Email : "kansarm@gmail.com"
DateOfBirth :"03-04-2001"
Password : "password"
Role : ROLE_MODERATOR

FirstName : "Bla"
LastName : "MacBla"
Username : "macbla"
Email : "MacBla@gmail.com"
DateOfBirth :"01-02-1903"
Password : "password"
Role : ROLE_MODERATOR

FirstName : "Blaat"
LastName : "Macbla"
Username : "macblaat"
Email : "MacBlaBla@gmail.com"
DateOfBirth :"01-05-1949"
Password : "password"
Role : ROLE_USER

Restendpoints:
=
AuthenticatedController
=======================
Signin:
-------
URL: http://localhost:8080/api/auth/signin

POST-request

JSON: { "username" : "macbla", "password" : "password" }

Geeft een JWT-token terug.

Signup:
------
URL: http://localhost:8080/api/auth/signup

POST-request

JSON : { "firstName" : " ", "lastName" : " ", "username" : " ", "email" : " " , "dateOfBirth" : " " , "password" : " " }

Alle velden zijn verplicht.

Signup voor huis
----------------
URL: http://localhost:8080/api/auth/signup/house

POST-request

JSON { "firstName" : " ", "lastName" : " ", "username" : " " , "dateOfBirth" : " " , "password" : " " }

Alle velden zijn verplicht. 

UserController
=

Signin met JWT-token
-
URL: http://localhost:8080/api/users/jwtlogin

GET-request

JSON

Verkrijg de token door eerst een signin te doen. Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Je krijgt user informatie terug.

Voeg huisgenoot toe
-
URL: http://localhost:8080/api/users/roommate

POST-request

JSON { "firstName" : " ", "lastName" : " " , "email" : " " }

email is verplicht.

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR. Email is verplicht. Geeft een email terug.

Update huisgenoot
-
URL: http://localhost:8080/api/users/update

POST-request

JSON { "firstName" : " ", "lastName" : " ", "dateOfBirth" : " " , "password" : " " , "passwordRepeat" : " " }

Password en passwordRepeat moeten beide worden ingevuld en gelijk aan elkaar zijn. Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER.

Haal eigen profiel op
-
URL: http://localhost:8080/api/users/download

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER. Geeft profiel user terug.

Haal huisgenoten op
-
URL: http://localhost:8080/api/users/all

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. 
Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER. Geeft alle mede huisgenoten terug.

Verwijder huisgenoot
-
URL: http://localhost:8080/api/users/delete/{username}

DELETE-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Vul op de plek van username de naam van de te verwijderen huisgenoot op. Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR.

DeclarationController
=
Declaratie toevoegen
-
URL: http://localhost:8080/api/declarations/upload

POST-request

JSON { "amount" : "12.34 ", "fileName" : "jpg in base64 String" }

Amount moet een double zijn. Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER.

Declaraties ophalen
-
URL: http://localhost:8080/api/declarations/all

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR. Je krijgt een lijst met alle declaraties terug.

Declaraties ophalen van user
-
URL: http://localhost:8080/api/declarations/personal/{checked}

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER.
Geeft gecontroleerde declaraties terug bij checked=true. Geeft ongecontroleerde/veranderde declaraties terug bij checked=false.

Declaraties veranderen
-
URL: http://localhost:8080/api/declarations/edit

PUT-request

JSON { "fileName" : "jpg in base64 String", "amount" : "12.34", "id" : "id van declaratie" }

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. 
Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER.

Declaratie ophalen die verander moet worden
-
URL: http://localhost:8080/api/declarations/edit/{id}

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER. Geeft een declaratie terug die verandert kan worden.

Declaratie goed of afkeuren
-
URL: http://localhost:8080/api/declarations/checked

PUT-request

JSON {"id" : "id" , "correct" : "true/false" }

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR. Verandert een declaratie naar correct of veranderbaar.

Declaratie verwijderen
-
URL: http://localhost:8080/api/declarations/delete/{id}

DELETE-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR. Verwijdert de declaratie met de {id}.

AccountController
=
Account updaten
-
URL: http://localhost:8080/api/accounts/update

POST-request

JSON { "accountNumber" : "1234567890", "waterUtility" : "12.34 ", "gasUtility" : "12.34" , "elektraUtility" : "12.34", "internetUtility" : "12.34" }

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin. De utilitys moeten een double zijn. Alleen toegankelijk voor user met de ROLE_MODERATOR. 

Account info downloaden
-
URL: http://localhost:8080/api/accounts/download

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER. Geeft de account gegevens terug.

BillController
=
Rekening is betaald zetten
-
URL: http://localhost:8080/api/bills/payed/{billId}

PUT-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR. Verandert de rekening met {billId} van een huisgenoot naar betaald.

Test rekeningen maken
-
URL: http://localhost:8080/api/bill/create/{houseId}

PUT-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR. De bedoeling van de studentapp is om bij elke nieuwe maand een nieuwe rekening te maken.
Om niet een hele maand te hoeven wachten kun je hier een PUT-request naar sturen die het jaar van de rekeningen en declaraties verandert in een willekeurig jaar.

Haal alle rekeningen op
-
URL: http://localhost:8080/api/house/{houseId}

GET-request

JSON

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR. Geeft alle rekeningen van het huis {houseId} terug.

Haal personlijke rekeningen op
-
URL: http://localhost:8080/api/personel/{payed}

Get-request

Vul voor de header Authorization in “Bearer jwt-token". Waarbij de jwt-token is vervangen door de token verkregen bij signin.
Alleen toegankelijk voor user met de ROLE_MODERATOR en ROLE_USER. Geeft bij {false} de onbetaalde rekeningen terug en bij {true} de betaalde rekeningen terug.