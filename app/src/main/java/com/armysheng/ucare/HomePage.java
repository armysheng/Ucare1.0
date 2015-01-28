package com.armysheng.ucare;
//try_PullToRefresh_map  
/**
 * 完成了从TXT文本中提取，并向下刷新 
 * blog:http://blog.csdn.net/harvic880925/article/details/17708409 
 * @author harvic
 * @date  2013-12-31 
 *
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;

import com.armysheng.ucare.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomePage extends Activity {

    private static final String TEST_FILE_NAME = "Ucare" ;
    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private PullToRefreshListView mPullRefreshListView;
    MyAdapter adapter=null;
    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
//                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(20))
                .build();
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
//        setTitle("视频广场");

        imageLoader.init(ImageLoaderConfiguration.createDefault(this));


        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);

        //设定下拉监听函数  
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel  
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.  
                new GetDataTask().execute();
            }
        });

        mPullRefreshListView.setMode(Mode.PULL_FROM_START);//设置顶部下拉
        listItem=getData();//获取LIST数据  
        adapter = new MyAdapter(this);

        //设置适配器  
        ListView actualListView = mPullRefreshListView.getRefreshableView();
        actualListView.setAdapter(adapter);

    }
    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        //后台处理部分  
        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {
            // Simulates a background job.  
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            ArrayList<HashMap<String,Object>> newArrayList = new ArrayList<HashMap<String,Object>>();
            HashMap<String, Object> map = new HashMap<String, Object>();
            try {

                map = new HashMap<String, Object>();
                map.put("name", "林珊");
                map.put("info", "上传了一张新照片油画");
                map.put("img","assets://7.png");
                newArrayList.add(0,map);
                newArrayList.add(0,map);

            } catch (Exception e) {
                // TODO: handle exception  adbe
                setTitle("map出错了");
                return null;
            }

            return newArrayList;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中  
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值  
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            //在头部增加新添内容  

            try {
                listItem.addAll(0,result);

                //通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合  
                adapter.notifyDataSetChanged();
                // Call onRefreshComplete when the list has been refreshed.  
                mPullRefreshListView.onRefreshComplete();
            } catch (Exception e) {
                // TODO: handle exception  
                setTitle(e.getMessage());
            }


            super.onPostExecute(result);
        }
    }

    private ArrayList<HashMap<String, Object>> getData() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        InputStream inputStream;
        try {
            inputStream=this.getAssets().open("my_friends.json");
            String json=readTextFile(inputStream);
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                map = new HashMap<String, Object>();
                map.put("name", array.getJSONObject(i).getString("name"));
                map.put("info", array.getJSONObject(i).getString("info"));
                map.put("img",array.getJSONObject(i).getString("photo"));
                list.add(map);
            }
            return list;

        } catch (Exception e) {
            // TODO: handle exception  
            e.printStackTrace();
        }


        return list;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView name;
        public TextView info;
    }

    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;

//        private ImageLoader imageLoader = new ImageLoader();

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listItem.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub  
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub  
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.list, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.name = (TextView)convertView.findViewById(R.id.name);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }
//            String urlString =(String) listItem.get(position).get("img");
//            Log.v("String url:" , urlString);
            imageLoader.displayImage((String)listItem.get(position).get("img"), holder.img,options,animateFirstListener);
//            holder.img.setImageBitmap(getHome((String)listItem.get(position).get("img")));
            holder.name.setText((String)listItem.get(position).get("name"));
            holder.info.setText((String)listItem.get(position).get("info"));


            return convertView;
        }

    }

    ////工具类  
    /**
     *
     * @param inputStream
     * @return
     */
    public String readTextFile(InputStream inputStream) {
        String readedStr = "";
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String tmp;
            while ((tmp = br.readLine()) != null) {
                readedStr += tmp;
            }
            br.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readedStr;
    }
    private void copyTestImageToSdCard(final File testImageOnSdCard) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = getAssets().open(TEST_FILE_NAME);
                    FileOutputStream fos = new FileOutputStream(testImageOnSdCard);
                    byte[] buffer = new byte[8192];
                    int read;
                    try {
                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                    } finally {
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                } catch (IOException e) {
                    L.w("Can't copy test image onto SD card");
                }
            }
        }).start();
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}  