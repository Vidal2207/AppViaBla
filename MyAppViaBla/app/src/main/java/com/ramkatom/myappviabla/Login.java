package com.ramkatom.myappviabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    /// Cuando inicialices la actividad, verificar si el usuario ya accedió
    private Dialog privacyDialog;
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
        setContentView(R.layout.activity_login);
        //SE ASIGNAN VALORES A VARIABLES PREVIAMENTE CREADAS
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);
        /// INSTANCIA DE AUTENTIFICACIÓN DE FIREBASE
        mAuth = FirebaseAuth.getInstance();

        /// CAMBIAR A LA ACTIVIDAD DE REGISTRO
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
        ////AVISO DE PRIVACIDAD
        //setContentView(R.layout.activity_login);
        // Inicializa el diálogo de privacidad
        privacyDialog = new Dialog(this);
        privacyDialog.setContentView(R.layout.dialog_privacy);

        // Obtén la referencia al TextView del aviso de privacidad
        TextView privacyTextView = findViewById(R.id.text_privacy);
        privacyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra la ventana emergente
                privacyDialog.show();
            }
        });

        // Configura el evento onClick del botón para cerrar la ventana emergente
        Button closeButton = privacyDialog.findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cierra la ventana emergente
                privacyDialog.dismiss();
            }
        });

        // Formato del texto del aviso de privacidad
        String privacyPolicyText = getString(R.string.privacy_notice);
        SpannableString spannableString = new SpannableString(privacyPolicyText);

        // Formato para el título "Política de privacidad"
        StyleSpan titleStyle = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(titleStyle, 0, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Formato para los párrafos
        StyleSpan paragraphStyle = new StyleSpan(Typeface.NORMAL);
        spannableString.setSpan(paragraphStyle, 19, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView privacyTextViewDialog = privacyDialog.findViewById(R.id.text_privacy_dialog);
        privacyTextViewDialog.setText(spannableString);

        // Establece el tamaño del diálogo
        Window window = privacyDialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
        ///QUITAR SALTOS DE LINEA
        editTextEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String txtUser = editTextEmail.getText().toString();
                txtUser.replaceAll("\n", "");
                editTextEmail.setText(txtUser);
                return true;
            }
        });
        editTextPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String txtUser = editTextPassword.getText().toString();
                txtUser.replaceAll("\n", "");
                editTextPassword.setText(txtUser);
                return true;
            }
        });
        /////MOSTRAR CONTRASEÑA
        CheckBox showPasswordCheckbox = findViewById(R.id.show_password_checkbox);
        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        /////

        /////REDONDEAR BOTON
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(0xFF133343); // Cambia el color del botón
        gradientDrawable.setCornerRadius(30); // Cambia el radio de los bordes
        btnLogin.setBackground(gradientDrawable); // Establece el fondo del botón como el objeto GradientDrawable

        //LOGUEARSE
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (email.isEmpty()) {
                    Toast.makeText(Login.this, "Ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 3000);
                    return;
                }
                if (!email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
                    // El correo electrónico no es válido
                    Toast.makeText(Login.this, "Ingrese un correo electrónico válido de Gmail", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 3000);
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(Login.this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 3000);
                    return;
                }
                // FUNCIÓN PARA LOGIN CON FIREBASE
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Autentificación exitosa", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 2000);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }
                    }
                });
            }
        });
    }
}