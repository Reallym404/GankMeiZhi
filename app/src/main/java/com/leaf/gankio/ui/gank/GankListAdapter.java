package com.leaf.gankio.ui.gank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leaf.gankio.R;
import com.leaf.gankio.entity.History;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/15 14:01
 * @TODO ： ...
 */

public class GankListAdapter extends RecyclerView.Adapter<GankListAdapter.ViewHolder> implements View.OnClickListener {

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<History> list;
    private Context context ;

    public GankListAdapter(List<History> list){
        this.list = list ;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fmt_gank_history_item, parent,false);
        view.setOnClickListener(this);
        return new GankListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        History history = list.get(position) ;
        holder.gankTitle.setText(history.getTitle());
        holder.itemView.setTag(history);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,(History)v.getTag(),list.indexOf((History)v.getTag()));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.gank_title)
        TextView gankTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
