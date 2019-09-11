package com.jeff.analysis.core.common;

import android.view.KeyEvent;
import android.view.View;

/**
 * @author Jeff
 * @describe
 * @date 2019/4/25.
 */
public interface OnItemListener {
    interface OnItemClickListener {
        void onItemClick(final View view, final int position);
    }

    interface OnItemFocusChangedListener<H extends BaseViewHolder> {
        void onItemFocusChanged(final View view, final boolean hasFocus, final H viewHolder, final int position);
    }

    interface OnItemKeyListener {
        boolean onKey(final View view, final int keyCode, final KeyEvent event, final int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(final View view, final int position);
    }

    interface OnItemSelectListener {
        void onItemSelected(final View view, final int position);

        void onItemUnSelected(final View view, final int position);
    }
}
