
package com.example.mobileguard.adapter;

import java.util.ArrayList;


import com.example.mobileguard.R;
import com.example.mobileguard.bean.Contactbean;
import com.example.mobileguard.db.ContactDao;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName:ContactListAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月8日 下午6:36:58 <br/>
 * 
 * @author dell
 * @version
 */
public class ContactListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contactbean> dates;

    public ContactListAdapter(Context context, ArrayList<Contactbean> dates) {
        this.context = context;
        this.dates = dates;
    }

    @Override
    public int getCount() {
        return dates == null ? 0 : dates.size();
    }

    @Override
    public Contactbean getItem(int position) {
        if (dates != null) {
            return dates.get(position);
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
            convertView = View.inflate(context, R.layout.contact_list_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.contact_list_item_iv);
            holder.name = (TextView) convertView.findViewById(R.id.contact_list_item_name);
            holder.number = (TextView) convertView.findViewById(R.id.contact_list_item_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Contactbean contactbean = dates.get(position);
        System.out.println(contactbean);
        System.out.println(holder.iv);
        System.out.println(holder.name);
        System.out.println(holder.number);
        Bitmap bitmap = ContactDao.getContacticon(context, contactbean.id);
        if (bitmap == null) {
            holder.iv.setImageResource(R.drawable.ic_contact);
        } else {
            holder.iv.setImageBitmap(bitmap);
        }
        holder.name.setText(contactbean.name);
        holder.number.setText(contactbean.number);
        return convertView;
    }

    class ViewHolder {
        ImageView iv;
        TextView name;
        TextView number;
    }
}
