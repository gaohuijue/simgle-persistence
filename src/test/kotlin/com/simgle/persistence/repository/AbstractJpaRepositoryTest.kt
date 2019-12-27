package com.simgle.persistence.repository

import com.simgle.core.SimgleApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.*
import java.util.*
import javax.transaction.Transactional

@SpringBootTest(classes = [SimgleApplication::class])
open class AbstractJpaRepositoryTest {

    @Autowired
    open var organizationRepository: OrganizationRepository? = null

    @Test
    @Transactional
    open fun testSaveOneGetOneUpdateOneDeleteOne() {
        val repository = organizationRepository!!
        var org: Organization? = Organization.build {
            id = "11111"
            name = "ABCDE"
            enable = 1
            operateTime = Date()
        }
        repository.save(org!!)
        org = repository.getOne(org.uuid!!)
        assertNotNull("保存失败", org)

        org!!.name = "EFGH"
        org.enable = 0
        repository.save(org)
        assertEquals("更新失败", "EFGH", repository.getOne(org.uuid!!)!!.name)

        repository.delete(org)
        assertNull("删除失败", repository.getOne(org.uuid!!))
    }
}