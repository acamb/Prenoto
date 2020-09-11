package ac.sanbernardo.prenoto.traits

import ac.sanbernardo.prenoto.annotations.Uppercase
import groovy.transform.CompileStatic

import java.lang.reflect.Field
import java.lang.reflect.Method

@CompileStatic
trait UppercaseTrait {
    void setUppercaseFields(){
        Class clazz = this.getClass();
        List<Field> fields = new ArrayList<>();
        fields.addAll(clazz.getDeclaredFields())
        fields.forEach({ field ->
            if (field.getType() == String.class &&
                    field.getAnnotation(Uppercase.class) != null
            ) {
                Method getter
                try {
                    getter = clazz.getMethod("get" + field.getName().capitalize())
                }
                catch (NoSuchMethodException e) {
                    getter = clazz.getMethod("is" + field.getName().capitalize())
                }
                if (getter.invoke(this) != null) {
                    Method setter = clazz.getMethod("set" + field.getName().capitalize(), String)
                    setter.invoke(this, ((String) getter.invoke(this)).toUpperCase())
                }
            }
        })
    }
}
