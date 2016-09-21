package com.jiajie.design.utils.validator;

interface Validator<T> {
    boolean isValid(T t);

    ValidatorError[] getErrors();
}