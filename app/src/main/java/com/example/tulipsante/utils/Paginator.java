package com.example.tulipsante.utils;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public abstract class Paginator extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    public Paginator(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            System.out.println("inside first");
            System.out.println(visibleItemCount);
            System.out.println(firstVisibleItemPosition);
            System.out.println(totalItemCount);
            System.out.println((visibleItemCount + firstVisibleItemPosition) >= totalItemCount);
            System.out.println(firstVisibleItemPosition >= 0);
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                System.out.println("loading more");
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}