package trash.jak.id.go.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import trash.jak.id.go.R;


/**
 * Created by itp on 06/12/17.
 */

public class CropOptionAdapter extends ArrayAdapter<CropOption> {
    private ArrayList<CropOption> mOptions;
    private LayoutInflater mInflater;
    private Context context;

    public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
        super(context, R.layout.crop_selector, options);

        mOptions 	= options;
        this.context = context;
        //mInflater	= LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.crop_selector, group, false);
        }

        CropOption item = mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);

            return convertView;
        }

        return null;
    }
}