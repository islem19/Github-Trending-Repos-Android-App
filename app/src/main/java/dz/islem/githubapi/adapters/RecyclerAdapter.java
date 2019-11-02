package dz.islem.githubapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import dz.islem.githubapi.R;
import dz.islem.githubapi.interfaces.IRecyclerView;
import dz.islem.githubapi.presenters.RecyclerViewPresenter;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static RecyclerViewPresenter mRecyclerViewPresenter;

    public RecyclerAdapter(RecyclerViewPresenter mRecyclerViewPresenter){
        this.mRecyclerViewPresenter = mRecyclerViewPresenter;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mRecyclerViewPresenter.onBindViewHolderAtPosition(holder,position);
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewPresenter.getDataSize();
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
                    Toast.makeText(view.getContext(),"Link Copied to the Clipboard",Toast.LENGTH_SHORT).show();
                    mRecyclerViewPresenter.copyToClip(view.getContext(),getAdapterPosition());
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

}
