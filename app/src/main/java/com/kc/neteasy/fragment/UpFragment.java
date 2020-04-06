package com.kc.neteasy.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.kc.neteasy.ICallBack.IResult;
import com.kc.neteasy.R;
import com.kc.neteasy.bean.TestBean;
import com.kc.neteasy.databinding.FragmentUpBinding;
import com.kc.neteasy.http.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpFragment extends Fragment {


    public UpFragment() {
        // Required empty public constructor
    }

    private FragmentUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_up, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String url = "";
        //["IMSI,213123213123132,黑,50,已找到","IMSI,213123213123132,黑,50,已找到",,,,,,,,,]
        //   String params = "{\"data\":[\"IMSI,213123213123132,黑,50,已找到\"],\"filename\":\"xxx\"}";
        List<String> strings = new ArrayList<>();
        HashMap hashMap = new HashMap();
        String data = JSONArray.toJSONString(strings);
        binding.urlEdit.setText(url);
        String jsonStr = "data=" + "data" + "&filename=" + "2020.txt";
        binding.params.setText(jsonStr);


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = binding.urlEdit.getText().toString();
                //   Network network = NetWorkUtils.getInstance(MainActivity.this).getNetWork();
                HttpUtils.getInstance(true).wirteJsonStrToUrl(requireActivity(), s, jsonStr, new IResult() {
                    @Override
                    public void onResponse(String s) {
                        binding.result.setText(s);
                    }
                });
            }
        });
    }
}
