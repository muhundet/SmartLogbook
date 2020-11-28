package com.example.smartlogbook.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartlogbook.R;
import com.example.smartlogbook.models.RegisterEntryModel;

import java.util.ArrayList;

public class CurrentRegisterRecyclerAdapter extends RecyclerView.Adapter<CurrentRegisterRecyclerAdapter.CurrentRegisterViewHolder> {

    ArrayList<RegisterEntryModel> registerEntries;

    public CurrentRegisterRecyclerAdapter(ArrayList<RegisterEntryModel> registerEntries) {
        this.registerEntries = registerEntries;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentRegisterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.employee_list_item,parent,false);
        return new CurrentRegisterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentRegisterViewHolder holder, int position) {
        RegisterEntryModel registerEntryModel = registerEntries.get(position);
        holder.bind(registerEntryModel);
    }



    @Override
    public int getItemCount() {
        return registerEntries.size();
    }

    public static class CurrentRegisterViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvDepartment, tvTimeIn, tvTimeOut;

        public CurrentRegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_employee_name);
            tvDepartment = itemView.findViewById(R.id.tv_employee_department);
            tvTimeIn = itemView.findViewById(R.id.tv_time_in);
            tvTimeOut = itemView.findViewById(R.id.tv_time_out);

        }

        public void bind(RegisterEntryModel registerEntryModel){
            String name = registerEntryModel.getEmployeeId() +", " + registerEntryModel.getEmployeeName() +", " +  registerEntryModel.getEmployeeSurname();
            tvName.setText(name);
            tvDepartment.setText(registerEntryModel.getEmployeeDepartment());
            tvTimeIn.setText(registerEntryModel.getTime_in());
            tvTimeOut.setText(registerEntryModel.getTime_out());
        }

    }
}
