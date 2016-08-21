
package com.example.mobileguard.adapter;

import java.util.zip.Inflater;

import com.example.mobileguard.R;

import android.content.Context;
import android.view.View;

import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName:GridAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月5日 下午11:20:39 <br/>
 * 
 * @author dell
 * @version
 */
public class GridAdapter extends BaseAdapter {
    private final static String[] TITLES =
            new String[] {"手机防盗", "骚扰拦截", "软件管家", "进程管理", "流量统计", "手机杀毒", "缓存清理", "常用工具"};
    private final static String[] DESCS = new String[] {"远程定位手机", "全面拦截骚扰", "管理您的软件", "管理运行进程",
            "流量一目了然", "病毒无处藏身", "系统快如火箭", "工具大全"};
    private final static int[] ICONS = new int[] {R.drawable.sjfd, R.drawable.srlj, R.drawable.rjgj,
            R.drawable.jcgl, R.drawable.lltj, R.drawable.sjsd, R.drawable.hcql, R.drawable.cygj};
    private Context context;

    public GridAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ICONS.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.grid_item, null);
        }
        ImageView iv = (ImageView) convertView.findViewById(R.id.grid_item_iv);
        TextView title = (TextView) convertView.findViewById(R.id.grid_item_title);
        TextView desc = (TextView) convertView.findViewById(R.id.grid_item_desc);
        iv.setImageResource(ICONS[position]);
        title.setText(TITLES[position]);
        desc.setText(DESCS[position]);
        return convertView;
    }

}
