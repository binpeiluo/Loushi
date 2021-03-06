package com.android.loushi.loushi.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.loushi.loushi.R;
import com.android.loushi.loushi.adapter.AdViewpagerAdapter;
import com.android.loushi.loushi.adapter.ViewPagerAdapter;
import com.android.loushi.loushi.callback.JsonCallback;
import com.android.loushi.loushi.jsonbean.ResponseJson;
import com.android.loushi.loushi.jsonbean.SceneJson;
import com.android.loushi.loushi.ui.fragment.CollectGoodFragment;
import com.android.loushi.loushi.ui.fragment.SceneDetailDesignFragment;
import com.android.loushi.loushi.ui.fragment.SceneDetailGoodFragment;
import com.android.loushi.loushi.util.KeyConstant;
import com.android.loushi.loushi.viewpager.CarouselViewPager;
import com.google.gson.Gson;
import com.lzy.okhttputils.OkHttpUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/7/24.
 */
public class SceneDetailActivity extends  BaseActivity {
    private CarouselViewPager carouselViewPager;
    private SceneDetailGoodFragment sceneDetailGoodFragment;
    private SceneDetailDesignFragment sceneDetailDesignFragment;
    private CollapsingToolbarLayout collapsing_toolbar_layout;
    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private ImageButton back;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    public static  String SCENE_STRING="SCENE_STRING";
    public static  String SCENE_ID="SCENE_ID";
    private SceneJson.BodyBean scenebean;

    private LinearLayout collect_bar;
    private LinearLayout collect;
    private LinearLayout comment;
    private LinearLayout share;
    private ImageButton btn_collect;
    private ImageButton btn_comment;
    private TextView tv_collect_count;
    private TextView tv_comment_count;
    private TextView tv_share_count;
    private String sceneJsonString="";
    public  String scene_id="1";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_detail;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_detail);
        if(getIntent().getStringExtra(SCENE_STRING)!=null)
        sceneJsonString = getIntent().getStringExtra(SCENE_STRING);
        Log.e("sceneJson",sceneJsonString);
        scenebean=new SceneJson.BodyBean();
        scenebean=new Gson().fromJson(sceneJsonString,SceneJson.BodyBean.class);
        scene_id = scenebean.getId()+"";
        Log.e("scene_id", scene_id);
        initView();
        initTablayout();
        initToobar();
        //bindCollectBarView();
    }

    private void initView() {
        carouselViewPager =(CarouselViewPager)findViewById(R.id.ad_viewPager);
        viewPager = (ViewPager)findViewById(R.id.main_vp_container);
        ImageView view1 = (ImageView) LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.ad_image, null);
        ImageView view2 = (ImageView) LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.ad_image, null);
        ImageView view3 = (ImageView) LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.ad_image, null);
        ImageView view4 = (ImageView) LayoutInflater.from(
                getApplicationContext()).inflate(R.layout.ad_image, null);
        ArrayList<ImageView> views = new ArrayList<ImageView>();
        //ImageView img = new ImageView(getApplicationContext());

        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);


        AdViewpagerAdapter adViewpagerAdapter = new AdViewpagerAdapter(views);

        carouselViewPager.setAdapter(adViewpagerAdapter);
        Picasso.with(getApplicationContext()).load(scenebean.getImgUrl()).into(view1);
        Picasso.with(getApplicationContext()).load(scenebean.getImgUrl()).into(view2);
        Picasso.with(getApplicationContext()).load(scenebean.getImgUrl()).into(view3);
        Picasso.with(getApplicationContext()).load(scenebean.getImgUrl()).into(view4);
    }
    private void initTablayout(){
        collapsing_toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        collapsing_toolbar_layout.setTitle("");
        tabLayout=(TabLayout)findViewById(R.id.toolbar_tab);
        viewPager = (ViewPager)findViewById(R.id.main_vp_container);
        sceneDetailGoodFragment = new SceneDetailGoodFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SCENE_ID",scene_id);
        sceneDetailGoodFragment.setArguments(bundle);
        sceneDetailDesignFragment = new SceneDetailDesignFragment();
        sceneDetailDesignFragment.setArguments(bundle);
        list_fragment = new ArrayList<>();
        list_title = new ArrayList<>();
        list_fragment.add(sceneDetailDesignFragment);

        list_fragment.add(sceneDetailGoodFragment);
        //list_fragment.add(collectGoodFragment);

        list_title.add("设计");
        list_title.add("购买");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText("设计"));
        tabLayout.addTab(tabLayout.newTab().setText("购买"));
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void initToobar(){
        mToolbar=(Toolbar)findViewById(R.id.program_toolbar);
        setSupportActionBar(mToolbar);
        back=(ImageButton)mToolbar.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void bindCollectBarView(){

            collect_bar=(LinearLayout)findViewById(R.id.collect_bar);
            //collect_bar.setVisibility(View.GONE);
            collect = (LinearLayout)collect_bar.findViewById(R.id.collect_bar_linear_like);
            comment = (LinearLayout)collect_bar.findViewById(R.id.collect_bar_linear_comment);
            share = (LinearLayout)collect_bar.findViewById(R.id.collect_bar_linear_share);
            btn_collect=(ImageButton)collect.findViewById(R.id.collect_bar_btn_like);
            tv_collect_count=(TextView)collect.findViewById(R.id.collect_bar_tv_like);
            tv_comment_count=(TextView)collect_bar.findViewById(R.id.collect_bar_tv_comment);
            tv_share_count=(TextView)collect_bar.findViewById(R.id.collect_bar_tv_share);
            tv_collect_count.setText(scenebean.getCollectionNum()+"");


    }
}
