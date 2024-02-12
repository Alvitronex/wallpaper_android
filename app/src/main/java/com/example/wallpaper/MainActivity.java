package com.example.wallpaper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap selectedImage;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            try {
                // obtiene la imagen seleccionada
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                // muestra la imagen en el imageview
                imageView.setImageBitmap(bitmap);
                // guarda la imagen seleccionada
                selectedImage = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setWallpaper(View view) {
        if (selectedImage != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Está seguro de establecer esta imagen como fondo de pantalla?")
                    //se argumenta si le parece proceder la accion
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // establece la imagen como fondo de pantalla
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                            try {
                                wallpaperManager.setBitmap(selectedImage);
                                // mensaje-aviso si se realizo el metodo
                                Toast toast = Toast.makeText(getApplicationContext(), "successfully", Toast.LENGTH_SHORT);
                                toast.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    // cancela la accion
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
}