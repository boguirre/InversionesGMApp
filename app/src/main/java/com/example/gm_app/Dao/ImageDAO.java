package com.example.gm_app.Dao;

import android.content.Context;

import com.example.gm_app.utils.CompresorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageDAO {

    StorageReference mStorage;

    public ImageDAO() {
        mStorage = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask save(Context context, File file){
        byte[] ImageByte = CompresorBitmapImage.getImage(context, file.getPath(),  500, 500);
        StorageReference storage = FirebaseStorage.getInstance().getReference().child(new Date() + ".jpg");
        mStorage = storage;
        UploadTask task = storage.putBytes(ImageByte);
        return task;
    }

    public StorageReference getStorage(){
        return mStorage;
    }


}
