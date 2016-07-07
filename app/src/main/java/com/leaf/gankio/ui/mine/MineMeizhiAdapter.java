package com.leaf.gankio.ui.mine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leaf.gankio.R;
import com.leaf.gankio.entity.MeiziBmob;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.utils.GlideBuilderTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/17 20:34
 * @TODO ： ...
 */

public class MineMeizhiAdapter extends RecyclerView.Adapter<MineMeizhiAdapter.ViewHolder> implements View.OnClickListener{


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<MeiziBmob> list;
    private Context context ;

    public MineMeizhiAdapter(List<MeiziBmob> list) {
        this.list = list;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_meizhi_item, parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MeiziBmob meiziBmob = list.get(position) ;
        GlideBuilderTool.getInstance().loadImg(context,meiziBmob.getUrl(),holder.imgMeizi);
        holder.itemView.setTag(meiziBmob);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,(MeiziBmob)v.getTag(),list.indexOf((MeiziBmob)v.getTag()));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_mine_meizi)
        ImageView imgMeizi;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
