package coursera_android_project.examples.dailyselfie;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by santosh on 18-07-2015.
 */
public class SelfieListAdapter extends BaseAdapter{
    private ArrayList<Selfie> mSelfieList;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public SelfieListAdapter(Context context, FragmentManager fragmentManager){
        mSelfieList = new ArrayList<Selfie>();
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    public void add(Selfie selfie){
        mSelfieList.add(selfie);
        this.notifyDataSetChanged();
    }

    public void clear(){
        mSelfieList.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSelfieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSelfieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        final Selfie selfie = (Selfie) getItem(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.selfie, null); ////////////////////

        final TextView textView = (TextView) linearLayout.findViewById(R.id.selfieName);
        textView.setText(selfie.getSelfieName());


        final ImageView imageView = (ImageView) linearLayout.findViewById(R.id.thumbnail);
        String url = selfie.getImagePath();

        Drawable d=Drawable.createFromPath(url);

        imageView.setImageDrawable(d);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        linearLayout.setTag(position);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int p = (Integer) v.getTag();
                ImageFragment frag = ImageFragment.newInstance(mSelfieList.get(p).getImagePath());

                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.main_layout, frag);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });

        return linearLayout;
    }
}
