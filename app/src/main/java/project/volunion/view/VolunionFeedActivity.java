package project.volunion.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import project.companyInfo.R;
import project.companyInfo.databinding.ActivityVolunionFeedBinding;
import project.volunion.MainActivity;
import project.volunion.adapter.VolunionAdapter;
import project.volunion.model.CompanyInfo;
import project.volunion.util.PreferencesManagerInstance;
import project.volunion.util.Util;

public class VolunionFeedActivity extends AppCompatActivity {


    private ActivityVolunionFeedBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<CompanyInfo> companyInfoArrayList;
    private VolunionAdapter volunionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVolunionFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        companyInfoArrayList = new ArrayList<>();

        initRecyclerView();
        initFirebase();
        getData();

    }

    private void initRecyclerView() {
        binding.recyclerViewVolunion.setLayoutManager(new LinearLayoutManager(this));
        volunionAdapter = new VolunionAdapter(companyInfoArrayList);
        binding.recyclerViewVolunion.setAdapter(volunionAdapter);

    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getData() {
        databaseCheckCollection(Util.KURUM_GONULLULERI);
    }


    private void databaseCheckCollection(String collectionName) {

        firebaseFirestore.collection(collectionName).addSnapshotListener((value, error) -> {

            if (error != null) {
                Toast.makeText(VolunionFeedActivity.this, "", Toast.LENGTH_SHORT).show();
            }
            if (value != null) {

                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {

                    Map<String, Object> data = documentSnapshot.getData();

                    assert data != null;
                    String selectedCompany = (String) data.get("company");
                    String email = (String) data.get("email");
                    Log.e("YER", "EMAIL: " + email);

                    assert email != null;
                    if (email.equals(PreferencesManagerInstance.getInstance(this).getGonulluEmail())) {
                        assert selectedCompany != null;
                        if (selectedCompany.equals(Util.COCUKLAR_KURUM_GONULLU)) {
                            databaseCollection(Util.COCUKLAR_KURUMU);
                            break;
                        } else if (selectedCompany.equals(Util.HAYVANLAR_KURUMU_GONULLU)) {
                            databaseCollection(Util.HAYVANLAR_KURUMU);
                            break;
                        }
                    }
                }
            }
        });

    }

    private void databaseCollection(String collectionName) {
        firebaseFirestore.collection(collectionName).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Toast.makeText(VolunionFeedActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                if (value != null) {

                    for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                        Map<String, Object> data = documentSnapshot.getData();

                        assert data != null;
                        String userEmail = (String) data.get("useremail");
                        String name = (String) data.get("name");
                        String des = (String) data.get("des");
                        String downloadUrl = (String) data.get("downloadUrl");

                        CompanyInfo companyInfo = new CompanyInfo(userEmail, name, des, downloadUrl);
                        companyInfoArrayList.add(companyInfo);

                    }
                    volunionAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_volunion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.profile) {
            //go Activity
            Intent intent = new Intent(this, ProfileVolunionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (item.getItemId() == R.id.logoutFromMenu) {
            //logout
            if (auth != null) {
                auth.signOut();

                PreferencesManagerInstance.getInstance(this).setGonulluEmail("");
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }


        return super.onOptionsItemSelected(item);
    }
}