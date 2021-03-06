package com.android.loushi.loushi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.loushi.loushi.R;

import com.android.loushi.loushi.jsonbean.UserCollectionsGood;
import com.android.loushi.loushi.jsonbean.UserCollectionsJson;
import com.squareup.picasso.Picasso;


import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/22.
 */
public class CollectGoodAdapter  extends RecyclerView.Adapter<CollectGoodAdapter.CollectGoodViewHolder> {

    private Context context;
    private String type;
    private List<UserCollectionsJson.BodyBean> beanList;
    public CollectGoodAdapter(Context context,List<UserCollectionsJson.BodyBean> beanList,String type){
        this.context = context;
        this.beanList=beanList;
        this.type = type;
    }
    @Override
    public CollectGoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectGoodViewHolder(LayoutInflater.from(
                context).inflate(R.layout.collect_good_item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(final CollectGoodViewHolder holder, final int position) {
        if(type.equals("3")) {
            final UserCollectionsJson.BodyBean.GoodsBean goodsBean = beanList.get(position).getGoods();
            holder.tv_name.setText(goodsBean.getName());
            Picasso.with(context).load(goodsBean.getImages().get(0).getUrl()).fit().into(holder.img_good);
            holder.tv_price.setText(goodsBean.getPrice()+"");
            holder.tv_like_count.setText(Integer.toString(goodsBean.getCollectionNum()));
            holder.btn_like.setChecked(true);
//        holder.btn_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                OkHttpUtils.post().url("http://119.29.187.58:10000/LouShi/user/userCollect.action"
//                ).addParams("user_id", "32").addParams("type", "3")
//                        .addParams("pid", Integer.toString(goodsBean.getId())).build().execute(new NormalCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        Log.e("goodadapter", Log.getStackTraceString(e));
//                    }
//
//                    @Override
//                    public void onResponse(ResponseJson responseJson) {
//                        if (responseJson.getState()) {
//
//                            if (isChecked) {
//
//                                //good.setCollectionNum(good.getCollectionNum() + 1);
//                                holder.tv_like_count.setText(Integer.toString(goodsBean.getCollectionNum()));
//                                //goodsList.remove(position);
//                            } else {
//
//                                //good.setCollectionNum(good.getCollectionNum() - 1);
//                                holder.tv_like_count.setText(Integer.toString(goodsBean.getCollectionNum()));
//
//                            }
//                            goodsBeanList.remove(position);
//
//                            notifyDataSetChanged();
//                        }
//
//                    }
//                });
//            }
//        });
        }
        if(type.equals("0")){
            final UserCollectionsJson.BodyBean.SceneBean goodsBean = beanList.get(position).getScene();
            holder.tv_name.setText(goodsBean.getName());
            holder.tv_price.setVisibility(View.INVISIBLE);
            holder.price_symbol.setVisibility(View.INVISIBLE);
            Picasso.with(context).load(goodsBean.getImgUrl()).fit().into(holder.img_good);

            holder.tv_like_count.setText(Integer.toString(goodsBean.getBrowseNum())+"人浏览");
            holder.btn_like.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
    public static class CollectGoodViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_good;
        TextView tv_like_count;
        CheckBox btn_like;
        TextView price_symbol;
        TextView tv_price;
        public CollectGoodViewHolder(View view) {
            super(view);
            tv_name = (TextView)view.findViewById(R.id.collect_good_item_tv_name);
            img_good=(ImageView)view.findViewById(R.id.collect_good_item_img_good);
            btn_like = (CheckBox)view.findViewById(R.id.collect_good_item_btn_like);
            tv_like_count=(TextView)view.findViewById(R.id.collect_good_item_tv_like_count);
            price_symbol=(TextView)view.findViewById(R.id.price_symbol);
            tv_price=(TextView)view.findViewById(R.id.tv_price);
        }


}
}

