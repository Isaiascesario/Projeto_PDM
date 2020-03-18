package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CadastroUsuario extends AppCompatActivity {
    private static final int IMAGEM_INTERNA = 12;
    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private ImageView imagemCadastroImagemView;
    private EditText emailEditText;
    private EditText usuarioEditText;
    private EditText senhaCadastroEditText;
    private EditText senhaReCadastroEditText;
    private FirebaseAuth firebaseAuth;
    private Bitmap imgCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        imagemCadastroImagemView = findViewById(R.id.imagemCadastroImagemView);
        emailEditText = findViewById(R.id.emailEditText);
        usuarioEditText = findViewById(R.id.usuarioEditText);
        senhaCadastroEditText = findViewById(R.id.senhaCadastroEditText);
        senhaReCadastroEditText = findViewById(R.id.senhaReCadastroEditText);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void terminarCadastro(View view) throws  IOException{
        String senha = senhaCadastroEditText.getText().toString();
        String senhaRe = senhaReCadastroEditText.getText().toString();
        final String usuario = usuarioEditText.getText().toString();
        String email = emailEditText.getText().toString();
        boolean cadastroOk = true;
        if(senha.equals("")){
            Toast.makeText(this, R.string.digiteUmaSenha, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(senhaRe.equals("")){
            Toast.makeText(this, R.string.digiteUmaSenha, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(usuario.equals("")){
            Toast.makeText(this, R.string.digiteUmUsuario, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(email.equals("")){
            Toast.makeText(this, R.string.digiteUmEmail, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(!email.startsWith(".com") && !email.startsWith("@") && !email.contains("@") || !email.contains(".com")){
            Toast.makeText(this, R.string.digiteUmEmailValido, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(!senha.equals(senhaRe)){
            Toast.makeText(this, R.string.senhasNaoBatem, Toast.LENGTH_SHORT).show();
            cadastroOk = false;
        }
        if(cadastroOk){
            firebaseAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = null;
                        try {
                            profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usuario)
                                    .setPhotoUri(getImageUri(imgCadastro))
                                    .build();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(user != null)
                          user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CadastroUsuario.this, "Cadastro Efetuado Com Sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CadastroUsuario.this,MainActivity.class));
                                    }
                                });

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(CadastroUsuario.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

    public void pegarImg(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imgCadastro = (Bitmap) extras.get("data");
            imagemCadastroImagemView.setImageBitmap(imgCadastro);
        }
    }

    public Uri getImageUri(Bitmap inImage) throws IOException {
        String FILENAME = "image.png";
        String PATH = "/mnt/sdcard/"+ FILENAME;
        File f = new File(PATH);
        return Uri.fromFile(f);
    }
}
