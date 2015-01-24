package com.armysheng.ucare;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends ListActivity {
    private static final String LOG_TAG ="LISTVIEW_TAG" ;
    private List<Map<String,Object>>  mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
//        SimpleAdapter adapter=new SimpleAdapter(this, getData(), R.layout.list,new String[]{"title","info","img"},
//                new int[]{R.id.title,R.id.info,R.id.img});
//        setListAdapter(adapter);
    }

    /*Create data HashMap List,the data is read from json files*/
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        InputStream inputStream ;
        try{
            inputStream = this.getAssets().open("myfriend.json");
            String json = readTextFile(inputStream);
            JSONArray jsonArray=new JSONArray(json);
            for (int i =0;i<jsonArray.length();i++) {
                map = new HashMap<String, Object>();
                map.put("name",jsonArray.getJSONObject(i).getString("name"));
                map.put("info",jsonArray.getJSONObject(i).get("info"));
                map.put("photo",jsonArray.getJSONObject(i).get("photo"));
                list.add(map);
                Log.v(LOG_TAG,map.get("name").toString());
            }
        } catch (Exception e) {
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
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null){
                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.name = (TextView)convertView.findViewById(R.id.name);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.img.setImageBitmap(getHome((String)mData.get(position).get("photo")));
            holder.name.setText((String)mData.get(position).get("name"));
            holder.info.setText((String)mData.get(position).get("info"));

            return convertView;
        }
    }

    private Bitmap getHome(String img) {
        String homeName = img + ".png";
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

    private String readTextFile(InputStream inputStream) {
        String readStr ="";
        BufferedReader br;
        try{
            br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String temp;
            while((temp=br.readLine())!=null){
                readStr +=temp;
            }
            br.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readStr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
