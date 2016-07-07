package com.leaf.gankio.ui.gank;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leaf.gankio.R;
import com.leaf.gankio.entity.Daily;
import com.leaf.gankio.ui.base.AnimRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/16 17:31
 * @TODO ： ...
 */

public class DailyGankAdapter extends AnimRecyclerViewAdapter<DailyGankAdapter.ViewHolder> {

    private List<Daily.ResultsBean.GankBean> list;
    public DailyGankAdapter(List<Daily.ResultsBean.GankBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_gank_item, parent, false);
        return new ViewHolder(v);
    }


    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Daily.ResultsBean.GankBean gankBean = list.get(position);
        if (position == 0) {
            showCategory(holder);
        }
        else {
            boolean theCategoryOfLastEqualsToThis = list.get(
                    position - 1).getType().equals(list.get(position).getType());
            if (!theCategoryOfLastEqualsToThis) {
                showCategory(holder);
            }
            else {
                hideCategory(holder);
            }
        }
        holder.gankCategory.setText(gankBean.getType());
        SpannableStringBuilder builder = new SpannableStringBuilder(gankBean.getDesc()).append(
                format(holder.gankTitle.getContext(), " (by " +
                        gankBean.getWho()+
                        ")", R.style.ViaTextAppearance));
        CharSequence gankText = builder.subSequence(0, builder.length());

        holder.gankTitle.setText(gankText);
        showItemAnim(holder.gankTitle, position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showCategory(ViewHolder holder) {
        if (!isVisibleOf(holder.gankCategory)) holder.gankCategory.setVisibility(View.VISIBLE);
    }

    private void hideCategory(ViewHolder holder) {
        if (isVisibleOf(holder.gankCategory)) holder.gankCategory.setVisibility(View.GONE);
    }

    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_category)
        TextView gankCategory;
        @Bind(R.id.tv_title)
        TextView gankTitle;
        @Bind(R.id.gank_lay)
        LinearLayout gankLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.gank_lay)
        public void onGotoWebGank(View v) {
            Daily.ResultsBean.GankBean gankBean = list.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(),WebGank.class) ;
            intent.putExtra("desc",gankBean.getDesc()) ;
            intent.putExtra("url",gankBean.getUrl()) ;
            intent.putExtra("category",gankBean.getType()) ;
            v.getContext().startActivity(intent);
        }
    }

    public SpannableString format(Context context, String text, int style) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new TextAppearanceSpan(context, style), 0, text.length(),
                0);
        return spannableString;
    }
}
