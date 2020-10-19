package com.example.smartlogbook.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.smartlogbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAllRegisters#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAllRegisters extends Fragment {
    private TableLayout tableLayout;
    View registerView;
    private String filePath = "generatedPDF.pdf";
    List<View> viewsToPDF = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mName, mSurname, mEmployeeID, mDepartment, mTimeIn, mTimeOut;


    public FragmentAllRegisters() {
        // Required empty public constructor
    }


    public static FragmentAllRegisters newInstance(int index) {
        FragmentAllRegisters fragment = new FragmentAllRegisters();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_all_registers, container, false);
        TextView tvId = root.findViewById(R.id.tv_table_employee_id);
        TextView tvSurname = root.findViewById(R.id.tv_table_surname);
        TextView tvName = root.findViewById(R.id.tv_table_name);
        TextView tvDepartment = root.findViewById(R.id.tv_table_department);
        TextView tvTimeIn = root.findViewById(R.id.tv_table_time_in);
        TextView tvTimeOut = root.findViewById(R.id.tv_table_time_out);
        return root;
    }

    private void makeTableHeader() {
        View tableRowHeader = LayoutInflater.from(getContext()).inflate(R.layout.table_item, null, false);
        TextView tv_Id = tableRowHeader.findViewById(R.id._id);
        TextView tvSurname = tableRowHeader.findViewById(R.id.tv_table_surname);
        TextView tvName =  tableRowHeader.findViewById(R.id.tv_table_name);
        TextView tvEmployeeId =  tableRowHeader.findViewById(R.id.tv_table_employee_id);
        TextView tvDepartment =  tableRowHeader.findViewById(R.id.tv_table_department);
        TextView tvTimeIn = tableRowHeader.findViewById(R.id.tv_table_time_in);
        TextView tvTimeOut =  tableRowHeader.findViewById(R.id.tv_table_time_out);

        tv_Id.setText("id");
        tvSurname.setText("SURNAME");
        tvName.setText("NAME");
        tvEmployeeId.setText("EmpID");
        tvDepartment.setText("DEPARTMENT");
        tvTimeIn.setText("TIME IN");
        tvTimeOut.setText("TIMEOUT");

        tableLayout.addView(tableRowHeader);
    }

    private void makeRegisterTableView(){

    }
}