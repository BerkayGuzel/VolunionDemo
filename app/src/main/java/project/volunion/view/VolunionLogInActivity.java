package project.volunion.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import project.companyInfo.databinding.ActivityVolunionLogInBinding;
import project.volunion.util.PreferencesManagerInstance;

public class VolunionLogInActivity extends AppCompatActivity {

    private ActivityVolunionLogInBinding binding;
    private FirebaseAuth auth;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVolunionLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth =FirebaseAuth.getInstance();
        clickBtn();
    }

    private void clickBtn(){
        binding.login.setOnClickListener(login->login());

        binding.forgotPassword.setOnClickListener(forgot->{
            forgot();
        });
    }

    private void forgot(){

        binding.textInputLayout.setVisibility(View.GONE);
        binding.textInputLayout1.setVisibility(View.GONE);
        binding.login.setVisibility(View.GONE);
        binding.forgotPassword.setVisibility(View.GONE);
        binding.sendEmail.setVisibility(View.VISIBLE);
        binding.textInputLayoutForgot.setVisibility(View.VISIBLE);



        binding.sendEmail.setOnClickListener(sendMail->{
            String email = binding.mailVolunionLogInForgot.getText().toString();
            if(email.isEmpty()){
                Toast.makeText(this, "Lütfen E-posta adresinizi giriniz", Toast.LENGTH_SHORT).show();
            }else {
                forgotPassword(email);
            }
        });


    }

    private void forgotPassword(String email){
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(VolunionLogInActivity.this, "Lütfen mailinizi kontrol edin!", Toast.LENGTH_SHORT).show();
                binding.textInputLayout.setVisibility(View.VISIBLE);
                binding.textInputLayout1.setVisibility(View.VISIBLE);
                binding.login.setVisibility(View.VISIBLE);
                binding.forgotPassword.setVisibility(View.VISIBLE);
                binding.sendEmail.setVisibility(View.GONE);
                binding.textInputLayoutForgot.setVisibility(View.GONE);
            }
        });
    }

    private void login(){

        String email = binding.mailVolunionLogIn.getText().toString();
        String password = binding.passwordVolunionLogIn.getText().toString();

        if(password.equals("") || email.equals("")){

            Toast.makeText(this, "Lütfen bilgilerinizi doğru giriniz", Toast.LENGTH_SHORT).show();

        }else {

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(autResult->{
                PreferencesManagerInstance.getInstance(this).setGonulluEmail(email);
                Intent intent = new Intent(this, VolunionFeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }).addOnFailureListener(exception->{

                Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }


}