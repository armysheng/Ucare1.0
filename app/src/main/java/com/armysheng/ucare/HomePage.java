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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;

import com.armysheng.ucare.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class HomePage extends ListActivity {

    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private PullToRefreshListView mPullRefreshListView;
    MyAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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

        mPullRefreshListView.setMode(Mode.PULL_FROM_END);//设置底部下拉刷新模式  

        listItem=getData();//获取LIST数据  
        adapter = new MyAdapter(this);

        //设置适配器  
        ListView actualListView = mPullRefreshListView.getRefreshableView();
        actualListView.setAdapter(adapter);

    }
    private class GetDataTask extends AsyncTask<Void, Void, HashMap<String, Object>> {

        //后台处理部分  
        @Override
        protected HashMap<String, Object> doInBackground(Void... params) {
            // Simulates a background job.  
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            try {

                map = new HashMap<String, Object>();
                map.put("name", "林珊");
                map.put("info", "上传了一张新照片油画");
                map.put("img","360");

            } catch (Exception e) {
                // TODO: handle exception  
                setTitle("map出错了");
                return null;
            }

            return map;
        }

        //这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中  
        //根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值  
        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            //在头部增加新添内容  

            try {
                listItem.add(result);

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

            holder.img.setImageBitmap(getHome((String)listItem.get(position).get("img")));
            holder.name.setText((String)listItem.get(position).get("name"));
            holder.info.setText((String)listItem.get(position).get("info"));

            return convertView;
        }

    }


    /**
     * 根据图片名称获取主页图片 
     */
    public Bitmap getHome(String photo){
        String homeName = photo + ".png";
        InputStream is=null;

        try {
            is=getAssets().open("home/"+homeName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

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


}  