package com.benmohammad.rxsmoke;

import androidx.core.widget.TextViewCompat;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AppConfig {

    public static Builder builder() {
        return new AutoValue_AppConfig.Builder();
    }

    public abstract String clientId();
    public abstract String accessKey();
    public abstract String clientSecretId();
    public abstract String redirectUri();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract AppConfig build();
        public abstract Builder clientId(String s);
        public abstract Builder accessKey(String s);
        public abstract Builder clientSecretId(String s);
        public abstract Builder redirectUri(String a);
    }
}
