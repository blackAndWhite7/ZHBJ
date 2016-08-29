package com.project.wei.zhbj.basepager.subclass.basemenudetailpager;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.project.wei.zhbj.R;
import com.project.wei.zhbj.basepager.BaseMenuDetailPager;
import com.project.wei.zhbj.domain.PhotosBean;
import com.project.wei.zhbj.global.GlobalConstants;
import com.project.wei.zhbj.utils.CacheUtil;
import com.project.wei.zhbj.utils.MyBitmapUtils;

import java.util.ArrayList;

/**
 * 菜单详情页-组图
 * @author Kevin
 * @date 2015-10-18
 */
public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener{

	@ViewInject(R.id.lv_photos)
	private ListView lv_photos;
	@ViewInject(R.id.gv_photos)
	private GridView gv_photos;
	private String result;
	private ArrayList<PhotosBean.DataDetails> photosNews;
	public  MyBitmapUtils bitmapUtils;
	private ImageButton ibtn_change;

	public PhotosMenuDetailPager(Activity activity, ImageButton ibtn_change) {
		super(activity);
		this.ibtn_change = ibtn_change;
	}

	@Override
	public View initView() {
		View photosView = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
		ViewUtils.inject(this,photosView);
		return photosView;
	}

	@Override
	public void initData() {
		String cache = CacheUtil.getCache(GlobalConstants.PHOTOS_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		ibtn_change.setOnClickListener(this);
		ibtn_change.setVisibility(View.VISIBLE);
		getDataFromServer();

	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.PHOTOS_URL, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				result = responseInfo.result;
				CacheUtil.setCache(GlobalConstants.PHOTOS_URL, result,mActivity);
				processData(result);
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	private void processData(String result) {
		Gson gson = new Gson();
		PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
		photosNews = photosBean.data.news;

		lv_photos.setAdapter(new PhotosAdapter());
		gv_photos.setAdapter(new PhotosAdapter());
	}
    private boolean showListView = true;// 标记当前是否是listview展示
	@Override
	public void onClick(View v) {
		if (showListView) {
			lv_photos.setVisibility(View.GONE);
			gv_photos.setVisibility(View.VISIBLE);
			ibtn_change.setImageResource(R.drawable.icon_pic_list_type);
			showListView = false;
		} else {
			lv_photos.setVisibility(View.VISIBLE);
			gv_photos.setVisibility(View.GONE);
			ibtn_change.setImageResource(R.drawable.icon_pic_grid_type);
			showListView = true;
		}
	}

	class PhotosAdapter extends BaseAdapter {
//      使用自定义的MyBitmapUtils来完成下载--设置--缓存--防止内存溢出等功能
		public PhotosAdapter(){
			bitmapUtils = new MyBitmapUtils();
		}
		@Override
		public int getCount() {
			return photosNews.size();
		}

		@Override
		public PhotosBean.DataDetails getItem(int position) {
			return photosNews.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.item_list_photos, null);
				viewHolder.ivPhotos = (ImageView) convertView.findViewById(R.id.iv_photos);
				viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PhotosBean.DataDetails photosDetails = getItem(position);
			viewHolder.tvTitle.setText(photosDetails.title);
			bitmapUtils.display(viewHolder.ivPhotos,photosDetails.listimage);
			return convertView;
		}
	}

	static class ViewHolder {
		public ImageView ivPhotos;
		public TextView tvTitle;
	}

}
