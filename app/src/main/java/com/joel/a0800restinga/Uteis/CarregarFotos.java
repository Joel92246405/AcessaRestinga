package com.joel.a0800restinga.Uteis;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.Carrossel;

import java.util.ArrayList;
import java.util.List;

public class CarregarFotos {

    public List<Carrossel> mSliderItems = new ArrayList<Carrossel>();


    public CarregarFotos() {

    }

    public List<Carrossel> retornarFotos () {

             DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carrossel");

             for (int i = 0; i < 10; i++) {
                 databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         for (int i = 0; i < 2; i++) {
                             for (DataSnapshot d : snapshot.getChildren()) {

                                 Carrossel carrossel = new Carrossel();
                                 carrossel = d.getValue(Carrossel.class);
                                 mSliderItems.add(carrossel);
                             }
                         }


                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });

                 for (int j = 0; j < 500; j ++){
                     //nada
                 }

         }
        return mSliderItems;
    }
}
