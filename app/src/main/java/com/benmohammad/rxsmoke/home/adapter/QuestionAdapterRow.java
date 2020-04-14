package com.benmohammad.rxsmoke.home.adapter;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.benmohammad.rxsmoke.data.model.Question;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class QuestionAdapterRow implements Parcelable {

    private static final int TYPE_QUESTION = 0x01;
    private static final int TYPE_LOAD_MORE = 0x02;
    private static final int TYPE_LOADING = 0x03;
    private static final int TYPE_ERROR = 0x04;

    public abstract Builder toBuilder();

    public boolean isTypeQuestion() {
        return type() == TYPE_QUESTION;
    }

    public boolean isTypeLoadMore() {
        return type() == TYPE_LOAD_MORE;
    }

    public boolean isTypeLoading() {
        return type() == TYPE_LOADING;
    }

    public boolean isTypeError() {
        return type() == TYPE_ERROR;
    }

    public static Builder builder() {
        return new AutoValue_QuestionAdapterRow.Builder();
    }

    public static QuestionAdapterRow ofQuestion(Question question) {
        return builder().type(TYPE_QUESTION).imageUrl(question.owner().image()).question(question)
                .name(question.owner().name()).timestamp(question.updatedAt())
                .title(question.title()).votes(question.score()).build();
    }

    public static QuestionAdapterRow ofLoading() {
        return builder().type(TYPE_LOADING).build();
    }

    public static QuestionAdapterRow ofLoadMore() {
        return builder().type(TYPE_LOAD_MORE).build();
    }

    public static QuestionAdapterRow ofError() {
        return builder().type(TYPE_ERROR).build();
    }

    public int compare(QuestionAdapterRow r2) {
        int comparedValue = -1;
        if(this.isTypeQuestion() && r2.isTypeLoadMore()) {
            return -1;
        } else if (this.isTypeLoadMore() && r2.isTypeQuestion()) {
            return 1;
        } else if (this.isTypeQuestion() && r2.isTypeLoading()) {
            return -1;
        } else if (this.isTypeLoading() && r2.isTypeQuestion()) {
            return 1;
        } else if (this.isTypeLoadMore() && r2.isTypeLoadMore()) {
            return 0;
        } else if (this.isTypeLoading() && r2.isTypeLoading()) {
            return 0;
        } else if (this.isTypeError() && r2.isTypeError()) {
            return 0;
        }
        return comparedValue;
    }


    public boolean areContentsTheSame(QuestionAdapterRow newItem) {
        if(this.isTypeQuestion() && newItem.isTypeQuestion()) {
            return this.question().equals(newItem.question());
        } else if (isTypeLoadMore() && newItem.isTypeLoadMore()) {
            return this.equals(newItem);
        } else if (isTypeLoading() && newItem.isTypeLoading()) {
            return this.equals(newItem);
        } else if (isTypeError() && newItem.isTypeError()) {
            return this.equals(newItem);
        } return false;
    }

    public boolean areItemsTheSame(QuestionAdapterRow newItem) {
        if(isTypeQuestion() && newItem.isTypeQuestion()) {
            return this.question().id().longValue() == newItem.question().id().longValue();
        } else if (isTypeLoadMore() && newItem.isTypeLoadMore()) {
            return true;
        } else if (isTypeLoading() && newItem.isTypeLoading()) {
            return true;
        } else return isTypeError() && newItem.isTypeError();
    }


    @Nullable
    public abstract Question question();

    @Nullable
    public abstract String title();

    @Nullable
    public abstract Long timestamp();

    @Nullable
    public abstract String name();

    @Nullable
    public abstract String imageUrl();

    @Nullable
    public abstract Integer votes();

    public abstract int type();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract QuestionAdapterRow build();

        public abstract Builder type(int t);

        public abstract Builder question(Question t);

        public abstract Builder title(String t);

        public abstract Builder timestamp(Long t);

        public abstract Builder name(String t);

        public abstract Builder imageUrl(String t);

        public abstract Builder votes(Integer t);
    }
}
