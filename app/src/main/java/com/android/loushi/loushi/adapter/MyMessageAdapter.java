package com.android.loushi.loushi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loushi.loushi.R;

import com.android.loushi.loushi.jsonbean.UserMessageJson;
import com.android.loushi.loushi.util.CircleImageTransformation;
import com.android.loushi.loushi.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by binpeiluo on 2016/7/21 0021.
 */
public class MyMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnItemClickListener mOnItemClickListener;
    private CircleImageTransformation mTransformation;

    private Context mContext;
    private List<UserMessageJson.BodyBean> myMessageList;

//    private List<UserMessageJson.BodyBean> mNewMessgeList=new ArrayList<UserMessageJson.BodyBean>();
//    private List<UserMessageJson.BodyBean> mOldMessageList=new ArrayList<UserMessageJson.BodyBean>();
    private int newCommentCount;  //新评论条数


    private static int TITLECOUNT=2;
    private static int BOTTOMCOUNT=1;
    private static String TITLENEW="新评论";
    private static String TITLEOLD="过往评论";

    private enum ViewType{
        TITLE,CONTENT,BOTTOM;
    }

    public MyMessageAdapter(Context mContext, List<UserMessageJson.BodyBean> myMessageList){
        this.mContext=mContext;
        this.myMessageList=myMessageList;
        this.mTransformation=new CircleImageTransformation();

    }

    /**
     * 解析我的消息list  按照日期被分为新评论与旧评论
     * @throws ParseException
     */
    public void parseMessage() throws ParseException{
        newCommentCount=DateUtils.parseMessage(myMessageList);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0||position==newCommentCount+1)
            return ViewType.TITLE.ordinal();
        else if(position==getItemCount()-1)
            return ViewType.BOTTOM.ordinal();
        else
            return ViewType.CONTENT.ordinal();
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        if(viewType==ViewType.TITLE.ordinal()){
            View view=View.inflate(mContext, R.layout.layout_message_title,null);
            holder=new TitleViewHolder(view);
        }else if(viewType==ViewType.CONTENT.ordinal()){
            View view=View.inflate(mContext, R.layout.item_newmessage,null);
            holder=new ContentViewHolder(view);
        }else if(viewType== ViewType.BOTTOM.ordinal()){
            View view=View.inflate(mContext,R.layout.mymessage_bottom,null);
            holder=new BottomViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ContentViewHolder){
            UserMessageJson.BodyBean myMessage;
            if(position<=newCommentCount){
                myMessage=myMessageList.get(position-1);
            }
            else
                myMessage=myMessageList.get(position-2);
            //TODO 圆形复用问题
            Picasso.with(mContext).load(myMessage.getComment().getUserInfo().getHeadImgUrl())
                    .fit()
                    .into(((ContentViewHolder) holder).imageView_User);
            ((ContentViewHolder) holder).textView_UserName.setText(
                    myMessage.getComment().getUserInfo().getNickname());
            ((ContentViewHolder) holder).textView_userContont.setText(
                    myMessage.getComment().getContent());
            ((ContentViewHolder) holder).textView_MessageDate.setText(
                    DateUtils.calulateDate(myMessage.getComment().getCDate()));
            ((ContentViewHolder) holder).textView_MessageTime.setText(
                    DateUtils.calulateTime(myMessage.getComment().getCDate()));
        }else if(holder instanceof TitleViewHolder){
            ((TitleViewHolder) holder).textViewTitle.setText(position==0?TITLENEW:TITLEOLD);
        }
    }

    @Override
    public int getItemCount() {
        return myMessageList.size()+TITLECOUNT;
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView_User;
        private TextView textView_UserName;
        private TextView textView_userContont;
        private TextView textView_MessageDate;
        private TextView textView_MessageTime;

        public ContentViewHolder(View itemView) {
            super(itemView);
            imageView_User= (ImageView) itemView.findViewById(R.id.item_imageView_message);
            textView_UserName= (TextView) itemView.findViewById(R.id.item_textView_messageUserName);
            textView_userContont= (TextView) itemView.findViewById(R.id.item_textView_messageContent);
            textView_MessageDate= (TextView) itemView.findViewById(R.id.item_textView_messageDate);
            textView_MessageTime= (TextView) itemView.findViewById(R.id.item_textView_messageTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null)
                mOnItemClickListener.onItemClick(v,getPosition());
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder  {

        private View viewHint;
        private TextView textViewTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            textViewTitle= (TextView) itemView.findViewById(R.id.textView_title);
            viewHint= itemView.findViewById(R.id.view_hint);
        }

    }
    public class BottomViewHolder extends RecyclerView.ViewHolder  {

        private LinearLayout btn_cleanComment;

        public BottomViewHolder(View itemView) {
            super(itemView);
            btn_cleanComment= (LinearLayout) itemView.findViewById(R.id.btn_cleanComment);
            btn_cleanComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"clear up",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    public interface OnItemClickListener{
        void onItemClick(View v,int postion);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
