package trash.jak.id.go.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

/**
 * Created by itp on 06/12/17.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener{
        LinearLayoutManager layoutManager;

        public EndlessScrollListener(LinearLayoutManager layoutManager) {
                this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading() && !isLastPage()) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                                && firstVisibleItemPosition >= 0
                                && totalItemCount >= PAGE_SIZE) {
                                loadMoreItems();
                        }
                }
        }

        protected abstract void loadMoreItems();

        public abstract int getTotalPageCount();

        public abstract boolean isLastPage();

        public abstract boolean isLoading();
}