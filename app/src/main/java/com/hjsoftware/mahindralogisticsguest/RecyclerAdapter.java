package com.hjsoftware.mahindralogisticsguest;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hjsoftware.mahindralogisticsguest.model.DutyData;
import com.hjsoftware.mahindralogisticsguest.webservices.API;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<DutyData> customArrayList;
    Context context;
    DutyData data;
    LayoutInflater inflater;
    boolean accept=false;
    API REST_CLIENT;
    Dialog dialog;
    private AdapterCallback mAdapterCallback;
    int pos;
    ArrayList<DutyData> mResultList;
    boolean status=false;


    public RecyclerAdapter(Context context, ArrayList<DutyData> customArrayList)
    {
        this.context=context;

        Fresco.initialize(context);

        this.customArrayList=customArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog=new Dialog(context);
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

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

        //holder.ivDriverPic.setImageURI(data.getImg());

        if(data.getImg().equals(""))
        {

        }
        else {
            //holder.ivDriverPic.setImageURI("http://192.168.1.12:3242/"+data.getImg());
            holder.ivDriverPic.setImageURI("http://mllapi.travelsmate.in/"+data.getImg());
        }

        if(data.getOtp().equals(""))
        {
            holder.tvOtp.setText("OTP: NA" );

        }
        else {

            holder.tvOtp.setText("OTP: " + data.getOtp());
        }





    }

    @Override
    public int getItemCount() {
        return customArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDsno,tvDsDate;
        LinearLayout rLayout;
        SimpleDraweeView ivDriverPic;
        TextView tvOtp;

        public MyViewHolder(final View itemView) {
            super(itemView);

            tvDsno=(TextView)itemView.findViewById(R.id.rw_dsno);
            tvDsDate=(TextView)itemView.findViewById(R.id.rw_dsdate);
            rLayout=(LinearLayout) itemView.findViewById(R.id.llLayout);
            ivDriverPic=(SimpleDraweeView)itemView.findViewById(R.id.rw_iv_driver);
            tvOtp=(TextView)itemView.findViewById(R.id.rw_otp);

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
