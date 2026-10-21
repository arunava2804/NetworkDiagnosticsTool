package com.arunava.networktool.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.arunava.networktool.model.NetworkStatusModel;
import com.arunava.networktool.model.PingResult;
import com.arunava.networktool.network.NetworkInfoProvider;
import com.arunava.networktool.network.PingDiagnosticsEngine;
import java.util.List;

public class NetworkViewModel extends AndroidViewModel {

    private final MutableLiveData<NetworkStatusModel> networkStatus = new MutableLiveData<>();
    private final MutableLiveData<List<PingResult>> pingResults = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isTesting = new MutableLiveData<>(false);
    private final PingDiagnosticsEngine pingEngine = new PingDiagnosticsEngine();

    public NetworkViewModel(@NonNull Application application) {
        super(application);
        refreshNetworkStatus();
    }

    public LiveData<NetworkStatusModel> getNetworkStatus() { return networkStatus; }
    public LiveData<List<PingResult>> getPingResults() { return pingResults; }
    public LiveData<Boolean> getIsTesting() { return isTesting; }

    public void refreshNetworkStatus() {
        networkStatus.setValue(NetworkInfoProvider.getNetworkStatus(getApplication()));
    }

    public void runPingTest() {
        isTesting.setValue(true);
        pingEngine.runDiagnostics(results -> {
            pingResults.postValue(results);
            isTesting.postValue(false);
        });
    }
}
