package com.ramkatom.myappviabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    ///ELEMENTOS USADOS EN LA VISTA GRÁFICA
    TextInputEditText editTextEmail, editTextPassword,editTextPassword2,editTextUser;
    Button btnRegister;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    ProgressBar progressBar;
    TextView textView;
    /// Cuando inicialices la actividad, verificar si el usuario ya accedió
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //SE ASIGNAN VALORES A VARIABLES PREVIAMENTE CREADAS
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);
        editTextUser = findViewById(R.id.name);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);
        /// INSTANCIA DE AUTENTIFICACIÓN DE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        //// CAMBIAR DE ACTIVIDAD AL LOGIN
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        /////REDONDEAR BOTON
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFF133343); // Cambia el color del botón
        gradientDrawable.setCornerRadius(30); // Cambia el radio de los bordes
        btnRegister.setBackground(gradientDrawable); // Establece el fondo del botón como el objeto GradientDrawable
        /// BOTÓN PARA REGISTRARSE EN LA APP
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);
                String email,password,password2,usuario;
                usuario = String.valueOf(editTextUser.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                password2 = String.valueOf(editTextPassword2.getText());
                if(email.isEmpty()){
                    Toast.makeText(Register.this, "Ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(Register.this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password == password2){
                    Toast.makeText(Register.this, "La contraseña es diferente", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<8){
                    Toast.makeText(Register.this, "Contraseña debe ser mayor a 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }


                // FUNCIÓN QUE CONECTA CON FIRE BASE PARA LA AUTENTIFICACIÓN
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                DatabaseReference mRef = mDatabase.getReference("usuarios").child(user.getUid());
                                mRef.child("usuario").setValue(usuario);
                                mRef.child("correo").setValue(email);

                                Toast.makeText(Register.this, "Cuenta creada",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                });
            }
        });
    }
}