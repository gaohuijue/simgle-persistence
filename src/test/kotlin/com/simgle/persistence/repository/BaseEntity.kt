package com.simgle.persistence.repository

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity : Serializable {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "UUID", unique = true, nullable = false, columnDefinition = "varchar(36) COMMENT '物理主键自动生成'")
    open var uuid: String? = null
    @Column(name = "OPERATOR", columnDefinition = "varchar(36) COMMENT '操作人'")
    open var operator: String? = null
    @Column(name = "OPERATE_TIME", columnDefinition = "datetime COMMENT '操作时间'")
    open var operateTime: Date? = null
    @Version
    @Column(name = "VERSION", columnDefinition = "int(8) COMMENT '乐观锁版本号'")
    open var version: Int? = null

    open fun verification() {
    }
}