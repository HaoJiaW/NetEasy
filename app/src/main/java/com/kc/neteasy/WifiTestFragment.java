package com.kc.neteasy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kc.neteasy.databinding.FragmentWifiTestBinding;
import com.kc.neteasy.wifi.WifiUtils;


public class WifiTestFragment extends Fragment {

    private FragmentWifiTestBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_wifi_test,container,false);
        initView();
        return binding.getRoot();
    }


    private void initView(){
        WifiUtils.getInstance(requireActivity()).checkWifiEnable(requireActivity());
        binding.connectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiUtils.getInstance(requireActivity()).connectWifi(requireContext().getApplicationContext(),"");
            }
        });
    }







}