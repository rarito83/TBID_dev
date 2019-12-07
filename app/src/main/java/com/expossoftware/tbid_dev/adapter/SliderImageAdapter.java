package com.expossoftware.tbid_dev.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.expossoftware.tbid_dev.R;


public class SliderImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mImagesId = new int[] {R.drawable.images1, R.drawable.images2, R.drawable.images3, R.drawable.images4, R.drawable.images5};

    public SliderImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImagesId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(mImagesId[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
