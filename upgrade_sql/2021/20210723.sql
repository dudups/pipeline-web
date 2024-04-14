-- 支持代码库重命名

-- 备份旧代码库表，以防回滚
-- 备份旧代码库表，以防回滚
-- 备份旧代码库表，以防回滚
CREATE TABLE `repo_bak` LIKE `repo`;
INSERT INTO `repo_bak` SELECT * FROM `repo`;

-- 清理假删数据
DELETE FROM `repo` WHERE deleted = 1;
-- 删除假删标识
ALTER TABLE `repo` DROP COLUMN `deleted`;
-- 修改 repo_name 列为 repo_key（ezcode 代码库 ID）
ALTER TABLE `repo` CHANGE `repo_name` `repo_key` varchar(255) NOT NULL DEFAULT '' COMMENT '代码库标识';

-- curl 刷新代码库ID脚本
curl -X POST "http://127.0.0.1:8201/internal/migrates/history_data?deleteHistory=false&migrateType=refresh_repo_key"


-- 可暂时不删除以下分表，以防回滚
-- 可暂时不删除以下分表，以防回滚
-- 可暂时不删除以下分表，以防回滚

-- 删除分支表
DROP TABLE IF EXISTS `branch`;
DROP TABLE IF EXISTS `branch_1`;
DROP TABLE IF EXISTS `branch_2`;
DROP TABLE IF EXISTS `branch_3`;
DROP TABLE IF EXISTS `branch_4`;
DROP TABLE IF EXISTS `branch_5`;
DROP TABLE IF EXISTS `branch_6`;
DROP TABLE IF EXISTS `branch_7`;
DROP TABLE IF EXISTS `branch_8`;
DROP TABLE IF EXISTS `branch_9`;
DROP TABLE IF EXISTS `branch_10`;

-- 删除分支最新提交表
DROP TABLE IF EXISTS `head_commit`;
DROP TABLE IF EXISTS `head_commit_1`;
DROP TABLE IF EXISTS `head_commit_2`;
DROP TABLE IF EXISTS `head_commit_3`;
DROP TABLE IF EXISTS `head_commit_4`;
DROP TABLE IF EXISTS `head_commit_5`;
DROP TABLE IF EXISTS `head_commit_6`;
DROP TABLE IF EXISTS `head_commit_7`;
DROP TABLE IF EXISTS `head_commit_8`;
DROP TABLE IF EXISTS `head_commit_9`;
DROP TABLE IF EXISTS `head_commit_10`;

-- 删除代码库提交表
DROP TABLE IF EXISTS `repo_commit`;
DROP TABLE IF EXISTS `repo_commit_1`;
DROP TABLE IF EXISTS `repo_commit_2`;
DROP TABLE IF EXISTS `repo_commit_3`;
DROP TABLE IF EXISTS `repo_commit_4`;
DROP TABLE IF EXISTS `repo_commit_5`;
DROP TABLE IF EXISTS `repo_commit_6`;
DROP TABLE IF EXISTS `repo_commit_7`;
DROP TABLE IF EXISTS `repo_commit_8`;
DROP TABLE IF EXISTS `repo_commit_9`;
DROP TABLE IF EXISTS `repo_commit_10`;
