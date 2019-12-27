package com.simgle.persistence.repository

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * 简单实现组织机构和权限，要组织机构是为了用树组件。
 */
@Entity
@Table(name = "SYS_PM_ORGANIZATION")
open class Organization : BaseEntity() {
    @Column(name = "ID", columnDefinition = "varchar(32) COMMENT 'id'")
    open var id: String? = null
    @Column(name = "NAME", columnDefinition = "varchar(32) COMMENT '显示名称'")
    open var name: String? = null
    @Column(name = "ENABLE", columnDefinition = "int COMMENT '启用'")
    open var enable: Int? = null

    companion object {
        fun build(block: Organization.() -> Unit): Organization = Organization().apply(block)
    }
}