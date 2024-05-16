package com.example.nossenshniyami.Creditcard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nossenshniyami.FirebaseHelper.FirebaseHelper;
import com.example.nossenshniyami.MainActivity;
import com.example.nossenshniyami.R;
import com.example.nossenshniyami.Reposetory.Reposetory;
import com.google.firebase.auth.FirebaseAuth;

public class CreditCardActivity extends AppCompatActivity implements View.OnClickListener {
private EditText editTextFullName,editTextEmail,editTextPhoneNumber,editTextCardNumber,editTextExpiryDate,editTextCVC;
private Button btnAddCredit;

FirebaseHelper firebaseHelper = new FirebaseHelper(this);
private static boolean creditOnff=false;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        editTextFullName=findViewById(R.id.editTextFullName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPhoneNumber=findViewById(R.id.editTextPhoneNumber);
        editTextCardNumber=findViewById(R.id.editTextCardNumber);
        editTextExpiryDate=findViewById(R.id.editTextExpiryDate);
        editTextCVC=findViewById(R.id.editTextCVC);
        mAuth = FirebaseAuth.getInstance();
        btnAddCredit=findViewById(R.id.btnAddCredit);

        editTextEmail.setText(mAuth.getCurrentUser().getEmail());


btnAddCredit.setOnClickListener(this);

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Fetching User");
        pd.setCancelable(false);
        pd.show();
        Reposetory reposetory =new Reposetory(firebaseHelper,this);
        reposetory.GetInfo(mAuth, new FirebaseHelper.userFound() {
            @Override
            public void onUserFound(String name, String phone) {
                editTextFullName.setText(name);
                editTextPhoneNumber.setText(phone);
                pd.dismiss();
            }
        });

        if (editTextPhoneNumber.getText().toString()==null)
        {
            Intent intent =new Intent(CreditCardActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v)
    {

     if (v==btnAddCredit)
     {
//
////      ד
         if (editTextFullName.getText().toString().isEmpty())
         {
             editTextFullName.setError("נא למלא שם מלא");
             return;
         }

         if (editTextPhoneNumber.getText().toString().length()==0)
         {
             editTextPhoneNumber.setError("נא למלא טלפון ");
             return;
         }

         if (editTextCardNumber.getText().toString().length()==0)
         {
             editTextCardNumber.setError("נא למלא מספר כרטיס");
             return;
         }
         if (editTextCardNumber.getText().toString().length()!=16)
         {
             editTextCardNumber.setError("כרטיס לא תקין");
             return;
         }
         if (editTextExpiryDate.getText().length()==0)
         {
             editTextExpiryDate.setError("נא למלא תוקף");
             return;
         }
         if(editTextExpiryDate.getText().toString().indexOf('/')!=2)
         {
             editTextExpiryDate.setError("תוקף לא תקין");
             return;
         }

         if (editTextCVC.getText().toString().length()==0)
         {
             editTextCVC.setError("נא למלא CVC");
             return;
         }
         if (editTextCVC.getText().toString().length()!=3)
         {
             editTextCVC.setError("צריך להיות 3 ספרות CVC");
             return;
         }

         creditOnff=true;
         Intent intent = new Intent(CreditCardActivity.this,MainActivity.class);
         intent.putExtra("creditonff",creditOnff);
         startActivity(intent);
     }
//if(v==btnAddCredit)
//{
//    creditOnff=true;
//}
    }
}
