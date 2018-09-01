package com.example.andeptrai.myapplication.dialog;

import android.app.Dialog;
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
import android.widget.Toast;

import com.example.andeptrai.myapplication.R;
import com.example.andeptrai.myapplication.utils.AndtUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowRenamePlaylistDialog extends DialogFragment {

    private static ShowRenamePlaylistDialog dialog;

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

    private static final String keyPlaylistId = "keyPlaylistId";


    long mPlaylistId;
    private OnComplete onComplete;

    public interface OnComplete {
        void complete(String newName);
    }

    public static ShowRenamePlaylistDialog newInstance(long playlistId) {
        if (dialog == null) {
            dialog = new ShowRenamePlaylistDialog();
        }

        Bundle bundle = new Bundle();
        bundle.putLong(keyPlaylistId, playlistId);

        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_dialog_rename_playlist, container, false);
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
        mPlaylistId = getArguments().getLong(keyPlaylistId);

        btnCancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });
        btnOk.setOnClickListener(v -> {
            String s = edit_playlist.getText().toString();
            if (s != null && s.length() > 0) {
                boolean isOk = AndtUtils.renamePlaylist(getContext(), mPlaylistId, s);

                if (!isOk) {
                    inputLayout.setError("Playlist da ton tai");
                } else {
                    if (onComplete != null) {
                        onComplete.complete(s);
                    }
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });
    }

    public ShowRenamePlaylistDialog setOnComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
        return dialog;
    }

}
