package project.volunion.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.companyInfo.databinding.RecyclerRowBinding;
import project.volunion.model.CompanyInfo;

public class VolunionAdapter extends RecyclerView.Adapter<VolunionHolder> {

    private ArrayList<CompanyInfo> companyInfoArrayList;

    public VolunionAdapter(ArrayList<CompanyInfo> companyInfoArrayList) {
        this.companyInfoArrayList = companyInfoArrayList;
    }

    @NonNull
    @Override
    public VolunionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent,false);
        return new VolunionHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunionHolder holder, int position) {

        holder.recyclerRowBinding.commentRow.setText(companyInfoArrayList.get(position).name);
        holder.recyclerRowBinding.descriptionRow.setText(companyInfoArrayList.get(position).description);
        Picasso.get().load(companyInfoArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.imageViewRow);

    }

    @Override
    public int getItemCount() {
        return companyInfoArrayList.size();
    }
}

class VolunionHolder extends RecyclerView.ViewHolder{

    RecyclerRowBinding recyclerRowBinding;

    public VolunionHolder(RecyclerRowBinding recyclerRowBinding) {
        super(recyclerRowBinding.getRoot());
        this.recyclerRowBinding = recyclerRowBinding;

    }
}
