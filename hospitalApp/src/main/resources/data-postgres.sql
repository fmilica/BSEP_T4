insert into authority (name) values ('ROLE_ADMIN');
insert into authority (name) values ('ROLE_DOCTOR');

-- password = admin
insert into users (type, name, surname, email, password, is_active)
values ('admin', 'Ksenija', 'Prcic', 'admin@admin.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', true);

insert into users (type, name, surname, email, password, is_active)
values ('doctor', 'Doktor', 'doktor', 'doc@doc.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', true);

insert into user_authority (user_id, authority_id) values (1, 1);
insert into user_authority (user_id, authority_id) values (2, 2);
