package com.example.administrator.cardviewtset.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.cardviewtset.ItemActivity;
import com.example.administrator.cardviewtset.MainActivity;
import com.example.administrator.cardviewtset.R;
import com.example.administrator.cardviewtset.RecyclerItem;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private List<RecyclerItem> list_items;
    private Context context;

    public RecyclerAdapter(List<RecyclerItem> items) {
        this.list_items = items;
    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerlayout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerItem recyclerItem = list_items.get(holder.getAdapterPosition());
                Intent intent = new Intent(context,ItemActivity.class);
                intent.putExtra(ItemActivity.NAME,recyclerItem.getName());
                intent.putExtra(ItemActivity.ID,recyclerItem.getImage_ID());
                intent.putExtra(ItemActivity.DES,recyclerItem.getDescription());
                intent.putExtra(ItemActivity.PATH_ID,recyclerItem.getPATH_ID());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {//UI更新
        RecyclerItem recyclerItem = list_items.get(position);
        holder.item_Text.setText(list_items.get(position).getName());
        if("http://tnfs.tngou.net/image/top/default.jpg".equals(recyclerItem.getImage_ID())){//更换服务器返回的默认图片
            Glide.with(context).load(MainActivity.get_Image_ID()).into(holder.item_Image);
        }else{
            Glide.with(context).load(recyclerItem.getImage_ID()).into(holder.item_Image);
        }
        //Glide 开源控件，图片加载缓存，防止内存溢出
    }

    @Override
    public int getItemCount() {
        return list_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_Image;
        private TextView item_Text;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view_recycler);
            item_Image = (ImageView) itemView.findViewById(R.id.item_image);
            item_Text = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
