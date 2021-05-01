insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('adminroot', 'RootCA', 'rootCA@example.com', 'adminroot', null, null, false, '2021-03-13 11:49:51', '2026-03-13 11:49:51');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('admin', 'localhost', 'admin@example.com', 'adminroot', null, null, false, '2021-03-13 12:01:28', '2023-03-13 12:01:28');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('hospital', 'localhost', 'hospital@example.com', 'adminroot', null, null, false, '2021-03-13 12:17:49', '2023-03-13 12:17:49');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('keycloak', 'localhost', 'micika4ever@gmail.com', 'adminroot', null, null, false, '2021-04-28 03:49:17', '2023-04-28 03:49:17');