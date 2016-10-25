package vungle.sample.admob;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.common.GoogleApiAvailability;
import com.vungle.mediation.VungleAdapter;
import com.vungle.mediation.VungleExtrasBuilder;
import com.vungle.mediation.VungleInterstitialAdapter;
import com.vungle.publisher.VunglePub;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RewardedVideoAd mAd;
    private InterstitialAd iAd;
    private static final String mAdUnitIdRewardBased = "ca-app-pub-3940256099942544/6905715713";
    private static final String mAdUnitIdInterstitial = "ca-app-pub-1812018162342166/1294731936";

    private Button showRewardedVideoAdButton;
    private Button showInterstitialAdButton;
    private ScrollView scrollView;
    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showRewardedVideoAdButton = ((Button) this.findViewById(R.id.show));
        showInterstitialAdButton = ((Button) this.findViewById(R.id.showInterstitial));
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        logTextView = (TextView) findViewById(R.id.logContent);

        // Check Google Play Services availability
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);
        apiAvailability.showErrorDialogFragment(this, errorCode, 0);

        //RewardedVideoAd init
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(mRewardedVideoAdListener);

        //InterstitialAd init
        iAd = new InterstitialAd(this);
        iAd.setAdUnitId(mAdUnitIdInterstitial);
        iAd.setAdListener(mAdListener);

        // About
        final String about = String.format(getString(R.string.format_about), getVersion(),
                getGooglePlayServicesVersion(), VunglePub.VERSION);
        log(about);
    }

    private void log(CharSequence text) {
        Log.d(TAG, text.toString());

        if (logTextView.length() > 0)
            logTextView.append("\n");
        logTextView.append(text);

        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 500);
    }

    private String getVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getVersion failed", e);
            return "";
        }
    }

    private String getGooglePlayServicesVersion() {
        try {
            return getPackageManager()
                    .getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "getGooglePlayServicesVersion failed", e);
            return "";
        }
    }

    private Bundle getNetworkExtras() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return new VungleExtrasBuilder()
                .setUserId(prefs.getString(SettingsActivity.KEY_PREF_USER_ID, null))
                .setSoundEnabled(prefs.getBoolean(SettingsActivity.KEY_PREF_SOUND_ENABLED, true))
                .build();
    }

    public void load(View view) {
        log("Loading reward-based ad…");
        Bundle extras = getNetworkExtras();
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(VungleAdapter.class, extras)
                .build();
        showRewardedVideoAdButton.setText(R.string.ad_loading);
        showRewardedVideoAdButton.setEnabled(false);
        mAd.loadAd(mAdUnitIdRewardBased, adRequest);
    }

    public void show(View view) {
        log("Showing reward-based ad…");
        mAd.show();
    }

    public void loadInterstitial(View view) {
        if (iAd.isLoading()) {
            log("An interstitial is already loading.");
            return;
        }
        if (iAd.isLoaded()) {
            log("An interstitial is already loaded.");
            return;
        }
        log("Loading interstitial ad…");
        Bundle extras = getNetworkExtras();
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(VungleInterstitialAdapter.class, extras)
                .build();
        showInterstitialAdButton.setText(R.string.ad_loading);
        showInterstitialAdButton.setEnabled(false);
        iAd.loadAd(adRequest);
    }

    public void showInterstitial(View view) {
        log("Showing interstitial ad…");
        iAd.show();
    }

    public void options(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private AdListener mAdListener = new AdListener() {
        @Override
        public void onAdClosed() {
            log("onInterstitialAdClosed");
            showInterstitialAdButton.setText(R.string.ad_shown);
            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            log("onInterstitialAdFailedToLoad: error " + errorCode);
            showInterstitialAdButton.setText(R.string.ad_failed);
            showInterstitialAdButton.setEnabled(false);
        }

        @Override
        public void onAdLeftApplication() {
            log("onInterstitialAdLeftApplication");
        }

        @Override
        public void onAdLoaded() {
            log("onInterstitialAdLoaded");
            showInterstitialAdButton.setText(R.string.ad_show);
            showInterstitialAdButton.setEnabled(true);
        }

        @Override
        public void onAdOpened() {
            log("onInterstitialAdOpened");
        }
    };

    private RewardedVideoAdListener mRewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLeftApplication() {
            log("onRewardedVideoAdLeftApplication");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            log("onRewardedVideoAdClosed");
            showRewardedVideoAdButton.setText(R.string.ad_shown);
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            log("onRewardedVideoAdFailedToLoad: error " + errorCode);
            showRewardedVideoAdButton.setText(R.string.ad_failed);
            showRewardedVideoAdButton.setEnabled(false);
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            log("onRewardedVideoAdLoaded");
            showRewardedVideoAdButton.setText(R.string.ad_show);
            showRewardedVideoAdButton.setEnabled(true);

        }

        @Override
        public void onRewardedVideoAdOpened() {
            log("onRewardedVideoAdOpened");
        }

        @Override
        public void onRewarded(RewardItem reward) {
            log("onRewarded! currency: " + reward.getType() + ", amount: " + reward.getAmount());
        }

        @Override
        public void onRewardedVideoStarted() {
            log("onRewardedVideoStarted");
        }
    };

}
