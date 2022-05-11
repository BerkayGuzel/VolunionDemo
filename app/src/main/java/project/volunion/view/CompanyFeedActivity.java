package project.volunion.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Map;

import project.companyInfo.R;
import project.companyInfo.databinding.ActivityCompanyMainBinding;
import project.volunion.MainActivity;
import project.volunion.adapter.CompanyAdminAdapter;
import project.volunion.util.Util;
import project.volunion.model.CompanyInfo;

public class CompanyFeedActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<CompanyInfo> companyInfoArrayList;
    private CompanyAdminAdapter companyAdminAdapter;

    private ActivityCompanyMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        companyInfoArrayList = new ArrayList<>();

        initRecyclerView();
        initFirebase();
        getData();
    }

    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        companyAdminAdapter = new CompanyAdminAdapter(companyInfoArrayList);
        binding.recyclerView.setAdapter(companyAdminAdapter);

    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getData(){

        FirebaseUser user = auth.getCurrentUser();
        if(user!=null){
            String email = user.getEmail();

            assert email != null;
            if(email.equals(Util.COCUKLAR_KURUMU_MAIL)){
                databaseCollection(Util.COCUKLAR_KURUMU);
            }
            else if(email.equals(Util.HAYVANLAR_KURUMU_MAIL)){
                databaseCollection(Util.HAYVANLAR_KURUMU);
            }
        }

    }

    private void databaseCollection(String collectionName){
        firebaseFirestore.collection(collectionName).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {

            if(error!=null){
                Toast.makeText(CompanyFeedActivity.this, "", Toast.LENGTH_SHORT).show();
            }
            if(value!=null){
                companyInfoArrayList.clear();
                for(DocumentSnapshot documentSnapshot : value.getDocuments()){
                    Map<String, Object> data = documentSnapshot.getData();

                    assert data != null;
                    String userEmail = (String) data.get("useremail");
                    String name = (String) data.get("name");
                    String des = (String) data.get("des");
                    String downloadUrl = (String) data.get("downloadUrl");

                    String documentId  = documentSnapshot.getId();
                    CompanyInfo companyInfo = new CompanyInfo(userEmail,name,des,downloadUrl);
                    companyInfo.setDocumentId(documentId);


                    companyInfoArrayList.add(companyInfo);

                }
                companyAdminAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_company, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.profile){
            //go Activity
            Intent intent = new Intent(this, CreateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logoutFromMenu) {
            //logout
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}