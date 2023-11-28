package account.fpoly.s_shop_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ContactUsActivity extends AppCompatActivity {
    TextView contact_phone, contact_email,contact_facebook;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        contact_phone = findViewById(R.id.contact_phone);
        contact_email = findViewById(R.id.contact_email);
        contact_facebook = findViewById(R.id.contact_facebook);

        contact_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenumber = contact_phone.getText().toString();
                if (!phonenumber.isEmpty()) {
                    // Use the ACTION_CALL intent to initiate a phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenumber));

                    try {
                        startActivity(callIntent);
                    } catch (SecurityException e) {
                        // Handle the exception, for example, request the CALL_PHONE permission
                        Toast.makeText(ContactUsActivity.this, "Permission to make a phone call is not granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the phone number is empty
                    Toast.makeText(ContactUsActivity.this, "Phone number is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        contact_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = contact_email.getText().toString().trim();

                if (!emailAddress.isEmpty()) {
                    // Use the ACTION_SENDTO intent to open the email app with the specified email address
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));

                    // You can add subject and body if needed
                    // emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    // emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email"));
                    } catch (Exception e) {
                        // Handle the exception, for example, show a message to the user
                        Toast.makeText(ContactUsActivity.this, "No email app installed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the email address is empty
                    Toast.makeText(ContactUsActivity.this, "Email address is empty", Toast.LENGTH_SHORT).show();
                }
            }


        });
        contact_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define the URL you want to open
                String url = "https://dribbble.com/shots/22649434-Contact-Us-Screen";

                // Use an Intent to open the URL in a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                // Start the activity
                startActivity(intent);
            }
        });



    }
}