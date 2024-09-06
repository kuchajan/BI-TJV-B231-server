#!/bin/sh
#users: 3
curl -i -X POST -H 'Content-Type: application/json' -d '{"email": "a@a.net", "name": "a"}' http://localhost:8080/user
curl -i -X POST -H 'Content-Type: application/json' -d '{"email": "a@a.net", "name": "a"}' http://localhost:8080/user
curl -i -X POST -H 'Content-Type: application/json' -d '{"email": "a@a.net", "name": "a"}' http://localhost:8080/user
#reservations: 3
curl -i -X POST -H 'Content-Type: application/json' -d '{"timeStart": 0, "timeEnd": 1, "reservationMaker": {"id":1}, "carReserved": {"id":1}}' http://localhost:8080/reservation
curl -i -X POST -H 'Content-Type: application/json' -d '{"timeStart": 0, "timeEnd": 1, "reservationMaker": {"id":1}, "carReserved": {"id":1}}' http://localhost:8080/reservation
curl -i -X POST -H 'Content-Type: application/json' -d '{"timeStart": 0, "timeEnd": 1, "reservationMaker": {"id":1}, "carReserved": {"id":1}}' http://localhost:8080/reservation
#cars: 5
curl -i -X POST -H 'Content-Type: application/json' -d '{"registrationPlate": "1T", "features":[], "make": {"id": 1}}' http://localhost:8080/car
curl -i -X POST -H 'Content-Type: application/json' -d '{"registrationPlate": "1T", "features":[], "make": {"id": 1}}' http://localhost:8080/car
curl -i -X POST -H 'Content-Type: application/json' -d '{"registrationPlate": "1T", "features":[], "make": {"id": 1}}' http://localhost:8080/car
curl -i -X POST -H 'Content-Type: application/json' -d '{"registrationPlate": "1T", "features":[], "make": {"id": 1}}' http://localhost:8080/car
curl -i -X POST -H 'Content-Type: application/json' -d '{"registrationPlate": "1T", "features":[], "make": {"id": 1}}' http://localhost:8080/car
#makes: 3
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "baseRentPrice": 1}' http://localhost:8080/make
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "baseRentPrice": 1}' http://localhost:8080/make
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "baseRentPrice": 1}' http://localhost:8080/make
#features: 5
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "priceIncrease": 1}' http://localhost:8080/feature
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "priceIncrease": 1}' http://localhost:8080/feature
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "priceIncrease": 1}' http://localhost:8080/feature
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "priceIncrease": 1}' http://localhost:8080/feature
curl -i -X POST -H 'Content-Type: application/json' -d '{"name": "F", "priceIncrease": 1}' http://localhost:8080/feature
