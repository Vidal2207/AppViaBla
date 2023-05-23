package com.ramkatom.myappviabla;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Chat extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CHAT");

    ConstraintLayout map;
    ConstraintLayout profile;

//    private View crearVistaMensaje(Mensaje mensaje) {
//        // Inflar el layout personalizado para el mensaje
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View vistaMensaje = inflater.inflate(R.layout.item_mensaje, null, false);
//
//        // Obtener referencias a las vistas dentro del layout
//        TextView tvUsuario = vistaMensaje.findViewById(R.id.tv_usuario);
//        TextView tvTexto = vistaMensaje.findViewById(R.id.tv_texto);
//        TextView tvFecha = vistaMensaje.findViewById(R.id.tv_fecha);
//
//        // Asignar los valores del mensaje a las vistas correspondientes
//        tvUsuario.setText(mensaje.getUsuario());
//        tvTexto.setText(mensaje.getTexto());
//        tvFecha.setText(mensaje.getFecha().toString());
//
//        // Devolver la vista personalizada
//        return vistaMensaje;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        map = findViewById(R.id.nav_bar_1);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
                finish();
            }
        });

        profile = findViewById(R.id.nav_bar_3);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        // Crear un objeto Mensaje
        Mensaje mensaje = new Mensaje("Juan", "Hola, ¿cómo estás?", new Date());

        // Enviar el objeto Mensaje a la base de datos
        myRef.push().setValue(mensaje);


        //myRef.addValueEventListener(new ValueEventListener() {
           /* @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Borrar los mensajes anteriores de la vista del chat
                ViewGroup chatView = findViewById(R.id.chat_view);
                chatView.removeAllViews();

                // Recorrer los mensajes recibidos y agregarlos a la vista del chat
                for (DataSnapshot mensajeSnapshot : dataSnapshot.getChildren()) {
                    Mensaje mensaje = mensajeSnapshot.getValue(Mensaje.class);
                    View mensajeView = crearVistaMensaje(mensaje);
                    chatView.addView(mensajeView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Este método se ejecutará si hay un error al obtener los datos.
            }
        });*/

    }
}