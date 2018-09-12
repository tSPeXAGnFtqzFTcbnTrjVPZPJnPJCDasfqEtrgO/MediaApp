package com.example.andeptrai.myapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.andeptrai.myapplication.Animation.CustomAnimation;
import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.services.ForegroundService;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DiscFragment extends Fragment {
    @BindView(R.id.disc)
    CircleImageView imageView;
    @BindView(R.id.txtv_name)
    TextView txtvName;
    @BindView(R.id.txtv_artist)
    TextView txtvArtist;

    CustomAnimation customAnimation;

    boolean isRegister = false;
    long albumId = -1;
    long prevId = albumId;
    String title="",artist="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_frag1,container,false );
        ButterKnife.bind(this,view );

        txtvName.setSelected(true);
        txtvArtist.setSelected(true);
        customAnimation = new CustomAnimation(imageView);
        customAnimation.setAnim(imageView);
        return view;
    }

    @Override
    public void onStart() {
        if(!isRegister){
            isRegister=true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(ActionBroadCast.CURSEEK.getName());
            getContext().registerReceiver(broadcastReceiver,filter );
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if(isRegister){
            isRegister=false;
            getContext().unregisterReceiver(broadcastReceiver);
        }
        super.onStop();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();

            if (action.equals(ActionBroadCast.CURSEEK.getName())) {

                prevId = albumId;
                albumId = intent.getLongExtra(ForegroundService.ALBUM_KEY,albumId);
                title = intent.getStringExtra(ForegroundService.NAME_SONG);
                artist = intent.getStringExtra(ForegroundService.NAME_ARTIST);


                ShowLog.logInfo("recieved",albumId + "_" + prevId+"_"+
                        Instance.mapImageAlbum.containsKey(albumId));

                if(albumId!=prevId){

                    ShowLog.logInfo("received","change " +albumId);
                    if(Instance.mapImageAlbum.containsKey(albumId)){
                        imageView.setImageBitmap(Instance.mapImageAlbum.get(albumId));

                    }else{
                        ShowLog.logInfo("img disc",null );
                        imageView.setImageResource(R.drawable.ic_disc);
                    }

                    txtvArtist.setText(artist);
                    txtvName.setText(title);

                }
            }
        }
    };
}
