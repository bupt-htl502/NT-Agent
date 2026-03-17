-- 创建班级表
CREATE TABLE IF NOT EXISTS t_class (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(255) NOT NULL COMMENT '班级名称',
    class_code VARCHAR(6) NOT NULL UNIQUE COMMENT '班级码',
    teacher_no VARCHAR(255) NOT NULL COMMENT '教师学号',
    teacher_name VARCHAR(255) NOT NULL COMMENT '教师姓名',
    isdeleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 修改学生表，添加班级ID字段
ALTER TABLE t_student ADD COLUMN class_id BIGINT COMMENT '班级ID';

-- 添加索引
CREATE INDEX idx_class_code ON t_class(class_code);
CREATE INDEX idx_teacher_no ON t_class(teacher_no);
CREATE INDEX idx_class_id ON t_student(class_id);
