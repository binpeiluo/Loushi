package com.android.loushi.loushi.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.loushi.loushi.R;
import com.android.loushi.loushi.adapter.MyMessageAdapter;
import com.android.loushi.loushi.callback.JsonCallback;
import com.android.loushi.loushi.jsonbean.UserMessageJson;
import com.android.loushi.loushi.util.KeyConstant;
import com.android.loushi.loushi.util.SpaceItemDecoration;
import com.android.loushi.loushi.util.UrlConstant;
import com.lzy.okhttputils.OkHttpUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by binpeiluo on 2016/7/31 0031.
 */
public class MyMessageActivity extends BaseActivity implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private ImageView imageViewBack;  //imageView_back
    private TextView textViewTitle;//textView_title
    private ImageView imageViewSearch;//imageView_search
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycleView;

    private MyMessageAdapter mAdapter;
    private List<UserMessageJson.BodyBean> myMessageList = new ArrayList<UserMessageJson.BodyBean>();

    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mymessage_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

        initToolbar();
        initRecycleView();
        loadMessage();

    }

    private void loadMessage(){
        loadComment(MainActivity.user_id);
    }

    private void loadComment(String user_id) {
        OkHttpUtils.post(UrlConstant.MYMESSAGE)
                .tag(this)
                .params(KeyConstant.USER_ID, user_id)
                .execute(new JsonCallback<UserMessageJson>(UserMessageJson.class) {
                    @Override
                    public void onResponse(boolean isFromCache, UserMessageJson userMessageJson,
                                           Request request, @Nullable Response response) {
                        if (userMessageJson.getState()) {
                            myMessageList.clear();
                            myMessageList.addAll(userMessageJson.getBody());
                            try {
                                mAdapter.parseMessage();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else
                            Toast.makeText(MyMessageActivity.this, "网络异常...", Toast.LENGTH_SHORT)
                                    .show();
                    }
                });
    }

    private void initRecycleView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new SpaceItemDecoration(this,14));
        mAdapter = new MyMessageAdapter(this, myMessageList);
        mAdapter.setmOnItemClickListener(new MyMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int postion) {
                Toast.makeText(MyMessageActivity.this, ""+postion, Toast.LENGTH_SHORT).show();
            }
        });
        recycleView.setAdapter(mAdapter);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setProgressViewOffset(
                true,
                0,
                (int)TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics()));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        firstVisibleItemPosition == 0) {
                    swipeRefreshLayout.setRefreshing(true);
                    loadMessage();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisibleItemPosition == mAdapter.getItemCount() - 1)
                    Toast.makeText(MyMessageActivity.this, "憋拉啦,没数据惹", Toast.LENGTH_SHORT).show();
                else {

                }
            }
        });
    }

    private void initToolbar() {
        imageViewBack = (ImageView) findViewById(R.id.imageView_back);
        textViewTitle = (TextView) findViewById(R.id.textView_title);
        imageViewSearch = (ImageView) findViewById(R.id.imageView_search);
        imageViewBack.setOnClickListener(this);
        textViewTitle.setText("我的消息");
        imageViewSearch.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }
}
