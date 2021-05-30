SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE ACT_ID_GROUP;
CREATE OR REPLACE VIEW ACT_ID_GROUP as
select
    r.key as ID_,
    null as REV_,
    r.key as NAME_,
    null as TYPE_
from `role` r;

DROP TABLE ACT_ID_MEMBERSHIP;
CREATE OR REPLACE VIEW ACT_ID_MEMBERSHIP AS
SELECT
    rel.user_id AS USER_ID_,
    r.key AS GROUP_ID_
FROM `user_role_rel` rel, `role` r where rel.role_id = r.id;

DROP TABLE ACT_ID_USER;
CREATE OR REPLACE VIEW ACT_ID_USER AS
SELECT
    CAST(u.id as CHAR) AS ID_,
    NULL AS REV_,
    NULL AS FIRST_,
    NULL AS LAST_,
    u.name AS DISPLAY_NAME_,
    u.email AS EMIAL_,
    NULL AS PWD_,
    NULL AS PICTURE_ID_,
    NULL AS TENANT_ID_
FROM `user` u;

SET FOREIGN_KEY_CHECKS = 1;
