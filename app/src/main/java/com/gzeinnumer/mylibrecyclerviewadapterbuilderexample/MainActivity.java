package com.gzeinnumer.mylibrecyclerviewadapterbuilderexample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gzeinnumer.mylibrecyclerviewadapterbuilder.AdapterBuilder;
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.AdapterCreator;
import com.gzeinnumer.mylibrecyclerviewadapterbuilder.BindViewHolder;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.ActivityMainBinding;
import com.gzeinnumer.mylibrecyclerviewadapterbuilderexample.databinding.RvItemBinding;

import java.util.ArrayList;
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
                .setCustomNoItem(R.layout.custom_empty_item)
                .setAnimation(R.anim.anim_two)
//                .setList(list)
                .onBind(new BindViewHolder() {
                    @Override
                    public void bind(View holder, int position) {
                        RvItemBinding bindingItem = RvItemBinding.bind(holder);
                        bindingItem.btn.setText(list.get(position).id + "_" + list.get(position).name);
                        bindingItem.btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "tekan " + position, Toast.LENGTH_SHORT).show();
                            }
                        });
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
//                adapter.setList(list);
            }
        }.start();

    }
}