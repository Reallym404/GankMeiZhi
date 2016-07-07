package com.leaf.gankio.ui.meizi;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leaf.gankio.R;
import com.leaf.gankio.entity.Classify;
import com.leaf.gankio.listener.OnRecyclerViewItemClickListener;
import com.leaf.gankio.utils.GlideBuilderTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/6 16:08
 * @TODO ： ...
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements View.OnClickListener{

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<Classify.ClassifyResultsBean> list;
    private Context context ;

    public RecyclerViewAdapter(List<Classify.ClassifyResultsBean> list) {
        this.list = list;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fmt_meizi_item, parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Classify.ClassifyResultsBean resultsBean = list.get(position) ;
       // Log.e("onBindViewHolder","----onBindViewHolder:") ;
        //ImageLoader.getInstance().displayImage(resultsBean.getUrl(),holder.imgMeizi, ImageLoadOptions.getOptionsBuild());
        holder.imgTimeLay.setBackgroundDrawable(new ColorDrawable(0x4c000000));
        GlideBuilderTool.getInstance().loadImg(context,resultsBean.getUrl(),holder.imgMeizi);
        String time = resultsBean.getPublishedAt().split("T")[0] ;
        holder.imgTime.setText(time);
        holder.itemView.setTag(resultsBean);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,(Classify.ClassifyResultsBean)v.getTag(),list.indexOf((Classify.ClassifyResultsBean)v.getTag()));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_meizi)
        ImageView imgMeizi;
        @Bind(R.id.img_time)
        TextView imgTime;
        @Bind(R.id.img_time_lay)
        RelativeLayout imgTimeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
