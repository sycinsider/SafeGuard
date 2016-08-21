
package com.example.mobileguard.db;

import java.io.InputStream;
import java.util.ArrayList;

import com.example.mobileguard.bean.Contactbean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * ClassName:ContactDao <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月8日 下午6:42:02 <br/>
 * 
 * @author dell
 * @version
 */
public class ContactDao {
    public static ArrayList<Contactbean> getAllContact(Context context) {
        ContentResolver cr = context.getContentResolver();
        // URI
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // 返回的列
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // 姓名
                ContactsContract.CommonDataKinds.Phone.NUMBER, // 号码
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID// raw_contact_id
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        ArrayList<Contactbean> dates = new ArrayList<Contactbean>();
        Cursor cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Contactbean contactbean = new Contactbean();
                contactbean.name = cursor.getString(0);
                contactbean.number = cursor.getString(1);
                contactbean.id = cursor.getString(2);
                dates.add(contactbean);
            }
            cursor.close();
        }
        return dates;
    }

    public static Bitmap getContacticon(Context context, String id) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id);
        InputStream inputStream =
                ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);

        return BitmapFactory.decodeStream(inputStream);

    }
}
