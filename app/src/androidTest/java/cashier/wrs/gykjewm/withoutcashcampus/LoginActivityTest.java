package cashier.wrs.gykjewm.withoutcashcampus;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.gykj.cashier.R;
import com.gykj.cashier.module.index.ui.activity.MainActivity;
import com.gykj.cashier.module.login.ui.activity.LoginActivity;
import com.robotium.solo.Solo;


import org.junit.Test;

/**
 * description:
 * <p>
 * author: josh.lu
 * created: 15/8/18 上午9:54
 * email:  1113799552@qq.com
 * version: v1.0
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {


    private Solo solo;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    public void testLogin() throws Throwable {
        final EditText username = (EditText) solo.getView(R.id.login_username_et);
        final EditText password  = (EditText) solo.getView(R.id.login_password_et);

        for (int i = 0;i<100;i++){
            final int finalI = i;
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {

                    username.setText("13765072164");
                    if(finalI == 99){
                        password.setText("000000");
                    }else {
                        password.setText("00000");
                    }

                }
            });


            solo.sleep(1000);
            solo.clickOnView(solo.getView(R.id.login_login_tv));


            solo.clearEditText(password);

            solo.sleep(1000);

            solo.clickOnView(solo.getView(R.id.login_login_tv));
            solo.clearEditText(password);

            assert(solo.waitForActivity(MainActivity.class.getSimpleName()));


            solo.sleep(1000);



        }



    }

    public void tearDown() throws Exception {
        super.tearDown();
        solo.finishOpenedActivities();
    }
}
