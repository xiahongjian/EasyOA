create view ACT_ID_USER as
select
    cast(id as char) ID_,
    null REV_,
    null FIRST_,
    null LAST_,
    name DISPLAY_NAME_,
    email EMAIL_,
    null PWD_,
    '' PICTURE_ID_,
    null TENAT_ID_
from user;

create view ACT_ID_GROUP as
select
    CAST(id as char) ID_,
    NULL REV_,
    name NAME_,
    NULL TYPE_
from role;

create view ACT_ID_MEMBERSHIP as
SELECT
    CAST(user_id as char) USER_ID_,
    CAST(role_id as char) ROLE_ID_
FROM user_role_rel;