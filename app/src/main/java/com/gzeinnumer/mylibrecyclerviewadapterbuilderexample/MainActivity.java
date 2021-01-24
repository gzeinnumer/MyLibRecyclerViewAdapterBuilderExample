package com.gzeinnumer.mylibrecyclerviewadapterbuilderexample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gzeinnumer.mylibrecyclerviewadapterbuilder.AdapterBuilder;
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.AdapterCreator;
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.helper.BindViewHolder;
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.helper.FilterCallBack;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.ActivityMainBinding;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.RvItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAdapter();
    }

    private void initAdapter() {
        List<MyModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new MyModel(i, "Data Ke " + (i + 1)));
        }
        AdapterCreator<MyModel> adapter = new AdapterBuilder<MyModel>(R.layout.rv_item)
                .setList(list)
                .setCustomNoItem(R.layout.custom_empty_item)
                .setAnimation(R.anim.anim_two)
                .setDivider(R.layout.custom_divider)
                .onBind(new BindViewHolder<MyModel>() {
                    @Override
                    public void bind(View holder, MyModel data, int position) {
                        RvItemBinding bindingItem = RvItemBinding.bind(holder);
                        bindingItem.btn.setText(data.getId() + "_" + data.getName());
                        bindingItem.btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "tekan " + position, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .onFilter(new FilterCallBack<MyModel>() {
                    @Override
                    public List<MyModel> performFiltering(CharSequence constraint, List<MyModel> listFilter) {
                        List<MyModel> fildteredList = new ArrayList<>();
                        if (constraint == null || constraint.length() == 0) {
                            Collections.sort(listFilter, new Comparator<MyModel>() {
                                @Override
                                public int compare(MyModel o1, MyModel o2) {
                                    return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                                }
                            });
                            fildteredList.addAll(listFilter);
                        } else {
                            String filterPattern = constraint.toString().toLowerCase().trim();
                            for (MyModel item : listFilter) {
                                if (String.valueOf(item.getId()).toLowerCase().contains(filterPattern)) {
                                    fildteredList.add(item);
                                }
                            }
                        }
                        return fildteredList;
                    }
                });

        binding.rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rv.hasFixedSize();
        binding.rv.setAdapter(adapter);

        //after 5 second, new data will appear
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                for (int i = 10; i < 100; i++) {
                    list.add(new MyModel(i, "Data Ke " + (i + 1)));
                }
                adapter.setList(list);
            }
        }.start();

        //use filter on TextWacher
        binding.ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //call the filter
                adapter.getFilter().filter(s);
            }
        });
    }
}