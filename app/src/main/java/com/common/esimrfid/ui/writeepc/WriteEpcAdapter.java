package com.common.esimrfid.ui.writeepc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.esimrfid.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WriteEpcAdapter extends RecyclerView.Adapter<WriteEpcAdapter.ViewHolder> {

    private List<String> mEpcs;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public WriteEpcAdapter(Context mContext, List<String> epcs) {
        this.mEpcs = epcs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_write_card, viewGroup, false);
        return new ViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String epc = mEpcs.get(i);
        viewHolder.tvEpc.setText(epc);
        viewHolder.tvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onRightImgClick(epc);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEpcs == null ? 0 : mEpcs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.epc_text)
        TextView tvEpc;
        @BindView(R.id.write_epc)
        TextView tvWrite;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public interface OnItemClickListener {
        void onRightImgClick(String epc);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
