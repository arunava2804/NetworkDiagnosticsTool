package com.arunava.networktool.ui;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.arunava.networktool.R;
import com.arunava.networktool.databinding.ActivityMainBinding;
import com.arunava.networktool.viewmodel.NetworkViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NetworkViewModel viewModel;
    private PingAdapter pingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(NetworkViewModel.class);
        setupRecyclerView();
        setupObservers();

        binding.btnRunTest.setOnClickListener(v -> viewModel.runPingTest());
        // Run initial test
        viewModel.runPingTest();
    }

    private void setupRecyclerView() {
        pingAdapter = new PingAdapter();
        binding.rvPingResults.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPingResults.setAdapter(pingAdapter);
    }

    private void setupObservers() {
        viewModel.getNetworkStatus().observe(this, status -> {
            if (status != null) {
                binding.tvConnectionType.setText(status.getConnectionType());
                binding.tvLocalIp.setText(status.getLocalIp());
                binding.tvLinkSpeed.setText(status.getLinkSpeed());
            }
        });

        viewModel.getPingResults().observe(this, results -> {
            pingAdapter.updateList(results);
        });

        viewModel.getIsTesting().observe(this, isTesting -> {
            binding.btnRunTest.setEnabled(!isTesting);
            if (isTesting) {
                binding.tvStatusMessage.setVisibility(View.VISIBLE);
                binding.tvStatusMessage.setText(R.string.status_testing);
            } else {
                binding.tvStatusMessage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.refreshNetworkStatus();
    }
}
