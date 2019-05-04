package com.hjsoftware.mahindralogisticsguest;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjsoftware.mahindralogisticsguest.model.DutyData;
import com.hjsoftware.mahindralogisticsguest.webservices.API;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FeedbackRecyclerAdapter extends RecyclerView.Adapter<FeedbackRecyclerAdapter.MyViewHolder> {

    ArrayList<DutyData> customArrayList;
    Context context;
    DutyData data;
    LayoutInflater inflater;
    boolean accept=false;
    API REST_CLIENT;
    Dialog dialog;
    private FeedbackRecyclerAdapter.AdapterCallback mAdapterCallback;
    int pos;
    ArrayList<DutyData> mResultList;
    boolean status=false;

    public FeedbackRecyclerAdapter(Context context, ArrayList<DutyData> customArrayList)
    {
        this.context=context;
        this.customArrayList=customArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog=new Dialog(context);
        try {
            this.mAdapterCallback = ((FeedbackRecyclerAdapter.AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    @Override
    public FeedbackRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feedback, parent, false);
        return new FeedbackRecyclerAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final FeedbackRecyclerAdapter.MyViewHolder holder, int position) {

        data=customArrayList.get(position);

        holder.tvDsno.setText("# "+data.getDsno());

        try {
            SimpleDateFormat newformat = new SimpleDateFormat("dd MMM yyyy");
            String datestring = data.getDsdate().split(" ")[0];
            SimpleDateFormat oldformat = new SimpleDateFormat("MM/dd/yyyy");
            String reformattedStr = newformat.format(oldformat.parse(datestring));

            holder.tvDsDate.setText(" "+reformattedStr);

        }catch(ParseException e){e.printStackTrace();}


        //holder.tvDsDate.setText(data.getDsdate().split(" ")[0]);

        holder.rLayout.setTag(position);





    }

    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDsno,tvDsDate;
        LinearLayout rLayout;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvDsno=(TextView)itemView.findViewById(R.id.rw_dsno);
            tvDsDate=(TextView)itemView.findViewById(R.id.rw_dsdate);
            rLayout=(LinearLayout) itemView.findViewById(R.id.llLayout);

            rLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        pos= (int) view.getTag();
                        mAdapterCallback.onMethodCallback(pos,customArrayList);

                    }
                    catch (ClassCastException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static interface AdapterCallback {
        void onMethodCallback(int position, ArrayList<DutyData> data);
    }

}