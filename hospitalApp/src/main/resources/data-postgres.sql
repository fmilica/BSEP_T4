insert into authority (name) values ('ROLE_ADMIN');
insert into authority (name) values ('ROLE_DOCTOR');

-- password = admin
insert into users (type, name, surname, email, password, is_active, account_non_locked, failed_attempts)
values ('admin', 'Ksenija', 'Prcic', 'admin@admin.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', true, true, 0);

insert into users (type, name, surname, email, password, is_active, account_non_locked, failed_attempts)
values ('doctor', 'Doktor', 'doktor', 'doc@doc.com', '$2y$12$.j5Wzzl4knQZMfoJipXFEu6LA4mC6nyMYfcgUNS2tF9C42C537Rx.', true, true, 0);

insert into user_authority (user_id, authority_id) values (1, 1);
insert into user_authority (user_id, authority_id) values (2, 2);
