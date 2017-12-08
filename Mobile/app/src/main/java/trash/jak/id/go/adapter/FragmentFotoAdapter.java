package trash.jak.id.go.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import trash.jak.id.go.R;
import trash.jak.id.go.model.FotoModel;

/**
 * Created by itp on 07/12/17.
 */

public class FragmentFotoAdapter extends RecyclerView.Adapter<FragmentFotoAdapter.ViewHolder> {

    private List<FotoModel> data;
    private Activity activity;

    public FragmentFotoAdapter(Activity activity, List<FotoModel> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public FragmentFotoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foto_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FragmentFotoAdapter.ViewHolder viewHolder, int i) {

        final FotoModel item = data.get(i);
        String gambar = item.getGambar();
        String nama = item.getNama();
        String deskripsi = item.getDeskripsi();

        Glide.with(activity)
                .load(item.getGambar())
                //.placeholder(R.mipmap.ic_launcher)
                //.error(R.mipmap.ic_launcher)
                .into(viewHolder.tv_gambar);

        viewHolder.tv_nama.setText(nama);
        viewHolder.tv_deskripsi.setText(deskripsi);
    }

    @Override
    public int getItemCount() {
        if (data==null){
            return 0;
        }
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_country;
        private ImageView tv_gambar;
        private TextView tv_nama;
        private TextView tv_deskripsi;



        public ViewHolder(View view) {
            super(view);

            tv_gambar = (ImageView)view.findViewById(R.id.gambar);
            tv_nama = (TextView)view.findViewById(R.id.nama);
            tv_deskripsi = (TextView)view.findViewById(R.id.deskripsi);
        }
    }
}


