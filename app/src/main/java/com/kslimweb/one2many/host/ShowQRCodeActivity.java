package com.kslimweb.one2many.host;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.kslimweb.one2many.R;
import com.kslimweb.one2many.utils.BuildTypeUtil;

import net.glxn.qrgen.android.QRCode;
import java.util.Objects;

import static com.kslimweb.one2many.host.SetHostActivity.INTENT_EXTRA_KEY;

public class ShowQRCodeActivity extends AppCompatActivity {

    // Event will become [One2Many-test] for development
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
        String topicNameString = intent.getStringExtra(INTENT_EXTRA_KEY);
        String finalQrValue;

        if (!BuildTypeUtil.isReleaseMode)
            finalQrValue = "One2Many-test";
        else
            finalQrValue =  "One2Many-" + topicNameString + "-" + generateRandomNumber();

        SUBSCRIBE_TOPIC = finalQrValue;

        Bitmap bitmap = QRCode.from(finalQrValue).bitmap();
        qrCode.setImageBitmap(bitmap);
        next.setOnClickListener(v -> startActivity(new Intent(ShowQRCodeActivity.this, SpeechToTextActivity.class)));
    }

    private String generateRandomNumber() {
        // give a range from 0 - 1000 generate random number
        int randomNumber = (int) ((Math.random() * ((1000) + 1)) + 0);
        return String.valueOf(randomNumber);
    }
}
