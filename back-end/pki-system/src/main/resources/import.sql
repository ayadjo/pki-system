INSERT INTO public.users (id, common_name, country, given_name, mail, organization, organization_unit, password, surname) VALUES (1,'Admin Admin', 'Serbia', 'Admin', 'admin@gmail.com', 'PKI1', 'PKI-Beograd', '$2a$10$lhYgvOwC1Q.fxzQBkwVNI.xqwKaoQiY6Gum5fzeN9jsuYStzORNGi', 'Admin');
INSERT INTO public.users (id, common_name, country, given_name, mail, organization, organization_unit, password, surname) VALUES (2,'Nina Ninic', 'Serbia', 'Nina', 'nina@gmail.com', 'PKI2', 'PKI-Novi Sad', '$2a$10$lhYgvOwC1Q.fxzQBkwVNI.xqwKaoQiY6Gum5fzeN9jsuYStzORNGi', 'Nina');
INSERT INTO public.users (id, common_name, country, given_name, mail, organization, organization_unit, password, surname) VALUES (3,'Marko Markic', 'Serbia', 'Marko', 'marko@gmail.com', 'PKI3', 'PKI-Uzice', '$2a$10$lhYgvOwC1Q.fxzQBkwVNI.xqwKaoQiY6Gum5fzeN9jsuYStzORNGi', 'Marko');
INSERT INTO public.users (id, common_name, country, given_name, mail, organization, organization_unit, password, surname) VALUES (4,'Tasa Tasic', 'Serbia', 'Tasa', 'tasa@gmail.com', 'PKI4', 'PKI-Sabac', '$2a$10$lhYgvOwC1Q.fxzQBkwVNI.xqwKaoQiY6Gum5fzeN9jsuYStzORNGi', 'Tasa');
INSERT INTO public.users (id, common_name, country, given_name, mail, organization, organization_unit, password, surname) VALUES (5,'Andja Andjic', 'Serbia', 'Andja', 'andja@gmail.com', 'PKI5', 'PKI-Lazarevac', '$2a$10$lhYgvOwC1Q.fxzQBkwVNI.xqwKaoQiY6Gum5fzeN9jsuYStzORNGi', 'Andja');


INSERT INTO public.role (id, name) VALUES (1,'ADMIN');
INSERT INTO public.role (id, name) VALUES (2,'USER');


INSERT INTO user_role (user_id, role_id) VALUES (1,1);
INSERT INTO user_role (user_id, role_id) VALUES (2,2);
INSERT INTO user_role (user_id, role_id) VALUES (3,2);
INSERT INTO user_role (user_id, role_id) VALUES (4,2);
INSERT INTO user_role (user_id, role_id) VALUES (5,2);