package com.kc.neteasy.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kc.neteasy.ICallBack.IResult;
import com.kc.neteasy.MainActivity;
import com.kc.neteasy.R;
import com.kc.neteasy.databinding.FragmentDownBinding;
import com.kc.neteasy.http.HttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownFragment extends Fragment {
    private FragmentDownBinding binding;

    public DownFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_down, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        String url = "https://www.baidu.com";
        binding.urlEdit.setText(url);
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = binding.urlEdit.getText().toString();
                //   Network network = NetWorkUtils.getInstance(MainActivity.this).getNetWork();
                HttpUtils.getInstance(true).getJsonStrFromUrl(requireActivity(), s, new IResult() {
                    @Override
                    public void onResponse(String s) {
                        binding.result.setText(s);
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.changeItem) {
            MainActivity.getInstance().getNavController().navigate(R.id.action_downFragment_to_upFragment);
        }
        if (item.getItemId() == R.id.wifiItem) {
            MainActivity.getInstance().getNavController().navigate(R.id.action_downFragment_to_wifiTestFragment);
        }
        return super.onOptionsItemSelected(item);
    }


}
