package com.utech.allinonevideodownloader;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utech.allinonevideodownloader.models.RecDisplayAllWebsites_Model;
import com.utech.allinonevideodownloader.utils.Constants;

import java.util.ArrayList;

public class AllSupportedApps extends AppCompatActivity {

    private RecyclerView recviewSocialnetwork;
    private RecyclerView recviewOthernetwork;
    ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList;
    ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList_otherwebsites;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_supported);
        recviewSocialnetwork = (RecyclerView) findViewById(R.id.recview_socialnetwork);
        recviewOthernetwork = (RecyclerView) findViewById(R.id.recview_othernetwork);
        recDisplayAllWebsitesModelArrayList =new ArrayList<>();
        recDisplayAllWebsitesModelArrayList_otherwebsites =new ArrayList<>();


        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.facebook,"Facebook"));
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.tiktok,"TikTok"));
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.instagram,"Instagram"));
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.twitter,"Twitter"));
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.likee,"Likee"));
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.vkontakte,"VK"));
        if (Constants.showyoutube) {

            recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.ytdpic,"Youtube"));

        }
        recDisplayAllWebsitesModelArrayList.add(new RecDisplayAllWebsites_Model(R.drawable.vimeo,"Vimeo"));

        RecDisplayAllWebsitesAdapter recDisplayAllWebsitesAdapter = new RecDisplayAllWebsitesAdapter(this,recDisplayAllWebsitesModelArrayList);

        recviewSocialnetwork.setAdapter(recDisplayAllWebsitesAdapter);
        recviewSocialnetwork.setLayoutManager(new GridLayoutManager(this,4));




        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.igtv,"IGTV"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.topbuzz,"Topbuzz"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.buzzfeed,"Buzzfeed"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.dailymotion,"Dailymotion"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.espn,"ESPN"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.flickr,"Flickr"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.imdb,"IMDB"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.imgurlogo,"Imgur"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.mashable,"Mashable"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.gag,"9GAG"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.ted,"TED"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.twitch,"Twitch"));
        recDisplayAllWebsitesModelArrayList_otherwebsites.add(new RecDisplayAllWebsites_Model(R.drawable.tumblrnew,"Tumblr"));





        RecDisplayAllWebsitesAdapter recDisplayAllWebsitesAdapter_otherwesites = new RecDisplayAllWebsitesAdapter(this,recDisplayAllWebsitesModelArrayList_otherwebsites);

        recviewOthernetwork.setAdapter(recDisplayAllWebsitesAdapter_otherwesites);
        recviewOthernetwork.setLayoutManager(new GridLayoutManager(this,4));






    }


    class RecDisplayAllWebsitesAdapter extends RecyclerView.Adapter<RecDisplayAllWebsitesAdapter.RecDisplayAllWebsitesViewHolder> {

        Context context;
        ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList;

        public RecDisplayAllWebsitesAdapter(Context context, ArrayList<RecDisplayAllWebsites_Model> recDisplayAllWebsitesModelArrayList) {
            this.context = context;
            this.recDisplayAllWebsitesModelArrayList = recDisplayAllWebsitesModelArrayList;
        }

        @NonNull
        @Override
        public RecDisplayAllWebsitesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecDisplayAllWebsitesViewHolder(LayoutInflater.from(context).inflate(R.layout.recdisplayallwebsites_item, null, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecDisplayAllWebsitesViewHolder holder, int position) {


            holder.imgRecDisplayAllWebsites.setImageResource(recDisplayAllWebsitesModelArrayList.get(position).getImageview());
            holder.txtviewRecDisplayAllWebsites.setText(recDisplayAllWebsitesModelArrayList.get(position).getText_view());

        }

        @Override
        public int getItemCount() {
            return recDisplayAllWebsitesModelArrayList.size();
        }

        class RecDisplayAllWebsitesViewHolder extends RecyclerView.ViewHolder {

            private ImageView imgRecDisplayAllWebsites;
            private TextView txtviewRecDisplayAllWebsites;

            public RecDisplayAllWebsitesViewHolder(View view) {
                super(view);
                imgRecDisplayAllWebsites = (ImageView) view.findViewById(R.id.img_RecDisplayAllWebsites);
                txtviewRecDisplayAllWebsites = (TextView) view.findViewById(R.id.txtview_RecDisplayAllWebsites);

            }


        }

    }
}