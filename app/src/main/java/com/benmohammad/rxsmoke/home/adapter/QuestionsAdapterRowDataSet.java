package com.benmohammad.rxsmoke.home.adapter;

import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapterRowDataSet extends SortedListAdapterCallback<QuestionAdapterRow> {

    private SortedList<QuestionAdapterRow> sortedlist;

    public QuestionsAdapterRowDataSet(QuestionsAdapter adapter, ArrayList<QuestionAdapterRow> data){
        super(adapter);
        sortedlist = new SortedList<>(QuestionAdapterRow.class, this);
        if(data != null && !data.isEmpty()) {
            sortedlist.addAll(data);
        }
    }

    public static QuestionsAdapterRowDataSet createWithInitialData(QuestionsAdapter adapter, ArrayList<QuestionAdapterRow> data) {
        return new QuestionsAdapterRowDataSet(adapter, data);
    }

    public static QuestionsAdapterRowDataSet createWithEmptyData(QuestionsAdapter adapter) {
        return new QuestionsAdapterRowDataSet(adapter, null);
    }

    @Override
    public int compare(QuestionAdapterRow o1, QuestionAdapterRow o2) {
        return o1.compare(o2);
    }

    @Override
    public boolean areContentsTheSame(QuestionAdapterRow oldItem, QuestionAdapterRow newItem) {
        return oldItem.areContentsTheSame(newItem);
    }

    @Override
    public boolean areItemsTheSame(QuestionAdapterRow item1, QuestionAdapterRow item2) {
        return item1.areItemsTheSame(item2);
    }

    public void addAllRows(List<QuestionAdapterRow> rows) {
        for(QuestionAdapterRow row : rows) {
            sortedlist.add(row);
        }
    }

    public void addRow(QuestionAdapterRow row) {
        sortedlist.add(row);
    }

    public void clearDataSet() {
        sortedlist.clear();
    }

    public void handleDestroy() {
        sortedlist.clear();
    }

    public int size() {
        return sortedlist.size();
    }

    public boolean isEmpty() {
        return sortedlist.size() == 0;
    }

    public QuestionAdapterRow get(int pos) {
        return sortedlist.get(pos);
    }

    public boolean containsLoadMore() {
        for(int i = 0; i < sortedlist.size(); i++) {
            if(sortedlist.get(0).isTypeLoadMore()) {
                return true;
            }
        }
        return false;
    }

    public void removeLoadMore() {
        int index = -1;
        for(int i = 0; i < sortedlist.size(); i ++) {
            if(sortedlist.get(i).isTypeLoadMore()) {
                index = i;
            }
        }
        if(index > -1) {
            sortedlist.removeItemAt(index);
        }
    }

    public void removeLoading() {
        int index = -1;
        for(int i = 0; i < sortedlist.size(); i++) {
            if(sortedlist.get(i).isTypeLoading()) {
                index = i;
            }
        }
        if(index > -1) {
            sortedlist.removeItemAt(index);
        }
    }

    public void removeError() {
        int index = -1;
        for(int i = 0; i < sortedlist.size(); i++) {
            if(sortedlist.get(i).isTypeError()) {
                index = i;
            }
        }
        if(index > -1) {
            sortedlist.removeItemAt(index);

        }
    }
}
