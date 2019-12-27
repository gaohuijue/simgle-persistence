package com.simgle.persistence.repository

import org.springframework.stereotype.Repository

@Repository
open class OrganizationRepository : AbstractJpaRepository<Organization, String>(Organization::class.java) {
}