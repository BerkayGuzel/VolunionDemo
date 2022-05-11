package project.volunion.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import project.companyInfo.R;
import project.companyInfo.databinding.ActivityVolunionSignUpBinding;
import project.volunion.util.PreferencesManagerInstance;
import project.volunion.util.Util;

public class VolunionSignUpActivity extends AppCompatActivity {

    private ActivityVolunionSignUpBinding binding;

    private String selectedCities, selectedTowns, selectedCompany;
    private ArrayAdapter<String> citiesAdapter, townAdapter, companyAdapter;
    private ArrayList<String> citiesList, townListIzmir, townListIstanbul, companyList;

    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVolunionSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAdapter();
        initFirebase();

        auth = FirebaseAuth.getInstance();
        clickBtn();
    }

    private void initFirebase() {
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    private void initAdapter() {
        citiesList = new ArrayList<>();
        citiesList.add("İzmir");
        citiesList.add("İstanbul");

        citiesAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, citiesList);
        binding.spinnerCity.setAdapter(citiesAdapter);

        townListIzmir = new ArrayList<>();
        townListIzmir.add("Buca");
        townListIzmir.add("Bornova");
        townListIzmir.add("Bostanlı");
        townListIzmir.add("Karşıyaka");

        townListIstanbul = new ArrayList<>();
        townListIstanbul.add("Beşiktaş");
        townListIstanbul.add("Kadıköy");
        townListIstanbul.add("Bebek");
        townListIstanbul.add("Şişli");

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position == 0) {

                    townAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_layout, townListIzmir);
                }
                if (position == 1) {
                    townAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_layout, townListIstanbul);
                }

                binding.spinnerTown.setAdapter(townAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        companyList = new ArrayList<>();
        companyList.add(Util.COCUKLAR_KURUM_GONULLU);
        companyList.add(Util.HAYVANLAR_KURUMU_GONULLU);
        companyAdapter = new ArrayAdapter<>(getApplication(), R.layout.spinner_layout, companyList);
        binding.spinnerCompany.setAdapter(companyAdapter);


    }

    private void clickBtn() {

        binding.signUpBtn.setOnClickListener(signUp -> {
            singUpBtn();
        });
    }

    private void singUpBtn() {

        String name = binding.nameVolunionSignUp.getText().toString();
        String surname = binding.surnameVolunionSignUp.getText().toString();
        String job = binding.jobVolunionSignUp.getText().toString();
        String email = binding.mailVolunionSignUp.getText().toString();
        String password = binding.passwordVolunionSignUp.getText().toString();
        selectedCities = binding.spinnerCity.getSelectedItem().toString();
        selectedTowns = binding.spinnerTown.getSelectedItem().toString();
        selectedCompany = binding.spinnerCompany.getSelectedItem().toString();


        if (email.equals("") || password.equals("") || name.equals("") || surname.equals("") ||
                job.equals("") || selectedCities.equals("") || selectedTowns.equals("") || selectedCompany.equals("")) {

            Toast.makeText(this, "Lütfen boş alanları doldurun", Toast.LENGTH_SHORT).show();

        } else {

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {

                FirebaseUser firebaseUser = auth.getCurrentUser();
                assert firebaseUser != null;
                String volunionId = firebaseUser.getUid();

                HashMap<String, Object> postDataVolunion = new HashMap<>();
                postDataVolunion.put("id", volunionId);
                postDataVolunion.put("name", name);
                postDataVolunion.put("surname", surname);
                postDataVolunion.put("job", job);
                postDataVolunion.put("email", email);
                postDataVolunion.put("city", selectedCities);
                postDataVolunion.put("town", selectedTowns);
                postDataVolunion.put("company", selectedCompany);

                putVolunionDataToFirebase(Util.KURUM_GONULLULERI, postDataVolunion);
                PreferencesManagerInstance.getInstance(this).setGonulluEmail(email);
            }).addOnFailureListener(exception -> {
                Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });

        }
    }

    private void putVolunionDataToFirebase(String collectionName, HashMap<String, Object> data) {
        firebaseFirestore.collection(collectionName).add(data).addOnSuccessListener(documentReference -> {

            Intent intent = new Intent(this, VolunionFeedActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }).addOnFailureListener(exception -> {
            Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}


