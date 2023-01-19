-- table schema设计


CREATE TABLE `t_invite_code`
(
    `id`                    BIGINT(10)    NOT NULL COMMENT '流水号ID',
    `_invite_code`          VARCHAR(48)   NOT NULL COMMENT '邀请码',
    `_friends_default_type` TINYINT(3)    NULL     DEFAULT '1' COMMENT '预设好友模式 1: 所有, 2: 轮询',
    `_status`               TINYINT(3)    NULL     DEFAULT '1' COMMENT '状态：0 关闭, 1: 开启',
    `_note`                 VARCHAR(1000) NULL     DEFAULT NULL COMMENT '后台备注',
    `_create_time`          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `_creator`              BIGINT(10)  NULL COMMENT '创建者ID',
    `_update_time`          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `_updater`              BIGINT(10)  NULL COMMENT '修改者ID',
    PRIMARY KEY (`id`)
)
    COLLATE = 'utf8mb4_0900_ai_ci'
    ENGINE = InnoDB
    COMMENT ='邀请码';



CREATE TABLE `t_default_member`
(
    `id`              BIGINT(10)    NOT NULL COMMENT '流水号ID',
    `_default_type`   TINYINT(3)    NOT NULL DEFAULT '1' COMMENT '预设类型: 好友:1, 群:2',
    `_member_id`      BIGINT(10)    NULL COMMENT '会员ID',
    `_group_id`       BIGINT(10)    NULL COMMENT '群ID',
    `_welcome_text`   VARCHAR(1000) NULL COMMENT '欢迎词',
    `_type`           TINYINT(3)    NOT NULL DEFAULT '1' COMMENT '类型: 1:所有新注册用户, 2:使用邀请码注册用户',
    `_invite_code_id` BIGINT(10)    NULL COMMENT '邀请码ID',
    `_create_time`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `_creator`        BIGINT(10)  NULL COMMENT '创建者ID',
    `_update_time`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `_updater`        BIGINT(10)  NULL COMMENT '修改者ID',
    PRIMARY KEY (`id`)
)
    COLLATE = 'utf8mb4_0900_ai_ci'
    ENGINE = InnoDB
    COMMENT ='预设好友/群设置';

CREATE TABLE `t_group`
(
    `id`                  BIGINT(10)   NOT NULL AUTO_INCREMENT COMMENT '流水号ID',
    `_name`               VARCHAR(100) NOT NULL COMMENT '群名称(群信息)',
    `_manager_id`         BIGINT(10)  NOT NULL COMMENT '群主',
    `_member_count`       INT          NOT NULL DEFAULT '0' COMMENT '群人数',
    `_member_count_limit` INT          NOT NULL DEFAULT '200' COMMENT '群人数上限',
    `_group_image`        VARCHAR(100) NOT NULL COMMENT '群头像图片',
    `_mute_type`          TINYINT(3)   NOT NULL DEFAULT '0' COMMENT '禁言 0:正常 1:禁言普通成员 2:禁言整个群',
    `_enter_auth_type`    TINYINT(3)   NOT NULL DEFAULT '1' COMMENT '入群验证类型 1:无需验证 2:管理员验证 3:不允许入群验证',
    `_invite_permission`  TINYINT(3)   NOT NULL DEFAULT '1' COMMENT '邀请权限(谁可以邀请他人入群) 1:管理员 2:所有人',
    `_invite_auth`        TINYINT(3)   NOT NULL DEFAULT '1' COMMENT '被邀请人身份验证 0:不需要同意 1:需要同意',
    `_modify_permission`  TINYINT(3)   NOT NULL DEFAULT '1' COMMENT '群资料修改权限 1:管理员 2:所有人',
    `_bulletin_title`     VARCHAR(100) NULL COMMENT '群公告标题',
    `_bulletin_content`   VARCHAR(500) NULL COMMENT '群公告内容',
    `_create_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `_creator`            BIGINT(10) NULL COMMENT '创建者ID',
    `_update_time`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `_updater`            BIGINT(10) NULL COMMENT '修改者ID',
    PRIMARY KEY (`id`) USING BTREE
)
    COMMENT ='群列表'
    COLLATE = 'utf8mb4_0900_ai_ci'
    ENGINE = InnoDB
;


CREATE TABLE `t_config`
(
    `_default_member` TINYINT(3) NOT NULL COMMENT '预设好友/群设置 1: 全部, 2:邀请码'
) COMMENT ='全局设置'
    COLLATE = 'utf8mb4_0900_ai_ci'
    ENGINE = InnoDB;