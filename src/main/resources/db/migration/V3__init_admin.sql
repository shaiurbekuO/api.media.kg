CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INSERT INTO profile(id,name,username,password,status, visible,created_date)
VALUES (1, 'Admin','shaiurbekuuluomurbek@gmail.com','$2a$10$I9R0jivo2qfrPwWQ0AuUrusVpOnqXnrIgPbui1.UIPaioMfKvaxiy', 'ACTIVE', true, now());

SELECT setval('profile_id_seq', max(id)) FROM profile;

INSERT INTO profile_role(profile_id, roles, created_date)
VALUES(1, 'ROLE_USER', now()),
    (1, 'ROLE_ADMIN', now());