package com.kslimweb.one2many.host;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.kslimweb.one2many.R;
import net.glxn.qrgen.android.QRCode;
import java.util.Objects;

public class ShowQRCodeActivity extends AppCompatActivity {

    public static String SUBSCRIBE_TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_show_qr_code);

        ImageView qrCode = findViewById(R.id.qr_code_image);
        Button next = findViewById(R.id.next);

        Intent intent = getIntent();
        String classNameString = intent.getStringExtra("CLASS_NAME");
        String topicNameString = intent.getStringExtra("TOPIC_NAME");

        String finalQrValue = classNameString + "-" + topicNameString;
        SUBSCRIBE_TOPIC = finalQrValue;

        Bitmap bitmap = QRCode.from(finalQrValue).bitmap();
        qrCode.setImageBitmap(bitmap);
        next.setOnClickListener(v -> startActivity(new Intent(ShowQRCodeActivity.this, SpeechToTextActivity.class)));
    }
}
