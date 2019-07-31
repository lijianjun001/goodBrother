package com.antelope.goodbrother;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;


public class Util {
    @Test
    public void getDimen() {
        float oneDimen = (1660f / 800f);
        for (int i = 1; i <= 1000; i++) {
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            System.out.println("<dimen name=\"ds" + i + "\">" + decimalFormat.format(oneDimen * i) + "px</dimen>");
        }
    }

    public String className = "WebData";
    String packageName = (className.charAt(0)+"").toLowerCase() + className.substring(1, className.length());
    File fileDir = new File("/Users/mac/StudioProjects/goodBrother/app/src/main/java/com/antelope/goodbrother/business", packageName);
    @Test
    public void commonActivity() throws IOException {
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File actFile = new File(fileDir.getAbsoluteFile(), className + "Activity.java");
        String classA = "package com.antelope.goodbrother.business." + packageName + ";\n" +
                "\n" +
                "import android.os.Bundle;\n" +
                "\n" +
                "import com.nirvana.zmkj.base.BaseActivity;\n" +
                "\n" +
                "public class " + className + "Activity extends BaseActivity {\n" +
                "    @Override\n" +
                "    protected void onCreate(Bundle savedInstanceState) {\n" +
                "        super.onCreate(savedInstanceState);\n" +
                "        setContentView(new " + className + "ViewHolder(this).getRootView());\n" +
                "    }\n" +
                "\n" +
                "}\n";
        FileOutputStream fileOutputStream = new FileOutputStream(actFile);
        fileOutputStream.write(classA.getBytes());
        fileOutputStream.close();
        System.out.println(classA);
    }

    @Test
    public void commonViewHolder() throws IOException {

        File holderFile = new File(fileDir.getAbsoluteFile(), className + "ViewHolder.java");
        String classA = "package com.antelope.goodbrother.business." + packageName + ";\n" +
                "\n" +
                "import android.support.v7.app.AppCompatActivity;\n" +
                "import android.view.View;\n" +
                "\n" +
                "import com.nirvana.zmkj.base.BaseViewHolder;\n" +
                "\n" +
                "public class " + className + "ViewHolder extends BaseViewHolder<" + className + "Presenter> {\n" +
                "    public " + className + "ViewHolder(AppCompatActivity activity) {\n" +
                "        super(activity);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    protected " + className + "Presenter createPresenter() {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public int getLayoutId() {\n" +
                "        return 0;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public void initContent(View contentView) {\n" +
                "\n" +
                "    }\n" +
                "}\n";
        FileOutputStream fileOutputStream = new FileOutputStream(holderFile);
        fileOutputStream.write(classA.getBytes());
        fileOutputStream.close();
        System.out.println(classA);
    }


    @Test
    public void commonPresenter() throws IOException {
        File fileDir = new File("/Users/mac/StudioProjects/zhongmubao_2/app/src/main/java/com/nirvana/ylmc/business", packageName);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File presenterFile = new File(fileDir.getAbsoluteFile(), className + "Presenter.java");
        String classA = "package com.antelope.goodbrother.business." + packageName + ";\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "\n" +
                "import com.nirvana.zmkj.base.BasePresenter;\n" +
                "\n" +
                "public class " + className + "Presenter extends BasePresenter {\n" +
                "    public " + className + "Presenter(Activity activity) {\n" +
                "        super(activity);\n" +
                "    }\n" +
                "}\n";
        FileOutputStream fileOutputStream = new FileOutputStream(presenterFile);
        fileOutputStream.write(classA.getBytes());
        fileOutputStream.close();
        System.out.println(classA);
    }

}
