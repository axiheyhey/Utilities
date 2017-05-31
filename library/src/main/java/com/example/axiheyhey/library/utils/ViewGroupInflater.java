package com.example.axiheyhey.library.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by axiheyhey on 17/05/11.
 */

@SuppressWarnings("WeakerAccess")
public final class ViewGroupInflater {

    public static ViewGroupInflater with(@NonNull Context context) {
        return new ViewGroupInflater(context);
    }

    @NonNull
    private Context mContext;

    private ViewGroupInflater(@NonNull Context context) {
        mContext = context;
    }

    public <T, VG extends ViewGroup> Requester<T, VG> dataSource(@NonNull Adapter<T, VG> adapter) {
        return new Requester<>(mContext, adapter);
    }

    public static class Requester<T, VG extends ViewGroup> {

        @NonNull
        private Context mContext;

        @NonNull
        private Adapter<T, VG> mAdapter;

        @Nullable
        private OnItemClickListener<T> mOnItemClickListener;

        @Nullable
        private OnItemLongClickListener<T> mOnItemLongClickListener;

        private Requester(@NonNull Context context, @NonNull Adapter<T, VG> adapter) {
            mContext = context;
            mAdapter = adapter;
        }

        public Requester<T, VG> setOnItemClickListener(@NonNull OnItemClickListener<T> listener) {
            mOnItemClickListener = listener;
            return this;
        }

        public Requester<T, VG> setOnItemLongClickListener(@NonNull OnItemLongClickListener<T> listener) {
            mOnItemLongClickListener = listener;
            return this;
        }

        public void into(@NonNull VG parent) {
            parent.removeAllViews();

            final Context context = mContext;
            final Adapter<T, VG> adapter = mAdapter;
            View headerView = adapter.onCreateHeaderView(context, parent);
            if (headerView != null) {
                parent.addView(headerView);
            }

            for (int index = 0, count = adapter.getCount(); index < count; index++) {
                final int pos = index;
                final View child = adapter.onCreateView(context, parent, pos);
                if (mOnItemClickListener != null) {
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(v, adapter.getItem(pos), pos);
                        }
                    });
                }
                if (mOnItemLongClickListener != null) {
                    child.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return mOnItemLongClickListener.onItemLongClick(v, adapter.getItem(pos), pos);
                        }
                    });
                }
                parent.addView(child);
            }

            View footerView = adapter.onCreateFooterView(context, parent);
            if (footerView != null) {
                parent.addView(footerView);
            }
        }
    }

    public static abstract class Adapter<T, VG extends ViewGroup> {

        @Nullable
        protected View onCreateHeaderView(@NonNull Context context, @NonNull VG parent) {
            return null;
        }

        @Nullable
        protected View onCreateFooterView(@NonNull Context context, @NonNull VG parent) {
            return null;
        }

        protected abstract int getCount();

        @NonNull
        protected abstract T getItem(int position);

        @NonNull
        protected abstract View onCreateView(@NonNull Context context, @NonNull VG parent, int position);
    }

    public interface OnItemClickListener<T> {

        void onItemClick(@NonNull View view, @NonNull T itemObject, int position);
    }

    public interface OnItemLongClickListener<T> {

        boolean onItemLongClick(@NonNull View view, @NonNull T itemObject, int position);
    }

}
