package com.nickteck.cus_prawnandcrab.animation_file;

/**
 * Created by admin on 11/30/2017.
 */

import android.support.annotation.NonNull;

public final class Preconditions {

    @NonNull
    public static <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    // Suppress default constructor for noninstantiability
    private Preconditions() {
        throw new AssertionError();
    }
}