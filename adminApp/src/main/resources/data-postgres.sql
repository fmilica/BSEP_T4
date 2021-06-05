insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('adminroot', 'RootCA', 'rootCA@example.com', 'adminroot', null, null, false, '2021-03-13 11:49:51', '2026-03-13 11:49:51');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('admin', 'localhost', 'admin@example.com', 'adminroot', null, null, false, '2021-03-13 12:01:28', '2023-03-13 12:01:28');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('hospital', 'localhost', 'hospital@example.com', 'adminroot', null, null, false, '2021-03-13 12:17:49', '2023-03-13 12:17:49');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('keycloak', 'localhost', 'micika4ever@gmail.com', 'adminroot', null, null, false, '2021-04-28 03:49:17', '2023-04-28 03:49:17');
insert into certificate_data (alias, common_name, email, parent_alias, revocation_date, revocation_reason, revoked, valid_from, valid_to)
    values ('5-medicaldevice1-rootca', 'MedicalDevice1', 'evaj.evadesign@gmail.com', 'adminroot', null, null, false, '2021-05-03 11:19:04', '2022-05-03 11:19:04');
-- bolnice
insert into hospital (email, med_dev_num) values ('novosadska@bolnica.com', 1);
insert into hospital (email, med_dev_num) values ('beogradska@bolnica.com', 0);
-- simulatori
insert into simulator (path) values ('C:\\Users\\evaje\\Downloads\\Keycloak\\keycloak-12.0.4\\standalone\\log');
insert into simulator (path) values ('C:\\Users\\evaje\\OneDrive\\Desktop\\Skola\\8_Semestar\\Bezbednost u sistemima elektronskog poslovanja\\projekat\\BSEP_T4\\adminApp\\logs');
-- bolnica-simulator
insert into simulator_hospital (hospital_id, simulator_id) values (1, 1);
