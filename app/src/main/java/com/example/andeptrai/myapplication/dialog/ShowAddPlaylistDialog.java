package com.example.andeptrai.myapplication.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.constant.ActionBroadCast;
import com.example.andeptrai.myapplication.function.MusicPlayer;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Playlist;
import com.example.andeptrai.myapplication.model.Song;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowAddPlaylistDialog extends DialogFragment {

    @BindView(R.id.layout_add_playlist)
    ConstraintLayout layout;
    @BindView(R.id.input_layout)
    TextInputLayout inputLayout;
    @BindView(R.id.edit_add_playlist)
    TextInputEditText edit_playlist;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private static final String keySongArr = "key_song_arr";

    private ArrayList<Song> mSongs;

    public static ShowAddPlaylistDialog newInstance(ArrayList<Song> songs) {
        ShowAddPlaylistDialog dialog = new ShowAddPlaylistDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(keySongArr, songs);

        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_dialog_add_playlist, container, false);
        ButterKnife.bind(this, view);

        init();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 400);

        }
    }

    private void init() {
        mSongs = (ArrayList<Song>) getArguments().getSerializable(keySongArr);

        btnCancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });
        btnOk.setOnClickListener(v -> {
            String s = edit_playlist.getText().toString();
            if (s != null && s.length() > 0) {
                int id = MusicPlayer.createPlaylist(getContext(), s);
                ShowLog.logInfo("id create", id);
                if (id == -1) {
                    inputLayout.setError("Playlist da ton tai");
                } else {
                    Instance.playlists.add(new Playlist(id, 0, s));
                    Instance.playlists.get(Instance.playlists.size() - 1).
                            addSongArray(getContext(), mSongs);

                    Intent intent = new Intent();
                    intent.setAction(ActionBroadCast.UPDATE_PLAYLIST.getName());
                    getContext().sendBroadcast(intent);

                    getDialog().dismiss();
                }
            }
        });
    }
}
