package project.volunion.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import project.companyInfo.R;
import project.companyInfo.databinding.RecyclerRowAdminBinding;
import project.volunion.model.CompanyInfo;
import project.volunion.util.PreferencesManagerInstance;

public class CompanyAdminAdapter extends RecyclerView.Adapter<CompanyAdminHolder> {

    private ArrayList<CompanyInfo> companyInfoArrayList;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;


    public CompanyAdminAdapter(ArrayList<CompanyInfo> companyInfoArrayList) {
        this.companyInfoArrayList = companyInfoArrayList;
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public CompanyAdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowAdminBinding recyclerRowBinding = RecyclerRowAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CompanyAdminHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdminHolder holder, int position) {

        holder.recyclerRowBinding.commentRow.setText(companyInfoArrayList.get(position).name);
        holder.recyclerRowBinding.descriptionRow.setText(companyInfoArrayList.get(position).description);
        Picasso.get().load(companyInfoArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.imageViewRow);


        holder.recyclerRowBinding.edit.setOnClickListener(edit -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.recyclerRowBinding.imageViewRow.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup))
                    .setExpanded(true, 1200)
                    .create();


            View view = dialogPlus.getHolderView();
            EditText name = view.findViewById(R.id.nameTextAdminRow);
            EditText des = view.findViewById(R.id.desTextAdminRow);
            TextView url = view.findViewById(R.id.imageUrlAdminRow);
            Button updateBtn = view.findViewById(R.id.editPopUp);
            Button deleteBtn = view.findViewById(R.id.deletePopUp);

            name.setText(companyInfoArrayList.get(position).name);
            des.setText(companyInfoArrayList.get(position).description);
            url.setText(companyInfoArrayList.get(position).downloadUrl);

            dialogPlus.show();


            updateBtn.setOnClickListener(update -> {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name.getText().toString());
                map.put("des", des.getText().toString());
                map.put("downloadUrl", url.getText().toString());

                Log.e("YER", "name" + name.getText().toString());
                firebaseFirestore.collection(
                        PreferencesManagerInstance.getInstance(view.getContext()).getKurumBilgi()
                ).document(companyInfoArrayList.get(position).getDocumentId()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(holder.itemView.getContext(), "Guncel", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();
                    }
                });

            });

            deleteBtn.setOnClickListener(delete -> {
                firebaseFirestore.collection(
                        PreferencesManagerInstance.getInstance(view.getContext()).getKurumBilgi()
                ).document(companyInfoArrayList.get(position).getDocumentId())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(holder.itemView.getContext(), "Sil", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        });

    }

    @Override
    public int getItemCount() {
        return companyInfoArrayList.size();

    }


}

class CompanyAdminHolder extends RecyclerView.ViewHolder {

    RecyclerRowAdminBinding recyclerRowBinding;

    public CompanyAdminHolder(RecyclerRowAdminBinding recyclerRowBinding) {
        super(recyclerRowBinding.getRoot());
        this.recyclerRowBinding = recyclerRowBinding;
    }
}

