package vn.edu.ueh.thanhdnh.firebase_example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

public class ArticleDetailActivity extends AppCompatActivity {
  public static final String EXTRA_ID = "article_id";
  public static final String EXTRA_TITLE = "article_title";
  public static final String EXTRA_IMAGE = "article_image";
  public static final String EXTRA_DESCRIPTION = "article_description";
  public static final String EXTRA_VIEW = "article_view";

  private TextView titleView;
  private TextView idView;
  private TextView descriptionView;
  private TextView viewCountView;
  private ImageView imageView;
  private ListenerRegistration articleListener;
  private String displayedImageUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_article_detail);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
      Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
      return insets;
    });

    titleView = findViewById(R.id.tvDetailTitle);
    idView = findViewById(R.id.tvDetailId);
    descriptionView = findViewById(R.id.tvDetailDescription);
    viewCountView = findViewById(R.id.tvDetailView);
    imageView = findViewById(R.id.ivDetailImage);

    Article initialArticle = new Article(
      valueOrEmpty(getIntent().getStringExtra(EXTRA_ID)),
      valueOrEmpty(getIntent().getStringExtra(EXTRA_TITLE)),
      valueOrEmpty(getIntent().getStringExtra(EXTRA_IMAGE)),
      valueOrEmpty(getIntent().getStringExtra(EXTRA_DESCRIPTION))
    );
    initialArticle.setView(getIntent().getLongExtra(EXTRA_VIEW, 0));
    renderArticle(initialArticle);

    ((Button) findViewById(R.id.btBack)).setOnClickListener(view -> finish());

    listenForArticleUpdates(initialArticle.getId());
  }

  private void listenForArticleUpdates(String articleId) {
    articleListener = FirebaseFirestore.getInstance()
      .collection("articles")
      .document(articleId)
      .addSnapshotListener(MetadataChanges.INCLUDE,
        (@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) -> {
        if (error != null) {
          Toast.makeText(this, getString(R.string.detail_load_failed, error.getMessage()), Toast.LENGTH_LONG).show();
          return;
        }
        if (snapshot == null) return;
        if (!snapshot.exists()) {
          if (snapshot.getMetadata().isFromCache()) return;
          Toast.makeText(this, R.string.article_no_longer_exists, Toast.LENGTH_LONG).show();
          return;
        }

        Article article = snapshot.toObject(Article.class);
        if (article == null) return;
        if (article.getId() == null || article.getId().isEmpty()) article.setId(snapshot.getId());
        renderArticle(article);
        });
  }

  private void renderArticle(Article article) {
    titleView.setText(valueOrEmpty(article.getTitle()));
    idView.setText(getString(R.string.article_id_value, valueOrEmpty(article.getId())));
    descriptionView.setText(valueOrEmpty(article.getDescription()));
    viewCountView.setText(getString(R.string.article_view_value, article.getView()));

    String imageUrl = valueOrEmpty(article.getImage());
    if (!imageUrl.equals(displayedImageUrl)) {
      displayedImageUrl = imageUrl;
      ImageLoader.load(imageUrl, imageView);
    }
  }

  private String valueOrEmpty(String value) { return value == null ? "" : value; }

  @Override
  protected void onDestroy() {
    if (articleListener != null) articleListener.remove();
    super.onDestroy();
  }
}
