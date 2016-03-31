package com.chuangyuan.qdocument.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * 获取手机通讯录联系人工具类
 * Created by Administrator on 2016/3/23 0023.
 */
public class ContactUtils {

    public static class Contact {
        public String name;
        public String phone;
        public String email;
        public String address;
        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return "name = " + name + ",phone = " + phone + ",email = " + email + ",address = " + address;
        }
    }

    public static ArrayList<Contact> getAllContacts(Context context) {
        ArrayList<Contact> datas = null;
        Contact contact = null;
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        //查询第一个表raw_contacts，只关心contact_id这一列
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"contact_id"}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            datas = new ArrayList<ContactUtils.Contact>();
            while (cursor.moveToNext()) {
                //因为我的结果集里只有一列，所以这从0开始，也只有0这一列
                String contact_id = cursor.getString(0);

                //contact_id在用户删除联系人之后，这条记录并没有删除，只是将contact_id置为null
                if (contact_id != null) {
                    System.out.println("contact id = " + contact_id);
                    //如果contact_id不为null,才有必要创建一个javabean对象
                    contact = new Contact();
                    //查询第二张表的时候我只关心mimetype和data1这两列
                    Cursor datacursor = context.getContentResolver().query(dataUri, new String[]{"mimetype", "data1"},
                            "raw_contact_id = ?", new String[]{contact_id},
                            null);

                    //这个结果集存的是所有联系人的每个字段的信息，一个字段分了一行
                    if (datacursor != null && datacursor.getCount() > 0) {
                        while (datacursor.moveToNext()) {
                            //拿到了每个字段的type类型
                            String type = datacursor.getString(0);
                            //拿到了每个字段的值
                            String data = datacursor.getString(1);
                            if(TextUtils.isEmpty(data)){
                                continue;
                            }
                            //对类型做判断的目的是为了将对应的值赋值给javabean对象的相应属性
                            if (type.equals("vnd.android.cursor.item/name")) {
                                // 说明是name


                                contact.name = data;
                            } else if (type
                                    .equals("vnd.android.cursor.item/phone_v2")) {
                                // 说明是电话
                                contact.phone = data;
                            } else if (type
                                    .equals("vnd.android.cursor.item/postal-address_v2")) {
                                // 说明是地址
                                contact.address = data;

                            } else if (type
                                    .equals("vnd.android.cursor.item/email_v2")) {
                                // 说明是email
                                contact.email = data;
                            }
                        }
                        // 将datacursor关闭
                        datacursor.close();
                    }
                    //这个循环的目的仅仅是赋值，赋值完之后将javabean对象放到集合中
                    datas.add(contact);
                }

            }

            cursor.close();
        }

        return datas;

    }

}
