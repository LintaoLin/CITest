package lin.tao.citest;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by mac on 2017/8/17.
 */

public class MainActivity extends AppCompatActivity{
    Test test = new Test();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        test.name = "hello";
    }

    protected void testProtect() {

    }

    void def() {

    }

    private void test() {

    }

    public View setTest(Test test) {
        this.test = test;
        return new TextView(this);
    }
}
