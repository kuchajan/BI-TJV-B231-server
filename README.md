# BI-TJV-B231-server

This is a repository for the server part of a semestral project for the course named Java Technology at FIT CTU. It is a REST API server using Spring Framework for a made up car lease company. The client application can be found [here](https://github.com/kuchajan/BI-TJV-B231-client).

There is a bug that I did not have enough time back then to solve, where after inserting test data through an insert script, the primary keys in the application would not synchronize with PKs in the database. The bodge-style solve is to run `fixPK.sh`, which POSTs to the api just enough data to synchronize the keys.

At development, I was still learning what docker compose is, so currently, it only starts a postgres database.

Below is the original README content in czech:


# Půjčovna aut: Semestrální práce z BI-TJV v semestru B231 - serverová část

Toto je repozitář pro serverovou část semestrální práce. Pokud hledáte klientskou část, je [zde](https://gitlab.fit.cvut.cz/kuchaj19/bi-tjv-b231-semestral-client).

Věci k prvnímu kontrolnímu bodu jsou obsaženy ve složce "first_control_point". Pokud je to podstatný, chodím na cvičení na paralelku č. 104.

Vše, kromě prvního kontrolního bodu, je vypracováno v angličtině.

## Spuštění

Ke spuštění jsou potřeba nástroje:
 - Docker-compose
 - Java verze 17
 - Gradle

Abyste spustili db server, navigujte do adresáře serverového repoziráře a spusťte příkaz:
```
docker-compose up
```
Ten bude dostupný na postgresql://localhost:5432/carlease, jméno: admin, heslo: admin.

Abyste spustili API server, navigujte do adresáře serverového repozítáře a spusťte příkaz:
```
./gradlew bootRun
```
Ten bude dostupný na http://localhost:8080/
Bude zpřístupněna i dokumentace API na http://localhost:8080/swagger-ui/index.html

Abyste spustili klientskou aplikaci, navigujte do adresáře klientského repozitáře a spusťe příkaz:
```
./gradlew bootRun
```
Ta bude zpřístupněna na http://localhost:8081/ jako webová aplikace.