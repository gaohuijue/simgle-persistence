package com.simgle.persistence.datasource

import com.simgle.core.Constant
import com.simgle.core.tool.ThreadBinds
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DataSourceDecideInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (HandlerMethod::class.java.isAssignableFrom(handler::class.java)) {
            val anno = (handler as HandlerMethod).getMethodAnnotation(UseDataSource::class.java)
            if (anno != null) {
                ThreadBinds.put(Constant.DATA_SOURCE_KEY, anno.value)
            }
        }
        return true
    }
}