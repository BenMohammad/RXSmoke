package com.benmohammad.rxsmoke.home.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.benmohammad.rxsmoke.R;
import com.benmohammad.rxsmoke.rxevent.AppEvents;
import com.benmohammad.rxsmoke.rxevent.RxEventBus;
import com.benmohammad.rxsmoke.utils.Utils;
import com.benmohammad.rxsmoke.view.TagView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionAdapterVH> {

    private static final int VIEW_TYPE_QUESTION = 0x01;
    private static final int VIEW_TYPE_LOADMORE = 0x02;
    private static final int VIEW_TYPE_LOADING = 0x03;
    private static final int VIEW_TYPE_ERROR = 0x04;

    RxEventBus eventBus;
    private ArrayList<QuestionAdapterVH> viewHolders = new ArrayList<>();
    private QuestionsAdapterRowDataSet dataSet;

    @NonNull
    @Override
    public QuestionAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionAdapterVH holder = null;
        switch (viewType) {
            case VIEW_TYPE_QUESTION:
                holder = QuestionAdapterVH.ofQuestion(parent);
                break;
            case VIEW_TYPE_LOADMORE:
                holder = QuestionAdapterVH.ofLoadMore(parent);
                break;
            case VIEW_TYPE_LOADING:
                holder = QuestionAdapterVH.ofLoading(parent);
                break;
            case VIEW_TYPE_ERROR:
                holder = QuestionAdapterVH.ofError(parent);
                break;
            default:
                break;
        }
        if(holder != null) {
            viewHolders.add(holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapterVH holder, int position) {
        holder.bind(dataSet.get(position), eventBus);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        QuestionAdapterRow row = dataSet.get(position);
        if(row.isTypeQuestion()) {
            return VIEW_TYPE_QUESTION;
        } else if(row.isTypeLoadMore()) {
            return VIEW_TYPE_LOADMORE;
        } else if(row.isTypeLoading()) {
            return VIEW_TYPE_LOADING;
        } else if(row.isTypeError()) {
            return VIEW_TYPE_ERROR;
        }
        return RecyclerView.NO_POSITION;
    }

    public void setData(QuestionsAdapterRowDataSet adapterRowDataSet) {
        this.dataSet = adapterRowDataSet;
    }

    public void setEventBus(RxEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handleDestroy() {
        dataSet.clearDataSet();
        for(QuestionAdapterVH holder : viewHolders) {
            holder.destroy();
        }
        viewHolders.clear();
    }

    abstract static class QuestionAdapterVH extends RecyclerView.ViewHolder {

        QuestionAdapterVH(View itemView) {
            super(itemView);
        }

        static QuestionAdapterVH ofQuestion(ViewGroup parent) {
            return QuestionVH.create(parent);
        }

        static QuestionAdapterVH ofLoadMore(ViewGroup parent) {
            return LoadMoreVH.create(parent);
        }

        static QuestionAdapterVH ofError(ViewGroup parent) {
            return ErrorVH.create(parent);
        }

        static QuestionAdapterVH ofLoading(ViewGroup parent) {
            return LoadingVH.create(parent);
        }

        abstract void bind(QuestionAdapterRow row, RxEventBus eventBus);

        abstract void destroy();

        public static class QuestionVH extends QuestionAdapterVH {

            @BindView(R.id.tv_owner_name)
            TextView ownerName;

            @BindView(R.id.tv_question_posted_at)
            TextView timestamp;

            @BindView(R.id.tv_question_title)
            TextView title;

            @BindView(R.id.iv_owner_profile)
            ImageView avatar;

            @BindView(R.id.flow_layout_tags)
            ViewGroup tagsLayout;

            @BindView(R.id.tv_view_count)
            TextView viewCount;

            @BindView(R.id.tv_answer_count)
            TextView answerCount;

            @BindView(R.id.tv_vote_count)
            TextView votesCount;



            QuestionVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public static QuestionVH create(ViewGroup parent) {
                View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_question, parent, false);
                return new QuestionVH(rootView);
            }


            @Override
            void bind(QuestionAdapterRow row, RxEventBus eventBus) {
                if(tagsLayout != null && tagsLayout.getChildCount() != 0) {
                    tagsLayout.removeAllViews();
                }

                Utils.loadRoundImage(itemView.getContext(), row.imageUrl(), avatar);
                ownerName.setText(row.name());
                title.setText(row.title());
                timestamp.setText(Utils.timeStampRelativeToCurrentTime(row.timestamp() * 1000));
                if(row.question().tags() != null && !row.question().tags().isEmpty()) {
                    for(String tag : row.question().tags()) {
                        TagView tagView = TagView.formView(tagsLayout, tag);
                        tagView.setOnClickListener(v -> eventBus.send(new Pair<>(AppEvents.QUESTION_TAG_CLICKED, tag)));
                        tagsLayout.addView(tagView);
                    }
                }

                viewCount.setText(String.valueOf(row.question().viewCount()));
                answerCount.setText(String.valueOf(row.question().answerCount()));
                votesCount.setText(String.valueOf(row.question().score()));
            }

            @Override
            void destroy() {

            }
        }

        public static class LoadMoreVH extends QuestionAdapterVH {

            LoadMoreVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public static LoadMoreVH create(ViewGroup parent) {
                View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_load_more, parent, false);
                return new LoadMoreVH(rootView);
            }


            @Override
            void bind(QuestionAdapterRow row, RxEventBus eventBus) {

            }

            @Override
            void destroy() {

            }
        }


        public static class LoadingVH extends QuestionAdapterVH {

            LoadingVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public static LoadingVH create(ViewGroup parent) {
                View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_loading, parent, false);
                return new LoadingVH(rootView);
            }


            @Override
            void bind(QuestionAdapterRow row, RxEventBus eventBus) {

            }

            @Override
            void destroy() {

            }
        }

        public static class ErrorVH extends QuestionAdapterVH {

            ErrorVH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public static ErrorVH create(ViewGroup parent) {
                View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_error, parent, false);
                return new ErrorVH(rootView);
            }


            @Override
            void bind(QuestionAdapterRow row, RxEventBus eventBus) {

            }

            @Override
            void destroy() {

            }
        }
    }
}
