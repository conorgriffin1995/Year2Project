# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table basket (
  id                        bigint not null,
  customer_email            varchar(255),
  constraint uq_basket_customer_email unique (customer_email),
  constraint pk_basket primary key (id))
;

create table category (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table order_item (
  id                        bigint not null,
  order_id                  bigint,
  basket_id                 bigint,
  product_id                bigint,
  stock                     integer,
  price                     double,
  constraint pk_order_item primary key (id))
;

create table product (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  stock                     integer,
  price                     double,
  constraint pk_product primary key (id))
;

create table shop_order (
  id                        bigint not null,
  order_date                timestamp,
  customer_email            varchar(255),
  user_email                varchar(255),
  constraint pk_shop_order primary key (id))
;

create table user (
  usertype                  varchar(31) not null,
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  street1                   varchar(255),
  street2                   varchar(255),
  town                      varchar(255),
  post_code                 varchar(255),
  credit_card               varchar(255),
  ex_month                  varchar(255),
  ex_year                   varchar(255),
  constraint pk_user primary key (email))
;


create table category_product (
  category_id                    bigint not null,
  product_id                     bigint not null,
  constraint pk_category_product primary key (category_id, product_id))
;
create sequence basket_seq;

create sequence category_seq;

create sequence order_item_seq;

create sequence product_seq;

create sequence shop_order_seq;

create sequence user_seq;

alter table basket add constraint fk_basket_customer_1 foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_basket_customer_1 on basket (customer_email);
alter table order_item add constraint fk_order_item_order_2 foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
create index ix_order_item_order_2 on order_item (order_id);
alter table order_item add constraint fk_order_item_basket_3 foreign key (basket_id) references basket (id) on delete restrict on update restrict;
create index ix_order_item_basket_3 on order_item (basket_id);
alter table order_item add constraint fk_order_item_product_4 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_order_item_product_4 on order_item (product_id);
alter table shop_order add constraint fk_shop_order_customer_5 foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_customer_5 on shop_order (customer_email);
alter table shop_order add constraint fk_shop_order_user_6 foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_user_6 on shop_order (user_email);



alter table category_product add constraint fk_category_product_category_01 foreign key (category_id) references category (id) on delete restrict on update restrict;

alter table category_product add constraint fk_category_product_product_02 foreign key (product_id) references product (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists basket;

drop table if exists category;

drop table if exists category_product;

drop table if exists order_item;

drop table if exists product;

drop table if exists shop_order;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists basket_seq;

drop sequence if exists category_seq;

drop sequence if exists order_item_seq;

drop sequence if exists product_seq;

drop sequence if exists shop_order_seq;

drop sequence if exists user_seq;

