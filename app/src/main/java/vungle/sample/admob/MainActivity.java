package vungle.sample.admob;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements RewardedVideoAdListener {

    private RewardedVideoAd mAd;
    private String mAdUnitId = "ca-app-pub-3940256099942544/6905715713";

    private final Object mLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setUserId("userId");
        mAd.setRewardedVideoAdListener(this);
    }

    public void load(View view) {
        synchronized (mLock) {
            AdRequest adRequest = new AdRequest.Builder().build();
            ((Button) this.findViewById(R.id.show)).setText("Loading....");
            ((Button) this.findViewById(R.id.show)).setEnabled(false);
            mAd.loadAd(mAdUnitId, adRequest);
        }
    }

    public void show(View view) {
        mAd.show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        ((Button) this.findViewById(R.id.show)).setText("Ad shown");
        ((Button) this.findViewById(R.id.show)).setEnabled(false);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int arg0) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        ((Button) this.findViewById(R.id.show)).setText("Fail to Load Ad");
        ((Button) this.findViewById(R.id.show)).setEnabled(false);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        synchronized (mLock) {
            Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            ((Button) this.findViewById(R.id.show)).setText("Show");
            ((Button) this.findViewById(R.id.show)).setEnabled(true);
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem arg0) {
        Toast.makeText(this,
                "onRewarded! currency: " + arg0.getType() + "  amount: " + arg0.getAmount(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }
}
