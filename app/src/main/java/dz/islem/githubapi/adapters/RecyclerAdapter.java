package dz.islem.githubapi.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dz.islem.githubapi.R;
import dz.islem.githubapi.interfaces.IRecyclerView;
import dz.islem.githubapi.models.ItemModel;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static List<ItemModel> mData = new ArrayList<>();

    public RecyclerAdapter(){}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder mViewHolder, int position) {
        ItemModel data = mData.get(position);
        String mCount = (data.getStar_count() >= 1000 ? data.getStar_count()/1000+"k" : String.valueOf(data.getStar_count()));

        if(data.getName()!= null) mViewHolder.setTitle(data.getName());
        if(data.getDescription()!= null) mViewHolder.setDescription(data.getDescription());
        if (data.getOwners() != null) mViewHolder.setAvatar(data.getOwners().getAvatar_url());
        if (data.getLanguage() != null) mViewHolder.setLanguage(data.getLanguage());
        if (data.getLicenses() != null) mViewHolder.setLicense(data.getLicenses().getName());
        mViewHolder.setStarCount(mCount);
    }

    @Override
    public int getItemCount() {
        return (mData == null ? 0 : mData.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements IRecyclerView {
        private final TextView mTitle;
        private final TextView mDescription;
        private final TextView mStar;
        private final TextView mLang;
        private final ImageView mAvatatImg;
        private final TextView mLicenseView;

        public ViewHolder(@NonNull final View mView) {
            super(mView);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RecyclerAdapter.copyToClip(view.getContext(),getAdapterPosition());
                }
            });

            mTitle = mView.findViewById(R.id.title);
            mDescription = mView.findViewById(R.id.description);
            mAvatatImg = mView.findViewById(R.id.avatarImg);
            mStar = mView.findViewById(R.id.starView);
            mLang = mView.findViewById(R.id.langView);
            mLicenseView = mView.findViewById(R.id.licenseView);
        }

        @Override
        public void setTitle(String title) {
            mTitle.setText(title);
        }

        @Override
        public void setDescription(String description) {
            mDescription.setText(description);
        }

        @Override
        public void setStarCount(String count) {
            mStar.setText(count);
        }

        @Override
        public void setLanguage(String language) {
            mLang.setText(language);
        }

        @Override
        public void setAvatar(String avatar) {
            Picasso.get().load(avatar).into(mAvatatImg);
        }

        @Override
        public void setLicense(String license) {
            mLicenseView.setText(license);
        }

    }

    public static void copyToClip(Context context, int position) {
        ClipboardManager clipboard = (ClipboardManager)  context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Git URL", mData.get(position).getClone_url());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(context,"Link Copied to the Clipboard",Toast.LENGTH_SHORT).show();
    }

    public void setData(List<ItemModel> mData) {
        this.mData = mData;
    }

}
