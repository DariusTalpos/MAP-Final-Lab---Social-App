# MAP Final Lab - Social App

EN: This application is the final version of the homework received at the "Advanced Programming Methods" course at Babeș-Bolyai University.
It is an application that stores data using a PostgreSQL database, and has multiple functionalities:
1. Log In - by starting the application, the user can log in by using a valid username and the associated password in the log in window.
2. Viewing friends, friend requests and other users - the main menu of the application has multiple buttons representing tabs which display different types of users (friends, users that sent friend requests to the user, users that the user sent friend requests to, and the other users).
3. Sending/accepting/declining friend requests - depending on the tab selected, the user can choose to select another and send them a friend request (if they are on the "others" tab) or either accepting or declining their request (if they are on the "users that sent friend requests" tab).
4. Sending and viewing messages - by choosing a friend, the user can view the messages sent between the two and converse in real time.
5. Log Out - by pressing the "Log out" button in the main menu, the user will log out of the application.

This application was built using the Java programming language with the GUI being created by using JavaFX. The observer pattern is used in order to display conversation taking place in real time.
This application does not have any network features, everything is done locally.

RO: Această aplicație este versiunea finală a temei primite în cadrul materiei "Metode Avansate de Programare" la Universitatea Babeș-Bolyai.
Este o aplicație care stochează date utilizând o bază de date PostgreSQL și are multiple funcționalități:
1. Log In - prin pornirea aplicației, utilizatorul poate să se conecteze prin introducerea unui nume de utilizator și a parolei aferente în fereastra de log in.
2. Vizualizarea prietenilor, a cererilor de prietenie și a celorlalți utilizatori - meniul principal al aplicației conține multiple butoane reprezentând tab-uri care prezintă diferite tipuri de utilizatori (prieteni, utilizatori care au trimis cereri de prietenie utilizatorului, utilizatori care au primit de la utilizator cereri de prietenie, și ceilalți utilizatori).
3. Trimitere/acceptare/refuzare de cereri de prietenie - în funcție de tab-ul selectat, utilizatorul poate să aleagă un altul și să le trimită o cerere de prietenie (dacă se află în tab-ul "others") sau fie să le accepte, fie să le refuze cererea de prietenie (dacă se află în tab-ul "users that sent friend requests").
4. Trimiterea și vizualizarea de mesaje - prin selectarea unui prieten, utilizatorul poate să vizualizeze mesajele trimise de cei doi și să converseze în timp real.
5. Log Out - prin apăsarea butonului "Log out" în meniul principal, utilizatorul se poate deconecta de la aplicație.

Această aplicație a fost construită utilizând limbajul de programare Java iar GUI-ul a fost creat utilizând JavaFX. Șablonul de proiectare (pattern-ul) observer este utilizar pentru a prezenta desfășurarea conversației în timp real.
Această aplicație nu conține elemente de networking, totul desfășurându-se la nivel local.
