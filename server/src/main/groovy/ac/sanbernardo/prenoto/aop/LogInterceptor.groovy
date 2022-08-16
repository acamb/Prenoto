package ac.sanbernardo.prenoto.aop

import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import java.security.Principal

@Singleton
class LogInterceptor implements MethodInterceptor<Object, Object> {
    @Override
    Object intercept(MethodInvocationContext<Object, Object> context) {
        String args="{"
        context.getParameters().each { key,value ->
            args += "[ $key : ${getDescrizione(value.value)} ] "
        }
        args += "}"
        LoggerFactory.getLogger(context.executableMethod.getTargetMethod().declaringClass)
            .info("${context.executableMethod.name}, args: ${args}")
        def returnValue =  context.proceed();
        LoggerFactory.getLogger(context.executableMethod.getTargetMethod().declaringClass)
                .info("${context.executableMethod.name}, return: ${returnValue}")
        return returnValue
    }

    private static String getDescrizione(Object o){
        if(o == null){
            return "null"
        }
        if(o instanceof Principal){
            return o.name
        }
        else{
            return o.toString()
        }
    }
}
