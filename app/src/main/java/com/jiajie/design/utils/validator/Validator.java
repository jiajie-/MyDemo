package com.jiajie.design.utils.validator;

public interface Validator<T> {
    boolean isValid(T t);

    ValidatorError[] getErrors();
}