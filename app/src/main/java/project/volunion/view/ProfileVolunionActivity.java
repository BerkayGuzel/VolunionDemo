package project.volunion.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import project.companyInfo.databinding.ActivityProfileVolunionBinding;
import project.volunion.util.Util;

public class ProfileVolunionActivity extends AppCompatActivity {

    private ActivityProfileVolunionBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileVolunionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebase();
        getData();

    }



    private void initFirebase(){
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void getData(){
       firebaseFirestore.collection(Util.KURUM_GONULLULERI).addSnapshotListener((value, error) -> {

           if(value!=null) {
               for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                   Map<String, Object> data = documentSnapshot.getData();

                   assert data != null;
                   FirebaseUser user = auth.getCurrentUser();
                   assert user != null;
                   String userId = user.getUid();

                   String volunionId = (String) data.get("id");
                   String name = (String) data.get("name");
                   String surname = (String) data.get("surname");
                   String job = (String) data.get("job");
                   String city = (String) data.get("city");
                   String town  ="/ "+ data.get("town");
                   String company  = (String) data.get("company");
                   if(userId.equals(volunionId)){
                       binding.volunionName.setText(name);
                       binding.volunionSurname.setText(surname);
                       binding.volunionJob.setText(job);
                       binding.volunionCity.setText(city);
                       binding.volunionTown.setText(town);
                       binding.volunionCompany.setText(company);

                   }



               }
           }else {
               assert error != null;
               Toast.makeText(ProfileVolunionActivity.this, "Hara oluştu" + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

}