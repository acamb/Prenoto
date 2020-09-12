package ac.sanbernardo.prenoto.aop

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type

import java.lang.annotation.Documented
import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
@Around
@Type(LogInterceptor.class)
public @interface Logged {

}
