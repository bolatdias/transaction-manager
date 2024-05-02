create table currency
(
    close_exchange          decimal(38, 2) null,
    previous_close_exchange decimal(38, 2) null,
    exchange_date           datetime(6)    null,
    id                      bigint auto_increment
        primary key,
    symbol                  varchar(255)   null
);


create table limits
(
    limit_value  decimal(38, 2)              null,
    created_date datetime(6)                 null,
    id           bigint auto_increment
        primary key,
    type         enum ('PRODUCT', 'SERVICE') null
);

create table transaction
(
    sum          decimal(38, 2) null,
    account_from bigint         null,
    account_to   bigint         null,
    currency_id  bigint         not null,
    datetime     datetime(6)    null,
    id           bigint auto_increment
        primary key,
    limit_id     bigint         not null,


    foreign key (limit_id) references limits (id),
    foreign key (currency_id) references currency (id)


);

