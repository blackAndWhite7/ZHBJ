package com.project.wei.zhbj.domain;

import java.util.ArrayList;

/**
 * Created by wei on 2016/8/27 0027.
 */
//  组图界面的json数据
public class PhotosBean {

    public PhotosData data;
    public class PhotosData {
        public String more;
        public ArrayList<DataDetails> news;

    }

    public class DataDetails {
        public String id;
        public String listimage;
        public String title;
    }

}
