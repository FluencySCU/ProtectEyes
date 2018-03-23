package cn.fluencycat.protecteyes.PickImg;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imnjh.imagepicker.ImageLoader;

import cn.fluencycat.protecteyes.Const.CONTEXT;
import cn.fluencycat.protecteyes.R;



public class GlideImageLoader implements ImageLoader {
  @Override
  public void bindImage(ImageView imageView, Uri uri, int width, int height) {
    Glide.with(CONTEXT.context).load(uri).placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher).override(width, height).dontAnimate().into(imageView);
  }

  @Override
  public void bindImage(ImageView imageView, Uri uri) {
    Glide.with(CONTEXT.context).load(uri).placeholder(R.mipmap.ic_launcher)
        .error(R.mipmap.ic_launcher).dontAnimate().into(imageView);
  }

  @Override
  public ImageView createImageView(Context context) {
    ImageView imageView = new ImageView(context);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    return imageView;
  }

  @Override
  public ImageView createFakeImageView(Context context) {
    return new ImageView(context);
  }
}
