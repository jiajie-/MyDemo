package com.jiajie.design.utils.textwatchers;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;

import com.jiajie.design.R;

/**
 * MinimumLengthTextWatcher
 * Created by jiajie on 16/7/4.
 */
public class PasswordTextWatcher extends ErrorTextWatcher {

    private final int mMinLength;
    private boolean mReachedMinLength = false;

    public PasswordTextWatcher(@NonNull final TextInputLayout textInputLayout, final int minLength) {
        this(textInputLayout, minLength, R.string.error_invalid_password);
    }

    public PasswordTextWatcher(@NonNull final TextInputLayout textInputLayout,
                               final int minLength,
                               @StringRes final int errorMessage) {
        super(textInputLayout,
                String.format(textInputLayout.getContext().getString(errorMessage), minLength));
        this.mMinLength = minLength;
    }

    @Override
    public void onTextChanged(final CharSequence text, final int start, final int before, final int count) {
        if (mReachedMinLength) {
            validate();
        }
        if (text.length() >= mMinLength) {
            mReachedMinLength = true;
        }
    }

    @Override
    public boolean validate() {
        mReachedMinLength = true; // This may not be true but now we want to force the error to be shown
        showError(getEditTextValue().length() < mMinLength);
        return !hasError();
    }
}
