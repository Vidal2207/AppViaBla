package com.ramkatom.myappviabla;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("CHAT");

    ConstraintLayout map;
    ConstraintLayout chat;
    FirebaseAuth auth;
    Button button;
    TextView textView,textCorreo;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imageView = findViewById(R.id.imageView7);
        imageView.setImageResource(R.drawable.lobo);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button3);
        textView = findViewById(R.id.textView3);
        textCorreo = findViewById(R.id.textView4);
        user = auth.getCurrentUser();
        /////REDONDEAR BOTON
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFF133343); // Cambia el color del botón
        gradientDrawable.setCornerRadius(30); // Cambia el radio de los bordes
        button.setBackground(gradientDrawable); // Establece el fondo del botón como el objeto GradientDrawable


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textCorreo.setText(user.getEmail());
            String userId = user.getUid(); // Obtén el ID único del usuario actual
            textView.setText(userId);
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId); // Obtén la referencia al nodo del usuario en la base de datos
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String usuario = dataSnapshot.child("usuario").getValue(String.class); // Obtén el nombre de usuario del dataSnapshot
                        String correo = dataSnapshot.child("correo").getValue(String.class); // Obtén el correo electrónico del dataSnapshot
                        textView.setText("Usuario: " + usuario);
                        textCorreo.setText("Correo: " + correo);
                    }
                    else{
                        //textView.setText("Usuario no encontrado");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    textView.setText("Error al obtener los datos: " + databaseError.getMessage());
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });


        map = findViewById(R.id.nav_bar_1);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Map.class);
                startActivity(intent);
                finish();
            }
        });

        chat = findViewById(R.id.nav_bar_2);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Chat.class);
                startActivity(intent);
                finish();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Este método se ejecutará cada vez que se cambie algo en el nodo.
                // dataSnapshot contiene los datos actualizados del nodo.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Este método se ejecutará si hay un error al obtener los datos.
            }
        });

    }
}