package vn.edu.ueh.thanhdnh.firebase_example;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageLoader {
  private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);

  private ImageLoader() {}

  public static void load(String imageUrl, ImageView imageView) {
    imageView.setTag(imageUrl);
    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
    if (imageUrl == null || imageUrl.trim().isEmpty()) return;

    EXECUTOR.execute(() -> {
      HttpURLConnection connection = null;
      try {
        connection = (HttpURLConnection) new URL(imageUrl).openConnection();
        connection.setConnectTimeout(8_000);
        connection.setReadTimeout(8_000);
        connection.setDoInput(true);
        connection.connect();
        try (InputStream stream = connection.getInputStream()) {
          Bitmap bitmap = BitmapFactory.decodeStream(stream);
          imageView.post(() -> {
            if (imageUrl.equals(imageView.getTag()) && bitmap != null) imageView.setImageBitmap(bitmap);
          });
        }
      } catch (Exception ignored) {
        imageView.post(() -> {
          if (imageUrl.equals(imageView.getTag())) {
            imageView.setImageResource(android.R.drawable.ic_menu_report_image);
          }
        });
      } finally {
        if (connection != null) connection.disconnect();
      }
    });
  }
}
