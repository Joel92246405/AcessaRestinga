package com.joel.a0800restinga.Uteis;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.joel.a0800restinga.Model.Carrossel;
import com.joel.a0800restinga.Model.SliderItem;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.Users;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    final private Context context;
    private List<Carrossel> mSliderItems = new ArrayList<Carrossel>();
    DatabaseReference databaseReference;

    public SliderAdapter(Context context, List<Carrossel> itemFotos) {
        this.context = context;
        //Cria o nó storage

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
/*
            if (! itemFotos.isEmpty())
                mSliderItems = itemFotos;
*/
            //CarregarFotos carregarFotos = new CarregarFotos();
            //mSliderItems = carregarFotos.retornarFotos();

            Carrossel sliderItem = new Carrossel();
            sliderItem.setUrl("https://firebasestorage.googleapis.com/v0/b/telefones-c6ce7.appspot.com/o/zapzap.png?alt=media&token=95eaffb1-413f-46e0-a646-e1603ccf11d5");
            sliderItem.setDescricao("Google");
            mSliderItems.add(sliderItem);

        }else {

            Carrossel sliderItem = new Carrossel();
            sliderItem.setUrl("https://firebasestorage.googleapis.com/v0/b/telefones-c6ce7.appspot.com/o/zapzap.png?alt=media&token=95eaffb1-413f-46e0-a646-e1603ccf11d5");
            sliderItem.setDescricao("Google");
            mSliderItems.add(sliderItem);
            sliderItem = new Carrossel();
            sliderItem.setUrl("https://www.google.com/imgres?imgurl=https%3A%2F%2Fwww.abioptica.com.br%2Fwp-content%2Fuploads%2F2020%2F08%2FGoogle_Buscador.png&imgrefurl=https%3A%2F%2Fwww.abioptica.com.br%2Fe-assim-que-sua-otica-usa-o-google-para-vender-mais%2F&tbnid=nXr_5bg2fzqkkM&vet=12ahUKEwjJ2IXy0ZvuAhUCG7kGHTLMAf8QMygFegUIARCzAQ..i&docid=uIXkZveBSUvjKM&w=1280&h=720&q=google&ved=2ahUKEwjJ2IXy0ZvuAhUCG7kGHTLMAf8QMygFegUIARCzAQ");
            sliderItem.setDescricao("Google 2");
            mSliderItems.add(sliderItem);
        }
    }



    public void renewItems(List<Carrossel> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(Carrossel sliderItem) {

            this.mSliderItems.add(sliderItem);
            notifyDataSetChanged();

    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final Carrossel sliderItem = mSliderItems.get(position);

        viewHolder.textViewDescription.setText(sliderItem.getDescricao());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.BLACK);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, sliderItem.getTelefone(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size

        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        ImageView imageGifContainer;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
