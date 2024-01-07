-- generated create script

create sequence car_seq start with 1 increment by 50;
create sequence feature_seq start with 1 increment by 50;
create sequence make_seq start with 1 increment by 50;
create sequence reservation_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table car (id bigint not null, make_id bigint not null, registration_plate varchar(255), primary key (id), constraint UKrx71k3sjwre0g4g0sg0f7svdi unique (registration_plate));
create table car_feature (car_id bigint not null, feature_id bigint not null);
create table car_reservations (car_id bigint not null, reservations_id bigint not null unique);
create table feature (id bigint not null, price_increase bigint not null, name varchar(255), primary key (id));
create table make (base_rent_price bigint not null, id bigint not null, name varchar(255), primary key (id));
create table make_cars (cars_id bigint not null unique, make_id bigint not null);
create table reservation (car_reserved_id bigint not null, id bigint not null, reservation_maker_id bigint not null, time_end bigint not null, time_start bigint not null, primary key (id));
create table users (id bigint not null, email varchar(255), name varchar(255), phone_number varchar(255), primary key (id));
create table users_reservations (reservations_id bigint not null unique, user_id bigint not null);
alter table if exists car add constraint FK8dnhr9fn281npgs59xx0o1qfj foreign key (make_id) references make;
alter table if exists car_feature add constraint FKgqgv3iyd1518909jkijos3tg3 foreign key (feature_id) references feature;
alter table if exists car_feature add constraint FKd86e0b4v70sx9onvqpf3hc81h foreign key (car_id) references car;
alter table if exists car_reservations add constraint FK8jdrj3hny146lc15r7dvph8hw foreign key (reservations_id) references reservation;
alter table if exists car_reservations add constraint FKbxmwhc4jgi8mtpumajexvapl6 foreign key (car_id) references car;
alter table if exists make_cars add constraint FK8w4epeg4s2m9k4mlfy7l7ku7k foreign key (cars_id) references car;
alter table if exists make_cars add constraint FKbx555hhndjs5f7woh0stm1ufs foreign key (make_id) references make;
alter table if exists reservation add constraint FKpf9ieymthvtd72953edsgiivf foreign key (car_reserved_id) references car;
alter table if exists reservation add constraint FK9ev1o5ls1aqigfmxdmmmw9syv foreign key (reservation_maker_id) references users;
alter table if exists users_reservations add constraint FKqavms38v91a10rddn5cj2fkqd foreign key (reservations_id) references reservation;
alter table if exists users_reservations add constraint FKbjbk2fls1rddtmi8wmswquatu foreign key (user_id) references users;

-- insertion of some data

insert into make (base_rent_price, id, name)
values (3500, 1, 'Toyota Corolla'),
       (5000, 2, 'Ford Focus'),
       (4000, 3, 'Škoda Octavia');

insert into feature (id, price_increase, name)
values (1, 500, 'GPS navigation'),
       (2, 900, 'Heated seats'),
       (3, 350, 'Backup camera'),
       (4, 400, 'Blind spot monitor'),
       (5, 1500, 'Sunroof');

insert into car (id, make_id, registration_plate)
values (1, 3, '1AB 5642'),
       (2, 1, '9AD B241'),
       (3, 1, '5S9 6514'),
       (4, 2, '1UU 4502'),
       (5, 3, '7Z6 6348');

insert into users (id, email, name, phone_number)
values (1, 'jane.doe@email.com', 'Jane Doe', null),
       (2, 'john.smith@email.net', 'John Smith', '776743168'),
       (3, 'joe.jones@mail.cz', 'Joe Jones', '682364698');

insert into reservation (car_reserved_id, id, reservation_maker_id, time_start, time_end)
values (1, 1, 1, 1688421600, 1688680800),
       (2, 2, 3, 1688508000, 1688594400),
       (1, 3, 2, 1688767200, 1689199200);

insert into car_feature (car_id, feature_id)
values (1, 3),
       (1, 4),
       (2, 1),
       (2, 2),
       (2, 4),
       (3, 5),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (4, 5);

insert into car_reservations (car_id, reservations_id)
values (1,1),
       (2,2),
       (1,3);

insert into make_cars (cars_id, make_id)
values (1,3),
       (2,1),
       (3,1),
       (4,2),
       (5,3);

insert into users_reservations (reservations_id, user_id)
values (1, 1),
       (2, 3),
       (3, 2);