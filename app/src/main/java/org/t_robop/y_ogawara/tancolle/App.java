package org.t_robop.y_ogawara.tancolle;

/**
 * Created by yuusuke on 16/08/24.
 */
import android.app.Application;
import com.deploygate.sdk.DeployGate;

//deploygate用
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this);
    }
}