package com.example.rikki.afcodetest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

/**
 * Created by Rikki on 2016/12/27.
 */

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    private Context context;
    private ArrayList<CardItem> list;
    private ImageLoader loader;

    public CardAdapter(Context context, ArrayList<CardItem> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        CardHolder viewHolder = new CardHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        CardItem item = list.get(position);
        if(item.getTitle().isEmpty()){
            holder.title.setVisibility(View.GONE);
        } else{
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(item.getTitle());
        }
        if(item.getPromo().isEmpty()){
            holder.promo.setVisibility(View.GONE);
        } else{
            holder.promo.setVisibility(View.VISIBLE);
            holder.promo.setText(item.getPromo());
        }
        if(item.getTopDesc().isEmpty()){
            holder.topDesc.setVisibility(View.GONE);
        } else{
            holder.topDesc.setVisibility(View.VISIBLE);
            holder.topDesc.setText(item.getTopDesc());
        }
        if(item.getBottomDesc().isEmpty()){
            holder.bottomDesc.setVisibility(View.GONE);
        } else{
            holder.bottomDesc.setVisibility(View.VISIBLE);
            holder.bottomDesc.setText(item.getBottomDesc());
        }
        if(item.getLink().isEmpty()){
            holder.link.setVisibility(View.GONE);
        } else{
            holder.link.setVisibility(View.VISIBLE);
            holder.link.setClickable(true);
            holder.link.setMovementMethod(LinkMovementMethod.getInstance());
            holder.link.setText(Html.fromHtml(item.getLink(), FROM_HTML_MODE_COMPACT));
            final String linkAddress = item.getLink();

            holder.link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int start=0, end=0;
                    for(int i=0; i<linkAddress.length(); i++){
                        if(linkAddress.charAt(i)=='\\'&& linkAddress.charAt(i+1)=='\"'){
                            start = i+2;
                            break;
                        }
                    }
                    for(int i=start+1; i<linkAddress.length(); i++){
                        if(linkAddress.charAt(i)=='\\'&& linkAddress.charAt(i+1)=='\"'){
                            end = i;
                            break;
                        }
                    }
                    String linkAdd = linkAddress.substring(start, end); Log.d("LinkAddress",linkAdd);
                    Intent webIntent = new Intent(context,WebActivity.class);
                    webIntent.putExtra("address", linkAdd);
                    context.startActivity(webIntent);
                }
            });
        }
        if(!item.getImageUrl().isEmpty()){
            holder.image.setVisibility(View.VISIBLE);
            loader=AppController.getInstance().getImageLoader();
            loader.get(item.getImageUrl(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if(response.getBitmap()!=null){
                        holder.image.setImageBitmap(response.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ImageLoader " + holder.getAdapterPosition(), "Error: " + error.getMessage());
                }
            });
        } else{
            holder.image.setVisibility(View.GONE);
        }
        if(item.getList().isEmpty()){
            holder.contentView.setVisibility(View.GONE);
        } else{
            holder.contentView.setVisibility(View.VISIBLE);
            holder.contentView.removeAllViews();
            for(int i=0; i<item.getList().size(); i++){
                final int id = i;
                Button button = new Button(context);
                button.setText(item.getList().get(i).getTitle());
                button.setBackgroundColor(Color.YELLOW);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = list.get(position).getList().get(id).getTarget();
                        Intent webIntent = new Intent(context,WebActivity.class);
                        webIntent.putExtra("address", address);
                        context.startActivity(webIntent);
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
//                        context.startActivity(browserIntent);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(30, 0, 30, 10);
                holder.contentView.addView(button, params);
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class CardHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView title, promo, topDesc, bottomDesc, link;
    LinearLayout contentView;

    public CardHolder(View v) {
        super(v);
        image = (ImageView) v.findViewById(R.id.backgroundImage);
        title = (TextView) v.findViewById(R.id.title);
        promo = (TextView) v.findViewById(R.id.promo_msg);
        topDesc = (TextView) v.findViewById(R.id.top_desc);
        bottomDesc = (TextView) v.findViewById(R.id.bottom_desc);
        link = (TextView) v.findViewById(R.id.link);
        contentView = (LinearLayout) v.findViewById(R.id.content_view);
    }
}