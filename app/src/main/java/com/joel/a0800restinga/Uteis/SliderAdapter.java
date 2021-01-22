package com.joel.a0800restinga.Uteis;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joel.a0800restinga.Model.Carrossel;
import com.joel.a0800restinga.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    final private Context context;
    private List<Carrossel> mSliderItems = new ArrayList<Carrossel>();



    public SliderAdapter(Context context) {
        this.context = context;


        try {
            BancoController crud = new BancoController(context);
            Cursor cursor = crud.carregaDados();


            if (cursor.moveToFirst()) {

                do {
                    Carrossel carrossel = new Carrossel();
                    carrossel.setDescricao(cursor.getString(0));       // definição do NOME retornado do cursor
                    carrossel.setUrl(cursor.getString(1));      // definição do EMAIL retornado do cursor
                    carrossel.setTelefone(cursor.getString(2));      // definição da SENHA retornado do cursor
                    carrossel.setWhatsapp(cursor.getString(3));
                    mSliderItems.add(carrossel);
                } while (cursor.moveToNext());
            } else {

            }
        }catch (Exception ex)
        {
            Log.d("CARROSSEL_SA_ON", ex.toString());
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

                //Toast.makeText(context, sliderItem.getTelefone(), Toast.LENGTH_SHORT).show();

                if (sliderItem.getWhatsapp().toString().equals("S")) {
                    //ZAPZAP
                    String telefone = sliderItem.getTelefone();
                    if (!sliderItem.getTelefone().equals("0")) {
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.putExtra("jid", "55" + telefone + "@s.whatsapp.net");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Olá. Estou entrando em contato pois peguei o contato no App Acessa Restinga!");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setPackage("com.whatsapp");
                        sendIntent.setType("text/plain");
                        v.getContext().startActivity(sendIntent);
                    }
                }else{
                    //LIGAR
                    String telefone = sliderItem.getTelefone();
                    Uri uri = Uri.parse("tel:"+telefone);
                    Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                    v.getContext().startActivity(intent);
                }
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
