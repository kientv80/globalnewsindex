package com.vns.webstore.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vns.webstore.middleware.entity.Article;
import com.vns.webstore.middleware.entity.NotifyInfo;
import com.vns.webstore.middleware.network.HttpClientHelper;
import com.vns.webstore.middleware.network.HttpRequestListener;
import com.vns.webstore.middleware.utils.JSONHelper;
import com.vns.webstore.ui.adapter.ArticleAdapter;
import com.vns.webstore.ui.adapter.PagerAdapter;
import com.webstore.webstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30/01/2017.
 */

public class ArticleFragment extends Fragment implements HttpRequestListener {
    private List<Article> articleList;
    RecyclerView articlesListingView;
    private String url;
    private String title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.article_listing,container,false);
        articlesListingView = (RecyclerView)viewGroup.findViewById(R.id.article_listing);
        articlesListingView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        articlesListingView.setLayoutManager(layoutManager);
        loadArticles();
        return viewGroup;
    }
    private void loadArticles(){
        HttpClientHelper.executeHttpGetRequest("http://360hay.com/getupdate?uid=" + "1234", this);
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onRecievedData(Object data) {
        if (data != null) {
            List<NotifyInfo> newNotifyInfoList = JSONHelper.toObjects(data.toString(), NotifyInfo.class);
            final List<Article> articles = new ArrayList<>();
            for (NotifyInfo n : newNotifyInfoList) {
                Article article = new Article();
                article.setTitle(n.getTitle());
                article.setImageUrl(n.getThemeUrl());
                article.setWebsiteAvatar(n.getThemeUrl());
                article.setFromWebSite(n.getFrom() + "\n(" + n.getDate() + ")");
                articles.add(article);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArticleAdapter adapter = new ArticleAdapter(getContext(), articles);
                            articlesListingView.setAdapter(adapter);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            }
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
