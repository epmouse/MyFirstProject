package com.chuangyuan.qdocument.pager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.chuangyuan.qdocument.R;
import com.chuangyuan.qdocument.utils.ContactUtils;
import com.chuangyuan.qdocument.utils.LogUtils;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class SecondPager extends BasePager {

    private ListView contact_list;
    private Button btn_getContact;
    //private ArrayList<String> allContacts;
    private ArrayList<ContactUtils.Contact> allContacts;

    public SecondPager(Context context) {
        super(context);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            allContacts = (ArrayList<ContactUtils.Contact>) msg.obj;
            for(ContactUtils.Contact contact :allContacts){
            LogUtils.logInfoStar(contact.toString());
            }
            adapter = new contactListAdapter();
            contact_list.setAdapter(adapter);

        }
    };
    @Override
    public View initView() {

        View view = View.inflate(context, R.layout.secondpager_layout, null);
        contact_list= (ListView) view.findViewById(R.id.contact_list);
        btn_getContact= (Button) view.findViewById(R.id.getContact);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<ContactUtils.Contact>  allContacts = ContactUtils.getAllContacts(context);
//
//                Message msg=Message.obtain();
//                msg.obj=allContacts;
//                msg.what=88;
//                handler.sendMessage(msg);
//
//            }
//        }).start();
        return view;
    }

    private contactListAdapter adapter;
    class contactListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(allContacts!=null&&allContacts.size()>0){
            return allContacts.size();
            }else{
                return 0;
            }
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
            viewHolder vh=null;
            if(convertView==null){
            convertView=View.inflate(context,R.layout.main_title_layout,null);
                vh=new viewHolder(convertView);
                convertView.setTag(vh);
            }else{
                vh= (viewHolder) convertView.getTag();
            }

            ContactUtils.Contact contactInfo = allContacts.get(position);
            vh.contactName.setText(contactInfo.name);
            vh.contactPhone.setText(contactInfo.phone);
            vh.addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO-执行添加好友的操作。
                }
            });


            return convertView;
        }
    }

    class viewHolder{
        TextView contactName,contactPhone;
        Button addFriend;
        public viewHolder(View convertView){
             contactName= (TextView) convertView.findViewById(R.id.tv_contactName);
            contactPhone= (TextView) convertView.findViewById(R.id.tv_contactPhone);
            addFriend= (Button) convertView.findViewById(R.id.addFriend);
        }
    }
}
