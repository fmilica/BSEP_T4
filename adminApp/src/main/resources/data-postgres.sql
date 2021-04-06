insert into authority (name) values ('ROLE_SUPER_ADMIN');

-- password = admin
insert into users (name, surname, email, password, is_active)
values ('Ksenija', 'Prcic', 'admin@admin.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', true);

insert into user_authority (user_id, authority_id) values (1, 1);
