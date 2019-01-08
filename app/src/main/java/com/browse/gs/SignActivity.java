package com.browse.gs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.browse.gs.util.Util;

import java.io.File;
//签字板
public class SignActivity extends AppCompatActivity {
    //文件名
    private String fileName;
    //签字视区
    private SignatureView signView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        //读取文件名
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            fileName = bundle.getString("ID");

        signView = (SignatureView) (findViewById(R.id.signView));
        if(Util.fileExists(Util.getSharedPreference(this, "FileDir") + fileName + ".png"))
            signView.url = Util.getSharedPreference(this, "FileDir") + fileName + ".png";
        Button btnClear = (Button)this.findViewById(R.id.clear);
        btnClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                signView = (SignatureView) (findViewById(R.id.signView));
                signView.clear();
                File file = new File(Util.getSharedPreference(SignActivity.this, "FileDir") + fileName + ".png");
                file.delete();
            }
        });
        //保存签字内容
        Button btnReturn = (Button)this.findViewById(R.id.ok);
        btnReturn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                signView.saveToFile(fileName);
                if(Util.fileExists(Util.getSharedPreference(SignActivity.this, "FileDir") + fileName + ".png"))
                {
                    Intent intent =new Intent();
                    intent.putExtra("result", fileName);
                    intent.putExtra("signature", "true");
                    setResult(RESULT_OK, intent);
                }
                SignActivity.this.finish();
            }
        });
    }
}
