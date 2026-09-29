package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
  private final LayoutInflater inflater;
  private List<Article> articles;

  public ArticleAdapter(Context context, List<Article> articles) {
    inflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles) {
    this.articles = articles;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ArticleViewHolder(inflater.inflate(R.layout.article_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article article = articles.get(position);
    holder.title.setText(article.getTitle());
    ImageLoader.load(article.getImage(), holder.image);
    holder.itemView.setOnClickListener(view -> openDetails(view.getContext(), article));
  }

  private void openDetails(Context context, Article article) {
    Intent intent = new Intent(context, ArticleDetailActivity.class);
    intent.putExtra(ArticleDetailActivity.EXTRA_ID, article.getId());
    intent.putExtra(ArticleDetailActivity.EXTRA_TITLE, article.getTitle());
    intent.putExtra(ArticleDetailActivity.EXTRA_IMAGE, article.getImage());
    intent.putExtra(ArticleDetailActivity.EXTRA_DESCRIPTION, article.getDescription());
    context.startActivity(intent);
  }

  @Override
  public int getItemCount() { return articles.size(); }

  static class ArticleViewHolder extends RecyclerView.ViewHolder {
    final ImageView image;
    final TextView title;

    ArticleViewHolder(@NonNull View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.ivArticleImage);
      title = itemView.findViewById(R.id.tvArticleTitle);
    }
  }
}
