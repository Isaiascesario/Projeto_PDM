package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditarPerfil extends AppCompatActivity {
    private EditText emailEditarEditText;
    private EditText usuarioEditarEditText;
    private ImageView imagemEditarImagemView;
    private FirebaseAuth firebaseAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        emailEditarEditText = findViewById(R.id.emailEditarEditText);
        usuarioEditarEditText = findViewById(R.id.usuarioEditarEditText);
        imagemEditarImagemView = findViewById(R.id.imagemEditarImagemView);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void editarPerfilFinal(View view){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        final String nEmail = emailEditarEditText.getText().toString();
        final String nUsuario = usuarioEditarEditText.getText().toString();
        if(user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nUsuario.equals("") ? user.getDisplayName(): nUsuario)
                    .build();
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.updateEmail(nEmail.equals("") ? user.getEmail() : nEmail);
                            Toast.makeText(EditarPerfil.this, "Editado com sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditarPerfil.this, TelaPerfil.class));
                        }
                    });
        }
    }
    public void editarImagem(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagemEditarImagemView.setImageBitmap(imageBitmap);
        }
    }
}
