package com.joel.a0800restinga.Uteis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.joel.a0800restinga.Model.Carrossel;
import com.joel.a0800restinga.Model.CarrosselOffline;
import com.joel.a0800restinga.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterOffline extends SliderViewAdapter<SliderAdapterOffline.SliderAdapterVH> {

    private Context context;

    private List<CarrosselOffline> mSliderItemsOffline = new ArrayList<CarrosselOffline>();
    private boolean isConnected;
    DatabaseReference databaseReference;

    public SliderAdapterOffline(Context context) {
        this.context = context;



        CarrosselOffline carrossel= new CarrosselOffline();
        carrossel.setUrl(R.drawable.compartilha);
        carrossel.setDescricao("Você pode compartilhar o contato com seu amigo que pediu e ainda não tem o aplicativo!");
        carrossel.setTelefone("");
        carrossel.setWhatsapp("N");
        mSliderItemsOffline.add(carrossel);

        carrossel= new CarrosselOffline();
        carrossel.setUrl(R.drawable.whatsapp);
        carrossel.setDescricao("Agora, caso você tenha o contato adcionado no telefone, pode enviar mensagem pelo WhatsApp!");
        carrossel.setTelefone("");
        carrossel.setWhatsapp("N");
        mSliderItemsOffline.add(carrossel);

        carrossel= new CarrosselOffline();
        carrossel.setUrl(R.drawable.ligacao);
        carrossel.setDescricao("Ao clicar no link do telefone, você ja faz a chada para o contato de forma simples e rápida!");
        carrossel.setTelefone("");
        carrossel.setWhatsapp("N");
        mSliderItemsOffline.add(carrossel);
    }



    public void renewItems(List<CarrosselOffline> sliderItems) {
        this.mSliderItemsOffline = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItemsOffline.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(CarrosselOffline sliderItem) {

            this.mSliderItemsOffline.add(sliderItem);
            notifyDataSetChanged();

    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final CarrosselOffline sliderItem = mSliderItemsOffline.get(position);

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

                //Toast.makeText(context, sliderItem.getTelefone(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size

        return mSliderItemsOffline.size();
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
