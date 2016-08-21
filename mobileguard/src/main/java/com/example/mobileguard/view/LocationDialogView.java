
package com.example.mobileguard.view;

import java.util.ArrayList;

import com.example.mobileguard.R;
import com.example.mobileguard.bean.LocationThemeBean;
import com.example.mobileguard.utils.Constantset;
import com.example.mobileguard.utils.SpUtils;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ClassName:LocationDialogView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月14日 下午4:01:29 <br/>
 * 
 * @author dell
 * @version
 */
public class LocationDialogView extends Dialog implements OnItemClickListener {
    private Context context;
    private ListView lv;
    private String[] titles = new String[] {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿",};
    private int[] icons = new int[] {R.drawable.location_bg1, R.drawable.location_bg2,
            R.drawable.location_bg3, R.drawable.location_bg4, R.drawable.location_bg5,};
    private ArrayList<LocationThemeBean> datas = new ArrayList<LocationThemeBean>();

    public LocationDialogView(Context context) {
        super(context, R.style.LocationDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_theme_dialog);
        LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lv = (ListView) findViewById(R.id.location_theme_lv);
        lv.setAdapter(new ThemeAdapter());
        lv.setOnItemClickListener(this);
        int saveIcon = SpUtils.getInt(context, Constantset.LOCATION_THEME);
        for (int i = 0; i < icons.length; i++) {
            LocationThemeBean bean = new LocationThemeBean();
            bean.name = titles[i];
            bean.icon = icons[i];
            if (bean.icon == saveIcon) {
                bean.status = true;
            } else {
                bean.status = false;
            }
            datas.add(bean);
        }
    }

    class ThemeAdapter extends BaseAdapter {

        private ImageView icon;
        private TextView tv;
        private ImageView status;

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public LocationThemeBean getItem(int position) {
            if (datas != null) {
                return datas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.location_theme_item, null);
                icon = (ImageView) convertView.findViewById(R.id.location_theme_item_icon);
                tv = (TextView) convertView.findViewById(R.id.location_theme_item_tv);
                status = (ImageView) convertView.findViewById(R.id.location_theme_item_status);
                holder.icon = icon;
                holder.tv = tv;
                holder.status = status;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            LocationThemeBean bean = datas.get(position);
            holder.icon.setImageResource(bean.icon);
            holder.tv.setText(bean.name);
            holder.status.setVisibility(bean.status ? View.VISIBLE : View.GONE);
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView tv;
            ImageView status;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LocationThemeBean bean = datas.get(position);
        SpUtils.putInt(context, Constantset.LOCATION_THEME, bean.icon);
        dismiss();
    }
}
