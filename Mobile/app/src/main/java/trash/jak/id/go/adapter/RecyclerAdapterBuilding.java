package trash.jak.id.go.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trash.jak.id.go.R;
import trash.jak.id.go.model.BuildingModel;
import trash.jak.id.go.widget.CircularTextView;

/**
 * Created by itp on 07/12/17.
 */

public class RecyclerAdapterBuilding extends RecyclerView.Adapter<RecyclerAdapterBuilding.MyViewHolder>  implements Filterable {
    private List<BuildingModel> doList;
    private List<BuildingModel> listdo;

    private Activity activity;
    public RecyclerAdapterBuilding(Activity activity, List<BuildingModel> itemsData) {
        this.listdo  = itemsData;
        this.doList = itemsData;
        this.activity = activity;
    }

    public RecyclerAdapterBuilding(List<BuildingModel> doList) {
        this.doList = doList;
    }

    @Override
    public RecyclerAdapterBuilding.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerAdapterBuilding.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_design_building, parent, false));
    }


    @Override
    public void onBindViewHolder(final RecyclerAdapterBuilding.MyViewHolder holder, int position) {
        try {
            //dikirim item model pakai parcelable_extra
            // nanti diparsing di get2 parsing FormUpload2
            //    untuk mengambil gambar private List<Image> listSebelum, listProses, listSesudah;
            final BuildingModel buildingModel = doList.get(position);
            final String alamat = buildingModel.getAlamat();
            String nm_penyedia = buildingModel.getNama_penyedia();
            String tlp = buildingModel.getNomor_telepon();
            String kelurahan = buildingModel.getKelurahan();
            String wilayah = buildingModel.getWilayah();
            String fontPath = "fonts/RobotoLight.ttf";
            Typeface tf = Typeface.createFromAsset(activity.getAssets(), fontPath);

            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(nm_penyedia);

            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(nm_penyedia.substring(0,1), color);

            /*int color_arr[] = {R.array.array_androidcolors};
            int rnd = new Random().nextInt(color_arr.length);*/
            //holder.img.setImageDrawable(drawable);

            if (alamat == null|| alamat.equalsIgnoreCase("")|| alamat.isEmpty()) {
                holder.alamat.setText(buildingModel.getAlamat());
            } else  {
                holder.nama_penyedia.setText(buildingModel.getNama_penyedia());
            }

            holder.alamat.setText(buildingModel.getAlamat()+
                    ", "+buildingModel.getKelurahan()+
                    ", "+buildingModel.getWilayah());

            holder.txt_circle.setSolidColor(color);
            //holder.txt_circle.getResources().getStringArray(color_arr[rnd]);
            holder.txt_circle.setText(nm_penyedia.substring(0,1).toUpperCase());

            // holder.npwz.setText(muzakiModel.getNpwz());
            holder.tlp.setText(buildingModel.getNomor_telepon());
        /*    holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent = new Intent(activity, FormKasMasuk.class);
                        intent.putExtra("npwz", muzakiModel.getNpwz());
                        intent.putExtra("namamuzaki", muzakiModel.getNama_muzaki());
                        intent.putExtra("datereg", muzakiModel.getDate_reg());
                        intent.putExtra("reg_no", muzakiModel.getReg_no());
                        //intent.putExtra("npwz", muzakiModel.getNpwz());
                        // intent.putExtra("list", workorderModel.getList());
                        activity.startActivity(intent);
                    }
            });*/
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (doList!=null) {
            return doList.size();
        }
        return 0;
    }

   /* public void filterList(ArrayList<BuildingModel> filterdNames ) {
        this.doList = filterdNames;
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    doList = listdo;
                } else {
                    ArrayList<BuildingModel> filteredList = new ArrayList<>();
                    for (BuildingModel androidVersion : doList) {

                        if (androidVersion.getNama_penyedia().toLowerCase().contains(charString) || androidVersion.getAlamat().toLowerCase().contains(charString)) {
                            filteredList.add(androidVersion);
                        }
                    }
                    doList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = doList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                doList = (ArrayList<BuildingModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_penyedia, alamat, tlp;
        public CircularTextView txt_circle;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            nama_penyedia = (TextView) view.findViewById(R.id.txt_penyedia);
            alamat = (TextView) view.findViewById(R.id.txt_alamat);
            tlp = (TextView) view.findViewById(R.id.txt_tlp);
            txt_circle = (CircularTextView) view.findViewById(R.id.txt_circle);
            img = (ImageView) view.findViewById(R.id.user_profile);


        }
    }

    private void setCircleColor(TextView txtView) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        txtView.setBackgroundColor(color);
    }

}