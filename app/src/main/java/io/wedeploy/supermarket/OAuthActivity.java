package io.wedeploy.supermarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.wedeploy.sdk.auth.Auth;
import com.wedeploy.sdk.auth.TokenAuth;
import com.wedeploy.sdk.transport.Response;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Silvio Santos
 */
public class OAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Auth authorization = TokenAuth.getAuthFromIntent(getIntent());

        if (authorization == null) {
            finish();
        }
        else {
            Settings.getInstance(this).saveToken(authorization.getToken());

            auth.getUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<Response>() {
                        @Override
                        public void onSuccess(Response response) {
                            Intent intent = new Intent(OAuthActivity.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    OAuthActivity.this,
                                    getString(R.string.could_not_sign_in_please_try_again),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private SupermarketAuth auth = new SupermarketAuth(Settings.getInstance(this));

}
