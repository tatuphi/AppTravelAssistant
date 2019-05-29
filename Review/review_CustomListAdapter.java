package com.example.review1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class review_CustomListAdapter  extends BaseAdapter {

    private List<review_comment> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public review_CustomListAdapter(Context aContext,  List<review_comment> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.review_item_listview, null);
            holder = new ViewHolder();
            holder.iconView = (ImageView) convertView.findViewById(R.id.imageView_CmtIcon);
            holder.commentNameView = (TextView) convertView.findViewById(R.id.textView_CmtName);
            holder.commentView = (TextView) convertView.findViewById(R.id.textView_Comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        review_comment country = this.listData.get(position);
        holder.commentNameView.setText(country.getCommentName());
        holder.commentView.setText(country.getComment());
        int imageId = this.getMipmapResIdByName(country.getIconName());
        holder.iconView.setImageResource(imageId);

        return convertView;
    }

     //Tìm ID của Image ứng với tên của ảnh (Trong thư mục mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        ImageView iconView;
        TextView commentNameView;
        TextView commentView;
    }

}
