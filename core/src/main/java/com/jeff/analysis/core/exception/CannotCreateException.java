package com.jeff.analysis.core.exception;

/**
 * @author Jeff
 * @describe
 * @date 2019/4/1.
 */
public final class CannotCreateException extends UnsupportedOperationException {

    public CannotCreateException(Class clz) {
        super("You can't instantiate "+clz.getName()+" !");
    }
}
