# --- Sample dataset

# --- !Ups

insert into category (id,name) values ( 1,'CD' );
insert into category (id,name) values ( 2,'Vinyl' );
insert into category (id,name) values ( 3,'Accessorie' );

insert into product (id,name,description,stock,price) values ( 15,'AM (2013)','Artic Monkeys Album',15,14.00);
insert into product (id,name,description,stock,price) values ( 16,'I (2015)','Artic Sea Album',33,16.00);
insert into product (id,name,description,stock,price) values ( 19,'Only By The Night (2008)','Kings Of Leon Album',14,12.00);
insert into product (id,name,description,stock,price) values ( 26,'Caracal (2015)','Disclosure Album',15,14.00);
insert into product (id,name,description,stock,price) values ( 3,'Abbey Road (1969)','The Beatles Album',7,29.50);
insert into product (id,name,description,stock,price) values ( 7,'Rumours (1977)','Fleetwood Mac Album',5,18.50);
insert into product (id,name,description,stock,price) values ( 17,'Legend (1984)','Bob Marley Album',12,20.00);
insert into product (id,name,description,stock,price) values ( 28,'Sony BTN200 Headphones','Sony Bluetooth Headphones',25,38.99);
insert into product (id,name,description,stock,price) values ( 29,'Beats By Dre 2.0 Headphones','Dr. Dre Headphones',27,208.99);
insert into product (id,name,description,stock,price) values ( 33,'Apple Earpods with Remote and Mic (White)','Apple Earphones',34,37.99);

insert into category_product (category_id,product_id) values (1,15);
insert into category_product (category_id,product_id) values (1,16);
insert into category_product (category_id,product_id) values (1,19);
insert into category_product (category_id,product_id) values (1,26);
insert into category_product (category_id,product_id) values (2,3);
insert into category_product (category_id,product_id) values (2,7);
insert into category_product (category_id,product_id) values (2,17);
insert into category_product (category_id,product_id) values (3,28);
insert into category_product (category_id,product_id) values (3,29);
insert into category_product (category_id,product_id) values (3,33);

insert into user (email,name,password,userType) values ( 'admin@products.com', 'Adam Admin', 'password', 'admin' );
insert into user (email,name,password,userType) values ( 'customer@products.com', 'Carl Customer', 'password', 'customer' );

