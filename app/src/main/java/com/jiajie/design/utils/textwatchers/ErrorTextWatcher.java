package com.jiajie.design.utils.textwatchers;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * ErrorTextWatcher
 * Created by jiajie on 16/7/4.
 */
public abstract class ErrorTextWatcher implements TextWatcher {

    private TextInputLayout mTextInputLayout;
    private String errorMessage;

    public abstract boolean validate();

    public ErrorTextWatcher(@NonNull final TextInputLayout mTextInputLayout,
                            @NonNull final String errorMessage) {
        this.mTextInputLayout = mTextInputLayout;
        this.errorMessage = errorMessage;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public final boolean hasError() {
        return mTextInputLayout.getError() != null;
    }

    protected String getEditTextValue() {
        return mTextInputLayout.getEditText() != null
                ? mTextInputLayout.getEditText().getText().toString() : "";
    }

    protected void showError(final boolean error) {
        if (!error) {
            mTextInputLayout.setError(null);
            mTextInputLayout.setErrorEnabled(false);
        } else {
            if (!errorMessage.equals(mTextInputLayout.getError())) {
                // Stop the flickering that happens when setting the same error message multiple times
                mTextInputLayout.setError(errorMessage);
            }
            mTextInputLayout.requestFocus();
        }
    }

}
