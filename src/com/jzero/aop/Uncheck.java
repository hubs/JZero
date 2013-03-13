
package com.jzero.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 2012-11-13:应用于类名上,表示当前类不会检测是否已存在session值
 * wangujqw@gmail.com
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Uncheck {
}
