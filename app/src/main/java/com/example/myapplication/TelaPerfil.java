package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class TelaPerfil extends AppCompatActivity {
    private TextView emailTextView;
    private TextView usuarioTextView;
    private ImageView imgPerfilImageView;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);
        emailTextView = findViewById(R.id.emailTextView);
        usuarioTextView = findViewById(R.id.usuarioTextView);
        imgPerfilImageView = findViewById(R.id.imgPerfilImageView);
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void editarPerfil(View view){
        startActivity(new Intent(this,EditarPerfil.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            usuarioTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            Uri imgUri = user.getPhotoUrl();
            if(imgUri != null)
                imgPerfilImageView.setImageURI(imgUri);
        }
        else
            Toast.makeText(this, "Erro ao Pegar Usuario", Toast.LENGTH_SHORT).show();

    }
}
