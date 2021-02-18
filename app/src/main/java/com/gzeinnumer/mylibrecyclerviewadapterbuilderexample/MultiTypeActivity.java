package com.gzeinnumer.mylibrecyclerviewadapterbuilderexample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.ActivityMultiTypeBinding;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.RvItemBinding;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.RvItemGenapBinding;
import com.gzeinnumer.rab.helper.BindViewHolderMultiType;
import com.gzeinnumer.rab.helper.FilterCallBack;
import com.gzeinnumer.rab.model.TypeViewItem;
import com.gzeinnumer.rab.multiType.AdapterBuilderMultiType;
import com.gzeinnumer.rab.multiType.AdapterCreatorMultiType;

import java.util.ArrayList;
import java.util.List;

public class MultiTypeActivity extends AppCompatActivity {
    private ActivityMultiTypeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        multiType();
    }

    private void multiType() {
        List<MyModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new MyModel(i, "Data Ke " + (i + 1)));
        }
        AdapterCreatorMultiType<MyModel> adapter = new AdapterBuilderMultiType<MyModel>()
                .setList(list)
                .setCustomNoItem(R.layout.custom_empty_item)
                .setAnimation(R.anim.anim_two)
                .setDivider(R.layout.custom_divider)
                .onBind(new BindViewHolderMultiType<MyModel>() {

                    private final int TYPE_GENAP = 1;
                    private final int TYPE_GANJIL = 0;

                    @Override
                    public TypeViewItem getItemViewType(int position) {
                        if (position % 2 == 0)
                            return new TypeViewItem(TYPE_GENAP, R.layout.rv_item_genap);
                        else
                            return new TypeViewItem(TYPE_GANJIL, R.layout.rv_item);
                    }

                    @Override
                    public void bind(AdapterCreatorMultiType<MyModel> adapter, View holder, MyModel data, int position, int viewType) {
                        if (viewType == TYPE_GENAP) {
                            RvItemGenapBinding bindingItem = RvItemGenapBinding.bind(holder);
                            bindingItem.btn.setText(data.getId() + "_" + data.getName() + "_Genap");
                            bindingItem.btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "tekan " + position, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (viewType == TYPE_GANJIL) {
                            RvItemBinding bindingItem = RvItemBinding.bind(holder);
                            bindingItem.btn.setText(data.getId() + "_" + data.getName() + "_Ganjil");
                            bindingItem.btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "tekan " + position, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .onFilter(new FilterCallBack<MyModel>() {
                    @Override
                    public List<MyModel> performFiltering(CharSequence constraint, List<MyModel> listFilter) {
                        List<MyModel> fildteredList = new ArrayList<>();
                        if (constraint != null || constraint.length() != 0) {
                            String filterPattern = constraint.toString().toLowerCase().trim();
                            for (MyModel item : listFilter) {
                                //filter by id
                                if (String.valueOf(item.getId()).toLowerCase().contains(filterPattern)) {
                                    fildteredList.add(item);
                                }
                                //filter by name
                                if (item.getName().toLowerCase().contains(filterPattern)) {
                                    fildteredList.add(item);
                                }
                            }
                        }
                        return fildteredList;
                    }
                });

        binding.rv.setAdapter(adapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rv.hasFixedSize();

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