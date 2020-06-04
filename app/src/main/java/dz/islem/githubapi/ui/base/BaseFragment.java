package dz.islem.githubapi.ui.base;

import android.content.Context;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment<T extends BaseViewModel> extends Fragment {
    protected T viewModel;
    public abstract T getViewModel();

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        viewModel = getViewModel();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }
}
