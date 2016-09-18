package cniao5shop.com.cniao5.cniaoshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import cniao5shop.com.cniao5.cniaoshop.Constants;
import cniao5shop.com.cniao5.cniaoshop.R;
import cniao5shop.com.cniao5.cniaoshop.WareListActivity;
import cniao5shop.com.cniao5.cniaoshop.adapter.HomeCategoryAdapter;
import cniao5shop.com.cniao5.cniaoshop.adapter.decoration.CardViewtemDecortion;
import cniao5shop.com.cniao5.cniaoshop.bean.Banner;
import cniao5shop.com.cniao5.cniaoshop.bean.Campaign;
import cniao5shop.com.cniao5.cniaoshop.bean.HomeCampaign;
import cniao5shop.com.cniao5.cniaoshop.http.BaseCallback;
import cniao5shop.com.cniao5.cniaoshop.http.OkHttpHelper;
import cniao5shop.com.cniao5.cniaoshop.http.SpotsCallback;

public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.slider)
    private SliderLayout sliderShow;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private Gson mGson = new Gson();
    private List<Banner> mBanner;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private HomeCategoryAdapter mAdatper;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void init() {
        requestImage();
        initRecyclerView();
    }

    void requestImage() {
        okHttpHelper.get(Constants.API.BANNER, new SpotsCallback<List<Banner>>(this.getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                if (mBanner != null)
                    initSlider();
            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void initSlider() {
        for (final Banner banner : mBanner) {
            TextSliderView textSliderView = new TextSliderView(this.getActivity());
            textSliderView.description(banner.getName()).image(banner.getImgUrl());
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(HomeFragment.this.getContext(), banner.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            sliderShow.addSlider(textSliderView);
        }
        //设置图片切换动画以及展示时间
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderShow.setDuration(2500);
        //添加图片切换事件
        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // Log.i("Slide", "onPageScrolled");
            }
            @Override
            public void onPageSelected(int position) {
               // Log.i("Slide", "onPageSelected");
            }
            @Override
            public void onPageScrollStateChanged(int state) {
               // Log.i("Slide", "onPageScrollStateChanged");
            }
        });
    }

    private void initRecyclerView(){
        okHttpHelper.get(Constants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>(this.getContext()){
            @Override
            public void onRequestBefore(Request request) {
                
            }
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(Response response) {

            }
            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private  void initData(List<HomeCampaign> homeCampaigns){
        mAdatper = new HomeCategoryAdapter(homeCampaigns,getActivity());
        mAdatper.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Constants.COMPAINGAIN_ID,campaign.getId());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdatper);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sliderShow.stopAutoCycle();
    }
}