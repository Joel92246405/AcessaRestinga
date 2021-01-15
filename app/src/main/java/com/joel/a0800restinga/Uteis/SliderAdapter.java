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
import com.joel.a0800restinga.Model.CarrosselOffline;
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



    public SliderAdapter(Context context) {
        this.context = context;


        BancoController crud = new BancoController(context);
        Cursor cursor = crud.carregaDados();


        if(cursor!=null){
            cursor.moveToFirst();
            do {
                Carrossel carrossel= new Carrossel();

                carrossel.setDescricao(cursor.getString(0));       // definição do NOME retornado do cursor
                carrossel.setUrl(cursor.getString(1));      // definição do EMAIL retornado do cursor
                carrossel.setTelefone(cursor.getString(2));      // definição da SENHA retornado do cursor
                mSliderItems.add(carrossel);
            } while(cursor.moveToNext());
        }else{

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

                String telefone = sliderItem.getTelefone();

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra("jid", "55" + telefone + "@s.whatsapp.net");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Olá!");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                v.getContext().startActivity(sendIntent);
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
