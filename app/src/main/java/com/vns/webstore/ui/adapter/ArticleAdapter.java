package com.vns.webstore.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vns.webstore.middleware.entity.Article;
import com.webstore.webstore.R;

import java.util.List;

/**
 * Created by root on 18/01/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context,List<Article> articles) {
        this.context = context;
        this.articles = articles;

    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_layout, viewGroup, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder articleViewHolder, int i) {
        Picasso.with(context).load(articles.get(i).getWebsiteAvatar()).resize(60, 60).into(articleViewHolder.ownerAvatar);
        articleViewHolder.ownerName.setText(articles.get(i).getFromWebSite());
        Picasso.with(context).load(articles.get(i).getImageUrl()).resize(120, 60).into(articleViewHolder.articleImage);
        articleViewHolder.articleContent.setText(articles.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{
        ImageView ownerAvatar;
        TextView ownerName;
        ImageView articleImage;
        TextView articleContent;

        public ArticleViewHolder(View view) {
            super(view);
            ownerAvatar = (ImageView) view.findViewById(R.id.owner_avatar);
            ownerName = (TextView) view.findViewById(R.id.owner_name);
            articleImage = (ImageView) view.findViewById(R.id.article_image);
            articleContent = (TextView) view.findViewById(R.id.article_content);
        }
    }
}
