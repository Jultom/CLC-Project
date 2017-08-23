package ro.artsoftconsult.myapplication.login;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;

import ro.artsoftconsult.myapplication.R;

public class AccountActivity extends AppCompatActivity {

    private TextView id;
    private TextView infoLabel;
    private TextView info;
    private static int loadImageResults=1;
    private ImageView profileImage;// ImageView
     private  Bitmap bmp;
    private ImageView profileImageNavHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        id = (TextView) findViewById(R.id.id_activity_account);
        infoLabel = (TextView) findViewById(R.id.info_label_activity_account);
        info = (TextView) findViewById(R.id.info_activity_account);
        profileImage = (ImageView)findViewById(R.id.profile_image_account);
     profileImageNavHeader= (ImageView)findViewById(R.id.profile_image_nav_header);

        accountActivity();


        profileImage.setOnClickListener(new OnClickListener(){
            public void onClick(View arg0){
             openGallery(); }
                });
                }

    private void openGallery() {
                       Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                  photoPickerIntent.setType("image/*");
                     startActivityForResult(photoPickerIntent, 1);
                                     }

    protected void onActivityResult(int requestCode, int resultcode, Intent intent)
{
super.onActivityResult(requestCode, resultcode, intent);

 if (requestCode == 1)
 {
 if (intent != null && resultcode == RESULT_OK)
  {

  Uri selectedImage = intent.getData();

  String[] filePathColumn = {MediaStore.Images.Media.DATA};
  Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
 cursor.moveToFirst();
 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
  String filePath = cursor.getString(columnIndex);
 cursor.close();
 if(bmp != null && !bmp.isRecycled()) {
     bmp = null;
 }
 bmp = BitmapFactory.decodeFile(filePath);
profileImage.setBackgroundResource(0);
profileImage.setImageBitmap(bmp);
//      profileImageNavHeader.setImageBitmap(bmp);

 }
else
{
 Log.d("Status:", "Photopicker canceled"); }
}
 }

    private void accountActivity() {
           AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                id.setText(accountKitId);

                PhoneNumber phoneNumber = account.getPhoneNumber();
                if (account.getPhoneNumber() != null) {
                    // if the phone number is available, display it
                    String formattedPhoneNumber = formatPhoneNumber(phoneNumber.toString());
                    info.setText(formattedPhoneNumber);
                    infoLabel.setText(R.string.phone_label);

                }
                else {
                    // if the email address is available, display it
                    String emailString = account.getEmail();
                    info.setText(emailString);
                    infoLabel.setText(R.string.email_label);

            }

            }

            @Override
            public void onError(final AccountKitError error) {
                // display error
                String toastMessage = error.getErrorType().getMessage();
                Toast.makeText(AccountActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }



    public void onLogout(View view) {
        // logout of Account Kit
        AccountKit.logOut();
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatPhoneNumber(String phoneNumber) {
        // helper method to format the phone number for display
        try {
            PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn = pnu.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        }
        catch (NumberParseException e) {
            e.printStackTrace();
        }
        return phoneNumber;
    }


}

