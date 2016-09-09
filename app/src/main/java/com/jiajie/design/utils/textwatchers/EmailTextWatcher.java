package com.jiajie.design.utils.textwatchers;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;

import com.jiajie.design.R;
import com.jiajie.design.utils.validator.EmailAddressValidator;

public class EmailTextWatcher extends ErrorTextWatcher {

    private final EmailAddressValidator mValidator = new EmailAddressValidator();
    private boolean mValidated = false;

    public EmailTextWatcher(@NonNull final TextInputLayout textInputLayout) {
        this(textInputLayout, R.string.error_invalid_email);
    }

    public EmailTextWatcher(@NonNull final TextInputLayout textInputLayout,
                            @StringRes final int errorMessage) {
        super(textInputLayout, textInputLayout.getContext().getString(errorMessage));
    }

    @Override
    public void onTextChanged(final CharSequence text,
                              final int start, final int before, final int count) {
        // The hasError is needed here
        // because I can restore the instance state of the input layout without updating this flag in the watcher
        if (mValidated || hasError()) {
            validate();
        }
    }

    @Override
    public boolean validate() {
        showError(!mValidator.isValid(getEditTextValue()));
        mValidated = true;
        return !hasError();
    }
}