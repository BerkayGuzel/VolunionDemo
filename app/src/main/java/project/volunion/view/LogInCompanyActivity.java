package project.volunion.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import project.companyInfo.databinding.ActivityLogInCompanyBinding;
import project.volunion.util.PreferencesManagerInstance;
import project.volunion.util.Util;

public class LogInCompanyActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private ActivityLogInCompanyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        btnClick();

    }

    private void btnClick() {
        binding.loginCompanyBtn.setOnClickListener(login -> login());

        binding.forgotCompanyPassword.setOnClickListener(view -> {
            setViewForForgot();
        });

        binding.enterMailCompanyForgot.setOnClickListener(sendMail -> {
            sendMail();
        });
    }

    private void login() {

        String email = binding.mailCompanyLogIn.getText().toString();
        String password = binding.passwordCompanyLogIn.getText().toString();

        if (password.equals("12345678") && email.equals("kimsesizcocuklardernegi@gmail.com")) {

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(autResult -> {
                PreferencesManagerInstance.getInstance(this).setKurumBilgi(Util.COCUKLAR_KURUMU);
                Intent intent = new Intent(this, CompanyFeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Util.COCUKLAR_KURUMU, 0);
                startActivity(intent);
                finish();

            }).addOnFailureListener(exception -> {

                Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });

        } else if (password.equals("12345678") && email.equals("sokakhayvanseverleri@gmail.com")) {

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(autResult -> {

                PreferencesManagerInstance.getInstance(this).setKurumBilgi(Util.HAYVANLAR_KURUMU);
                Intent intent = new Intent(this, CompanyFeedActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Util.HAYVANLAR_KURUMU, 0);
                startActivity(intent);
                finish();

            }).addOnFailureListener(exception -> {

                Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });

        } else {

            Toast.makeText(this, "Hata! \n Lütfen Kurum üyelinizi kontrol edin", Toast.LENGTH_LONG).show();
        }
    }

    private void setViewForForgot() {
        binding.textInputLayout.setVisibility(View.GONE);
        binding.textInputLayout2.setVisibility(View.GONE);
        binding.loginCompanyBtn.setVisibility(View.GONE);
        binding.forgotCompanyPassword.setVisibility(View.GONE);
        binding.enterMailCompanyForgot.setVisibility(View.VISIBLE);
        binding.textInputLayoutForgot.setVisibility(View.VISIBLE);

    }

    private void sendMail() {
        String mail = binding.enterMailCompanyForgot.getText().toString();
        if (mail.isEmpty()) {
            Toast.makeText(this, "Lütfen doğru mail addresi giriniz", Toast.LENGTH_SHORT).show();
        } else {
            auth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(LogInCompanyActivity.this, "Mailinize gönderildi", Toast.LENGTH_SHORT).show();
                    binding.textInputLayout.setVisibility(View.VISIBLE);
                    binding.textInputLayout2.setVisibility(View.VISIBLE);
                    binding.loginCompanyBtn.setVisibility(View.VISIBLE);
                    binding.forgotCompanyPassword.setVisibility(View.VISIBLE);
                    binding.enterMailCompanyForgot.setVisibility(View.GONE);
                    binding.textInputLayoutForgot.setVisibility(View.GONE);
                }
            });
        }
    }


}