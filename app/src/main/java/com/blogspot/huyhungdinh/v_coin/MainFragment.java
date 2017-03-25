package com.blogspot.huyhungdinh.v_coin;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by HUNGDH on 1/5/2016.
 */
public class MainFragment extends Fragment {

    private ListView lv_apps;
    private ArrayList<MyApps> listApps;
    private ListAppsAdapter adapter;

    public MainFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkInfo();

        isEmulator();

        Log.d("MYLOG", "EMULATOR? " + isRunOnEmulator());
    }

    private void checkInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("MYLOG", "IMEI: " + imei + "\nID: " + android_id);
        if (imei.equals("000000000000000")) {
            Log.d("MYLOG", "IS EMULATOR");
        } else {
            Log.d("MYLOG", "NOT EMULATOR");
        }
    }

    private void isEmulator() {
        if (Build.BRAND.equalsIgnoreCase("generic")) {
            Log.d("MYLOG", "YES, I am an emulator");
        } else {
            Log.d("MYLOG", "NO, I am NOT an emulator");
        }
    }

    private boolean isRunOnEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic")
                && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_apps = (ListView) view.findViewById(R.id.lv_apps);

        initUI();
    }

    private void initUI() {
        listApps = new ArrayList<MyApps>();
        listApps.add(new MyApps("Candy Crush Saga", "com.king.candycrushsaga", R.drawable.ic_candy_crush, 1000, false, null, null, false));
        listApps.add(new MyApps("Dragon City", "es.socialpoint.DragonCity", R.drawable.ic_dragon_city, 1500, false, null, null, false));
        listApps.add(new MyApps("Jurassic Park", "com.ludia.jurassicpark", R.drawable.ic_jurassic_park, 2000, false, null, null, false));
        listApps.add(new MyApps("Zombie Tsunami", "net.mobigame.zombietsunami", R.drawable.ic_zombie_tsunami, 2500, false, "Tên nhà phát hành game Zombie Tsunami?", "mobigame", false));

        adapter = new ListAppsAdapter(getActivity(), listApps);
        lv_apps.setAdapter(adapter);

        lv_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listApps.get(position).isInstalled()) {
                    dialogInstalled(position);
                } else {
                    dialogNotInstalled(position);
                }
            }
        });
    }

    private void dialogInstalled(int position) {
        if (!listApps.get(position).isFinished()) {
            if (listApps.get(position).getQuestion() != null) {
                dialogQuestion(position);
            } else {
                listApps.get(position).setFinished(true);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Bạn đã hoàn thành nhiệm vụ!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dialogNotInstalled(int position) {
        getApps(listApps.get(position).getLink());
    }

    private void dialogQuestion(final int position) {
        String question = listApps.get(position).getQuestion();
        final String answer = listApps.get(position).getAnswer();

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_question);

        TextView tv_question = (TextView) dialog.findViewById(R.id.tv_question);
        tv_question.setText(question);

        final EditText et_answer = (EditText) dialog.findViewById(R.id.et_answer);

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);

        btn_cancel.setText("Hủy bỏ");
        btn_ok.setText("Trả lời");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAnswer = et_answer.getText().toString();
                if (inputAnswer.trim().equalsIgnoreCase(answer)) {
                    listApps.get(position).setFinished(true);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Chính xác!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Sai!", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getApps(String appPackageName) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkInstall();
    }

    private void checkInstall() {
        for (int i = 0; i < listApps.size(); i++) {
            if (isPackageInstalled(listApps.get(i).getLink(), getActivity())) {
                listApps.get(i).setInstalled(true);
            } else {
                listApps.get(i).setInstalled(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
