package vungle.sample.admob;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private RewardedVideoAd mAd;
    private InterstitialAd iAd;
    private String mAdUnitIdRewardBased = "ca-app-pub-3940256099942544/6905715713";//rewardBased
    private String mAdUnitIdInterstitial = "ca-app-pub-1812018162342166/1294731936";//interstitial

    private Button showRewardedVideoAdButton;
    private Button showInterstitialAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showRewardedVideoAdButton = ((Button) this.findViewById(R.id.show));
        showInterstitialAdButton = ((Button) this.findViewById(R.id.showInterstitial));

        //RewardedVideoAd init
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setUserId("userId");
        mAd.setRewardedVideoAdListener(mRewardedVideoAdListener);

        //InterstitialAd init
        iAd = new InterstitialAd(this);
        iAd.setAdUnitId(mAdUnitIdInterstitial);
        iAd.setAdListener(mAdListener);

    }

    public void load(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();
        showRewardedVideoAdButton.setText("Loading....");
        showRewardedVideoAdButton.setEnabled(false);
        mAd.loadAd(mAdUnitIdRewardBased, adRequest);
    }

    public void show(View view) {
        mAd.show();
    }

    public void loadInterstitial(View view) {
        AdRequest adRequest = new AdRequest.Builder().build();
        showInterstitialAdButton.setText("Loading....");
        showInterstitialAdButton.setEnabled(false);
        iAd.loadAd(adRequest);
    }

    public void showInterstitial(View view) {
        iAd.show();
    }

    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdClosed() {
            Toast.makeText(MainActivity.this, "onAdClosed", Toast.LENGTH_SHORT).show();
            showInterstitialAdButton.setText("Ad shown");
            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Toast.makeText(MainActivity.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
            showInterstitialAdButton.setText("Fail to Load Ad");
            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdLeftApplication() {
            Toast.makeText(MainActivity.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdLoaded() {
            Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
            showInterstitialAdButton.setText("Show");
            showInterstitialAdButton.setEnabled(true);
        }

        @Override
        public void onAdOpened() {
            Toast.makeText(MainActivity.this, "onAdOpened", Toast.LENGTH_SHORT).show();
        }
    };

    private RewardedVideoAdListener mRewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLeftApplication() {
            Toast.makeText(MainActivity.this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdClosed() {
            Toast.makeText(MainActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            showRewardedVideoAdButton.setText("Ad shown");
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int arg0) {
            Toast.makeText(MainActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            showRewardedVideoAdButton.setText("Fail to Load Ad");
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            Toast.makeText(MainActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            showRewardedVideoAdButton.setText("Show");
            showRewardedVideoAdButton.setEnabled(true);

        }

        @Override
        public void onRewardedVideoAdOpened() {
            Toast.makeText(MainActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewarded(RewardItem arg0) {
            Toast.makeText(MainActivity.this,
                    "onRewarded! currency: " + arg0.getType() + "  amount: " + arg0.getAmount(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoStarted() {
            Toast.makeText(MainActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
        }
    };

}
