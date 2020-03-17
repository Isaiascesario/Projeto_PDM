package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "EmailLogin" ;
    private static final String PERMITIDO = "CheckBox";
    private FirebaseAuth firebaseAuth;
    private EditText loginEditText;
    private EditText senhaEditText;
    private boolean lembrarMe = false;
    private CheckBox lembrarCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginEditText = findViewById(R.id.loginEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        lembrarCheckBox = findViewById(R.id.lembraCheckBox);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String eValue = settings.getString("Email_Login", "");
        SharedPreferences settings2 = getSharedPreferences(PERMITIDO, 0);
        String eValue2 = settings2.getString("Check_Box", "NO");
        if(eValue2.equals("SIM"))
            lembrarCheckBox.setChecked(true);
        loginEditText.setText(eValue);
    }

    public void efetuarLogin(View view){
        String login = loginEditText.getText().toString();
        String senha = senhaEditText.getText().toString();
        lembrarMe = lembrarCheckBox.isChecked();

        if(!login.equals("")  && !senha.equals("")) {

            firebaseAuth.signInWithEmailAndPassword(login, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(MainActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                        if(!lembrarMe){
                            loginEditText.setText("");
                        }
                        senhaEditText.setText("");
                        startActivity(new Intent(MainActivity.this, TelaPrincipal.class));

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(MainActivity.this, R.string.falhaAoLogar,
                                Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
            });
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(lembrarMe){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Email_Login", loginEditText.getText().toString());
            editor.apply();
            SharedPreferences settings2 = getSharedPreferences(PERMITIDO, 0);
            editor = settings2.edit();
            editor.putString("Check_Box", "SIM");
            editor.apply();
        }
        else{
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Email_Login", "");
            editor.apply();
            SharedPreferences settings2 = getSharedPreferences(PERMITIDO, 0);
            editor = settings2.edit();
            editor.putString("Check_Box", "NAO");
            editor.apply();
        }
    }



    public void cadastrarUsuario(View view){
        startActivity(new Intent(MainActivity.this, CadastroUsuario.class));
    }
}
