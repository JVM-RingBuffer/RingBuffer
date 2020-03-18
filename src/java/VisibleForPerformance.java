package eu.menzani.ringbuffer.java;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated method or constructor of a non-private nested class should be private,
 * but is package-private for performance reasons.
 */
@Retention(RetentionPolicy.SOURCE)
@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface VisibleForPerformance {
}
