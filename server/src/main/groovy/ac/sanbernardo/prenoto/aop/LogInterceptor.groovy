package ac.sanbernardo.prenoto.aop

import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory

import javax.inject.Singleton

@Singleton
class LogInterceptor implements MethodInterceptor<Object, Object> {
    @Override
    Object intercept(MethodInvocationContext<Object, Object> context) {
        String args="{"
        context.getParameters().each { key,value ->
            args += "[ $key : ${value.value} ] "
        }
        args += "}"
        LoggerFactory.getLogger(context.executableMethod.getTargetMethod().declaringClass)
            .info("${context.executableMethod.name}, args: ${args}")
        return context.proceed();
    }
}
