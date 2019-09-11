package com.jeff.analysis.core.common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeff.analysis.core.util.ContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff
 * @describe
 * @date 2019/4/19.
 */
public abstract class AbstractAdapter<M, H extends BaseViewHolder<M>> extends RecyclerView.Adapter<H> {
    private static final String TAG = AbstractAdapter.class.getSimpleName();

    protected static final int VIEW_TYPE_EMPTY = 0xfff0;
    protected static final int VIEW_TYPE_DEFAULT = 0xfff1;

    private final SparseArray<View> mViewArray = new SparseArray<>();
    protected List<M> mData;
    protected Context mContext;
    protected ViewGroup mParent;
    private LayoutInflater mInflater;

    private OnItemListener.OnItemClickListener mClickListener;
    private OnItemListener.OnItemLongClickListener mLongClickListener;
    private OnItemListener.OnItemFocusChangedListener<H> mFocusChangeListener;
    private OnItemListener.OnItemSelectListener mSelectListener;
    private OnItemListener.OnItemKeyListener mKeyListener;

    public AbstractAdapter(){
        mData=new ArrayList<>();
    }

    public AbstractAdapter(List<M> data) {
        mData = data;
    }

    public void setData(@NonNull final List<M> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addData(@NonNull final List<M> data) {
        mData.addAll(data);
        notifyItemRangeChanged(getDataSize(), data.size());
    }

    public void addItem(int position, M item) {
        mData.add(position, item);
        notifyItemInserted(position);
        //保证position刷新
        notifyItemRangeInserted(position,getDataSize()-position);
    }

    public void addItem(M item){
        int position=getDataSize();
        addItem(position,item);
    }

    public void remove(int position){
        mData.remove(position);
        notifyItemRemoved(position);
        //保证position刷新
        notifyItemRangeChanged(position,getDataSize()-position);
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void move(int position,int newPosition){
        M data=mData.remove(position);
        mData.add(newPosition,data);
        notifyItemMoved(position, newPosition);
        //保证position刷新
        notifyItemRangeChanged(0,getDataSize());
    }

    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, this.getClass().getSimpleName() + " onCreateViewHolder: ");
        return createViewHolder(getItemView(parent, viewType), viewType);
    }

    /**
     * 创建新的ViewHolder
     *
     * @param itemView
     * @param viewType
     * @return
     */
    protected abstract H createViewHolder(View itemView, int viewType);

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_EMPTY:
                break;
            default:
                bindCustomViewHolder(holder,position);
                break;
        }
    }

    protected void bindCustomViewHolder(final H holder,final int position) {
        if (mClickListener!=null){
            holder.getBindListenerView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v,position);
                }
            });
        }
        if (mLongClickListener!=null){
            holder.getBindListenerView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onItemLongClick(v,position);
                }
            });
        }
        holder.getBindListenerView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mFocusChangeListener!=null){
                    mFocusChangeListener.onItemFocusChanged(v,hasFocus,holder,position);
                }
                if (mSelectListener!=null){
                    if (hasFocus){
                        mSelectListener.onItemSelected(v,position);
                    }else {
                        mSelectListener.onItemUnSelected(v,position);
                    }
                }
            }
        });
        if (mKeyListener!=null){
            holder.getBindListenerView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return mKeyListener.onKey(v,keyCode,event,position);
                }
            });
        }
        bind(holder, mData.get(position), position);
    }

    /**
     * ViewHolder绑定数据
     *
     * @param holder
     * @param data
     * @param position
     */
    protected void bind(final H holder, final M data, final int position){
        Log.d(TAG,holder.getClass().getSimpleName()+" bind: position= "+holder.getAdapterPosition());
        holder.bind(data, position);
        holder.setRecycled(false);
    }

    @Override
    public int getItemViewType(int position) {
        if (getDataSize() == 0 && getView(VIEW_TYPE_EMPTY) != null) {
            return VIEW_TYPE_EMPTY;
        }else {
            return getCustomViewType(position);
        }
    }

    protected int getCustomViewType(final int position) {
        return VIEW_TYPE_DEFAULT;
    }

    /**
     * 获取itemView
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected View getItemView(ViewGroup parent, int viewType) {
        if (mParent == null) {
            mParent = parent;
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(mContext);
        }
        View itemView = getView(viewType);
        if (itemView == null) {
            if (bindLayout(viewType) != 0) {
                itemView = inflateLayout(bindLayout(viewType));
                Log.i(TAG, this.getClass().getSimpleName() + " getItemView: bindLayout");
            } else {
                itemView = bindView(viewType);
                if (itemView == null) {
                    throw new IllegalArgumentException(this.getClass().getName() + " has not bind view by method #bindLayout or #bindView.");
                }
                Log.i(TAG, this.getClass().getSimpleName() + " getItemView: bindView");
            }
        }
        return itemView;
    }

    /**
     * 绑定布局文件
     * 同样也可以通过{@link #bindView(int)}来绑定自定义的View
     *
     * @param viewType
     * @return
     */
    protected abstract int bindLayout(final int viewType);

    /**
     * 绑定代码中自定义View
     *
     * @param viewType
     * @return
     */
    protected View bindView(int viewType) {
        return null;
    }

    /**
     * 根据布局id获取View
     *
     * @param layoutId
     * @return
     */
    protected View inflateLayout(@LayoutRes final int layoutId) {
        return mInflater.inflate(layoutId, mParent, false);
    }

    public int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemCount() {
        return getDataSize();
    }

    protected void setView(final int type, @NonNull final View view) {
        mViewArray.put(type, view);
        notifyDataSetChanged();
    }

    protected View getView(final int type) {
        return mViewArray.get(type);
    }

    protected void removeView(final int type) {
        if (getView(type) != null) {
            mViewArray.delete(type);
            notifyDataSetChanged();
        }
    }

    public void setEmptyView(@NonNull View emptyView) {
        setView(VIEW_TYPE_EMPTY, emptyView);
    }

    public View getEmptyView() {
        return getView(VIEW_TYPE_EMPTY);
    }

    public void removeEmptyView() {
        removeView(VIEW_TYPE_EMPTY);
    }

    public M getItem(int position) {
        return mData.get(position);
    }

    protected int getColor(int resId) {
        return ContextUtils.getColor(resId);
    }

    protected String getString(int resId) {
        return ContextUtils.getString(resId);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull H holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.isRecycled()){
            onBindViewHolder(holder,holder.getAdapterPosition());
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull H holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(TAG,holder.getClass().getSimpleName()+" release: position= "+holder.getAdapterPosition());
        holder.release(holder.getAdapterPosition());
        holder.setRecycled(true);
    }

    public void setOnItemClickListener(OnItemListener.OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemListener.OnItemLongClickListener longClickListener) {
        mLongClickListener = longClickListener;
    }

    public void setOnFocusChangeListener(OnItemListener.OnItemFocusChangedListener<H> focusChangeListener) {
        mFocusChangeListener = focusChangeListener;
    }

    public void setOnSelectListener(OnItemListener.OnItemSelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public void setOnKeyListener(OnItemListener.OnItemKeyListener keyListener) {
        this.mKeyListener = keyListener;
    }
}
