insert into role (id, name, description) values (1, 'admin', '系统管理员');
insert into permission(id, name, description, url, operation, `group`, `group_desc`) values (1, 'Adinm', 'Admin', '/**', 5, 'Admin', 'All Permission');
insert into role_permission_rel(id, role_id, permission_id) values (1, 1, 1);
insert into user_permission_rel (id, user_id, permission_id) values (1, 1, 1);