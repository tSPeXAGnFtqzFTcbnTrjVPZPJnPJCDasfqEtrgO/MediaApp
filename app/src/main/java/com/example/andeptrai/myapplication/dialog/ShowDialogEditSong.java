package com.example.andeptrai.myapplication.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andeptrai.myapplication.Instance;
import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.fragment.ListMusicFragment;
import com.example.andeptrai.myapplication.function.ShowLog;
import com.example.andeptrai.myapplication.model.Song;
import com.example.andeptrai.myapplication.utils.AndtUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowDialogEditSong extends DialogFragment {
    public static final String keyBundle = "keyBundle";
    public static final String keyPosition = "keyPosition";


    @BindView(R.id.txtv_name)
    TextView txtvName;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.txtv_artist)
    TextView txtvArtist;
    @BindView(R.id.edit_artist)
    EditText editArtist;
    @BindView(R.id.txtv_album)
    TextView txtvAlbum;
    @BindView(R.id.edit_album)
    EditText editAlbum;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    Song song;
    int position;

    public static ShowDialogEditSong newInstance(Song song, int position) {
        ShowDialogEditSong dialog = new ShowDialogEditSong();
        Bundle bundle = new Bundle();
        bundle.putSerializable(keyBundle, song);
        bundle.putInt(keyPosition, position);

        dialog.setArguments(bundle);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_edit, container, false);
        ButterKnife.bind(this, view);

        getBundle();
        init();
        action();

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


    private void getBundle() {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable(keyBundle);
        position = bundle.getInt(keyPosition);
    }

    private void init() {
        ShowLog.logInfo("dialoog edit song", editName);
        editName.setText(song.getNameVi());
        editArtist.setText(song.getArtistName());
        editAlbum.setText(song.getAlbumName());
    }

    private void action() {
        btnCancel.setOnClickListener(v -> {
            getDialog().cancel();
        });
        btnOk.setOnClickListener(v -> {
            String title = editName.getText().toString().trim();
            String artist = editArtist.getText().toString().trim();
            String album = editAlbum.getText().toString().trim();

            if (!title.isEmpty() && title.compareTo(song.getNameVi()) != 0) {
                if(AndtUtils.renameSong(getContext(), song.getId(), title)){
                    Instance.baseSong.get(position).setNameVi(title);
                    Intent intent = new Intent();
                    intent.setAction(ListMusicFragment.actionNotify);
                    getContext().sendBroadcast(intent);
                }
            }
            if (!artist.isEmpty() && artist.compareTo(song.getAlbumName()) != 0) {
                AndtUtils.renameArtist(getContext(), song.getArtistId(), artist);
            }
            if (!album.isEmpty() && album.compareTo(song.getAlbumName()) != 0) {
                AndtUtils.renameAlbum(getContext(), song.getAlbumId(), album);
            }
            getDialog().cancel();
        });
    }
}
