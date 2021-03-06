package exa.lnx.a;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Calendar;
import java.util.Date;

public class DashBoard extends Fragment {

    Context context;
    Button button;
    Button button2;
    Button button3;
    TextView textView2;
    TextView textView3;
    String distro;
    String s;
    boolean shouldShowAds;
    boolean isOreoNotified;
    SharedPreferences sharedPreferences;
    InterstitialAd mInterstitialAd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        getActivity().setTitle(R.string.dashboard);

        View view = inflater.inflate(R.layout.dashboard, container, false);

        context = getActivity().getApplicationContext();
        sharedPreferences = context.getSharedPreferences("GlobalPreferences", 0);
        isOreoNotified = sharedPreferences.getBoolean("IsOreoNotified", false);

        distro = "Nothing";

        s = Build.SUPPORTED_ABIS[0];

        shouldShowAds = false;

        if(s.equals("mips") | s.equals("mips64")){
            Toast.makeText(context, "Your device is not supported", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-5748356089815497/3581271493");

        if(!donationInstalled() && !isVideoAdsWatched()){
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);

        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        textView2.setText("Step 2 : Please choose a distro first");
        textView3.setText("Step 3 : Please choose a distro first.");
        button2.setEnabled(false);
        button3.setEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(s.equals("arm64-v8a") | !s.contains("arm")){
                    notifyUserToChooseDistro();
                }else{
                    notifyUserToChooseDistroARM();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                if(distro.equals("Ubuntu")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Ubuntu/ubuntu.sh && bash ubuntu.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Debian")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Debian/debian.sh && bash debian.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Kali")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Kali/kali.sh && bash kali.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Fedora")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Fedora/fedora.sh && bash fedora.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("CentOS")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/CentOS/centos.sh && bash centos.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Leap")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Leap/opensuse-leap.sh && bash opensuse-leap.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Tumbleweed")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Tumbleweed/opensuse-tumbleweed.sh && bash opensuse-tumbleweed.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("openSUSE")){
                    ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/armhf/opensuse.sh && bash opensuse.sh");
                    clipboard.setPrimaryClip(clip);
                }else if(distro.equals("Arch")){
                    if(s.equals("x86_64")){
                        ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/amd64/arch.sh && bash arch.sh");
                        clipboard.setPrimaryClip(clip);
                    }else{
                        ClipData clip = ClipData.newPlainText("Command", "pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/armhf/arch.sh && bash arch.sh");
                        clipboard.setPrimaryClip(clip);
                    }
                }else if(distro.equals("BlackArch")){
                    ClipData clip = ClipData.newPlainText("Command", "pacman-key --init && pacman-key --populate archlinuxarm && pacman -Sy --noconfirm curl && curl -O https://blackarch.org/strap.sh && chmod +x strap.sh && ./strap.sh");
                    clipboard.setPrimaryClip(clip);
                }
                if(mInterstitialAd != null && mInterstitialAd.isLoaded() && shouldShowAds){
                    if(!donationInstalled() && !isVideoAdsWatched()){
                        mInterstitialAd.show();
                    }
                    shouldShowAds = false;
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.termux");
                if(isPackageInstalled("com.termux", context.getPackageManager())){
                    startActivity(intent);
                }else{
                    notifyUserForInstallTerminal();
                }
            }
        });
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
        if(Build.VERSION.SDK_INT >= 26){
            if(!isOreoNotified){
                showOreoDialog();
            }
        }
        return view;
    }
    public void notifyUserToChooseDistro(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.distro_chooser, nullParent);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = view.findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = view.findViewById(R.id.checkBox5);
        final CheckBox checkBox6 = view.findViewById(R.id.checkBox6);
        final CheckBox checkBox7 = view.findViewById(R.id.checkBox7);
        final CheckBox checkBox8 = view.findViewById(R.id.checkBox8);
        final CheckBox checkBox9 = view.findViewById(R.id.checkBox9);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        if(distro.equals("Ubuntu")){
            checkBox.setChecked(true);
        }else if(distro.equals("Debian")){
            checkBox2.setChecked(true);
        }else if(distro.equals("Kali")){
            checkBox3.setChecked(true);
        }else if(distro.equals("Fedora")){
            checkBox4.setChecked(true);
        }else if(distro.equals("CentOS")){
            checkBox5.setChecked(true);
        }else if(distro.equals("Leap")){
            checkBox6.setChecked(true);
        }else if(distro.equals("Tumbleweed")){
            checkBox7.setChecked(true);
        }else if(distro.equals("Arch")){
            checkBox8.setChecked(true);
        }else if(distro.equals("BlackArch")){
            checkBox9.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox9.setChecked(false);
            }
        });
        checkBox9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        if(s.equals("i386")){
            checkBox4.setEnabled(false);
            checkBox6.setEnabled(false);
            checkBox7.setEnabled(false);
            checkBox8.setEnabled(false);
            checkBox9.setEnabled(false);
            checkBox4.setText("Not supported");
            checkBox6.setText("Not supported");
            checkBox7.setText("Not supported");
            checkBox8.setText("Not supported");
            checkBox9.setText("Not supported");
        }
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(checkBox.isChecked()){
                    if(!distro.equals("Ubuntu")){
                        shouldShowAds = true;
                        distro = "Ubuntu";
                    }
                }else if(checkBox2.isChecked()){
                    if(!distro.equals("Debian")){
                        shouldShowAds = true;
                        distro = "Debian";
                    }
                }else if(checkBox3.isChecked()){
                    if(!distro.equals("Kali")){
                        shouldShowAds = true;
                        distro = "Kali";
                    }
                }else if(checkBox4.isChecked()){
                    if(!distro.equals("Fedora")){
                        shouldShowAds = true;
                        distro = "Fedora";
                    }
                }else if(checkBox5.isChecked()){
                    if(!distro.equals("CentOS")){
                        shouldShowAds = true;
                        distro = "CentOS";
                    }
                }else if(checkBox6.isChecked()){
                    if(!distro.equals("Leap")){
                        shouldShowAds = true;
                        distro = "Leap";
                    }
                }else if(checkBox7.isChecked()){
                    if(!distro.equals("Tumbleweed")){
                        shouldShowAds = true;
                        distro = "Tumbleweed";
                    }
                }else if(checkBox8.isChecked()){
                    if(!distro.equals("Arch")){
                        shouldShowAds = true;
                        distro = "Arch";
                    }
                }else if(checkBox9.isChecked()){
                    if(!distro.equals("BlackArch")){
                        shouldShowAds = true;
                        distro = "BlackArch";
                    }
                }
                if(distro.equals("Ubuntu")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Ubuntu/ubuntu.sh && bash ubuntu.sh \n\n This should install Ubuntu on your system, you can then run ./start-ubuntu.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-ubuntu.sh to run the command line.");
                }else if(distro.equals("Debian")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Debian/debian.sh && bash debian.sh \n\n This should install Debian on your system, you can then run ./start-debian.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-debian.sh to run the command line.");
                }else if(distro.equals("Kali")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Kali/kali.sh && bash kali.sh \n\n This should install Kali on your system, you can then run ./start-kali.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-kali.sh to run the command line.");
                }else if(distro.equals("Fedora")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Fedora/fedora.sh && bash fedora.sh \n\n This should install Fedora on your system, you can then run ./start-fedora.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-fedora.sh to run the command line.");
                }else if(distro.equals("CentOS")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/CentOS/centos.sh && bash centos.sh \n\n This should install CentOS on your system, you can then run ./start-centos.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-centos.sh to run the command line.");
                }else if(distro.equals("Leap")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Leap/opensuse-leap.sh && bash opensuse-leap.sh \n\n This should install openSUSE Leap on your system, you can then run ./start-leap.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-leap.sh to run the command line.");
                }else if(distro.equals("Tumbleweed")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Tumbleweed/opensuse-tumbleweed.sh && bash opensuse-tumbleweed.sh \n\n This should install openSUSE Tumbleweed on your system, you can then run ./start-tumbleweed.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-tumbleweed.sh to run the command line.");
                }else if(distro.equals("Arch")){
                    if(s.equals("x86_64")){
                        textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/amd64/arch.sh && bash arch.sh && bash start-arch.sh && wget \n\n This should install Arch Linux on your system, you can then run ./start-arch.sh to run the command line.\n\nAfter that, you will need to run: chmod 755 additional.sh & ./additional.sh to setup resolv.conf, otherwise network connection wont work!!! (First time only)");
                        textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-arch.sh to run the command line, after that, you will also need to run ./additional.sh to setup resolv.conf, otherwise network connection wont work!!!");
                    }else{
                        textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/armhf/arch.sh && bash arch.sh \n\n This should install Arch Linux on your system, you can then run ./start-arch.sh to run the command line.\n\nAfter that, you will need to run: chmod 755 additional.sh & ./additional.sh to setup resolv.conf, otherwise network connection wont work!!! (First time only)");
                        textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-arch.sh to run the command line, after that, you will also need to run ./additional.sh to setup resolv.conf, otherwise network connection wont work!!!");
                    }
                    notifyUserForDeviceSpace();
                }else if(distro.equals("BlackArch")){
                    textView2.setText("Note: Please run this command inside Arch Linux shell to enable BlackArch Repository (Arch Linux need to be installed first).\n\nStep 2 : Copy the command to clipboard : pacman-key --init && pacman-key --populate archlinuxarm && pacman -Sy --noconfirm curl && curl -O https://blackarch.org/strap.sh && chmod +x strap.sh && ./strap.sh");
                    textView3.setText("Step 3 : Start Termux, enter Arch Linux shell by running ./start-arch.sh, then paste and enter the command to install BlackArch Repo. Remember: you will need to run ./start-arch.sh to enter Arch Linux shell first before using the command above.");
                }
                button2.setEnabled(true);
                button3.setEnabled(true);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void notifyUserToChooseDistroARM(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.distro_chooser_arm, nullParent);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = view.findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = view.findViewById(R.id.checkBox5);
        final CheckBox checkBox6 = view.findViewById(R.id.checkBox6);
        final CheckBox checkBox7 = view.findViewById(R.id.checkBox7);
        final CheckBox checkBox8 = view.findViewById(R.id.checkBox8);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        if(distro.equals("Ubuntu")){
            checkBox.setChecked(true);
        }else if(distro.equals("Debian")){
            checkBox2.setChecked(true);
        }else if(distro.equals("Kali")){
            checkBox3.setChecked(true);
        }else if(distro.equals("Fedora")){
            checkBox4.setChecked(true);
        }else if(distro.equals("CentOS")){
            checkBox5.setChecked(true);
        }else if(distro.equals("openSUSE")){
            checkBox6.setChecked(true);
        }else if(distro.equals("Arch")){
            checkBox7.setChecked(true);
        }else if(distro.equals("BlackArch")){
            checkBox8.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox8.setChecked(false);
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        if(s.equals("i386")){
            checkBox4.setEnabled(false);
            checkBox6.setEnabled(false);
            checkBox7.setEnabled(false);
            checkBox8.setEnabled(false);
            checkBox4.setText("Not supported");
            checkBox6.setText("Not supported");
            checkBox7.setText("Not supported");
            checkBox8.setText("Not supported");
        }
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(checkBox.isChecked()){
                    if(!distro.equals("Ubuntu")){
                        shouldShowAds = true;
                        distro = "Ubuntu";
                    }
                }else if(checkBox2.isChecked()){
                    if(!distro.equals("Debian")){
                        shouldShowAds = true;
                        distro = "Debian";
                    }
                }else if(checkBox3.isChecked()){
                    if(!distro.equals("Kali")){
                        shouldShowAds = true;
                        distro = "Kali";
                    }
                }else if(checkBox4.isChecked()){
                    if(!distro.equals("Fedora")){
                        shouldShowAds = true;
                        distro = "Fedora";
                    }
                }else if(checkBox5.isChecked()){
                    if(!distro.equals("CentOS")){
                        shouldShowAds = true;
                        distro = "CentOS";
                    }
                }else if(checkBox6.isChecked()){
                    if(!distro.equals("openSUSE")){
                        shouldShowAds = true;
                        distro = "openSUSE";
                    }
                }else if(checkBox7.isChecked()){
                    if(!distro.equals("Arch")){
                        shouldShowAds = true;
                        distro = "Arch";
                    }
                }else if(checkBox8.isChecked()){
                    if(!distro.equals("BlackArch")){
                        shouldShowAds = true;
                        distro = "BlackArch";
                    }
                }
                if(distro.equals("Ubuntu")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Ubuntu/ubuntu.sh && bash ubuntu.sh \n\n This should install Ubuntu on your system, you can then run ./start-ubuntu.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-ubuntu.sh to run the command line.");
                }else if(distro.equals("Debian")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Debian/debian.sh && bash debian.sh \n\n This should install Debian on your system, you can then run ./start-debian.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-debian.sh to run the command line.");
                }else if(distro.equals("Kali")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Kali/kali.sh && bash kali.sh \n\n This should install Kali on your system, you can then run ./start-kali.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-kali.sh to run the command line.");
                }else if(distro.equals("Fedora")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Fedora/fedora.sh && bash fedora.sh \n\n This should install Fedora on your system, you can then run ./start-fedora.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-fedora.sh to run the command line.");
                }else if(distro.equals("CentOS")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/CentOS/centos.sh && bash centos.sh \n\n This should install CentOS on your system, you can then run ./start-centos.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-centos.sh to run the command line.");
                }else if(distro.equals("openSUSE")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/armhf/opensuse.sh && bash opensuse.sh \n\n This should install openSUSE on your system, you can then run ./start-opensuse.sh to run the command line.");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-opensuse.sh to run the command line.");
                }else if(distro.equals("Arch")){
                    textView2.setText("Step 2 : Copy the command to clipboard : pkg install wget proot tar -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/armhf/arch.sh && bash arch.sh \n\n This should install Arch Linux on your system, you can then run ./start-arch.sh to run the command line.\n\nAfter that, you will need to run ./additional.sh to setup resolv.conf, otherwise network connection wont work!!! (First time only)");
                    textView3.setText("Step 3 : Start Termux, paste and enter the command to install distro. Remember: you will need to run ./start-arch.sh to run the command line, after that, you will also need to run: chmod 755 additional.sh & ./additional.sh to setup resolv.conf, otherwise network connection wont work!!! (First time only)");
                }else if(distro.equals("BlackArch")){
                    textView2.setText("Note: Please run this command inside Arch Linux shell to enable BlackArch Repository. (Arch Linux need to be installed first)\n\nStep 2 : Copy the command to clipboard : pacman-key --init && pacman-key --populate archlinuxarm && pacman -Sy --noconfirm curl && curl -O https://blackarch.org/strap.sh && chmod +x strap.sh && ./strap.sh");
                    textView3.setText("Step 3 : Start Termux, enter Arch Linux shell by running ./start-arch.sh, then paste and enter the command to install BlackArch Repo. Remember: you will need to run ./start-arch.sh to enter Arch Linux shell first before using the command above.");
                }
                button2.setEnabled(true);
                button3.setEnabled(true);
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void notifyUserForInstallTerminal(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = Uri.parse("market://details?id=com.termux");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if(Build.VERSION.SDK_INT >= 21){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try{
                    startActivity(intent);
                }catch(ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.termux")));
                }
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Termux is not installed, do you want to install it now ?");
    }
    public void notifyUserForDeviceSpace(){
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.notify1, nullParent);
        TextView textView = view.findViewById(R.id.textView);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        textView.setText("Arch Linux image is bigger than other distro (Around 300 MB), and may cost a lot of device space after decompressing. Having spare space over 1.5GB is highly recommended before installing.\n\n\nNote:\n\n1. Tar may produce error while extracting the image such as: tar: Ignoring unknown extended header keyword 'SCHILY.fflags', it is not an error, simply igrone it.\n\n2. Arch Linux image is hosted on external server, the link maybe broken is some special case, Please email the developers at exalabdevelopers@gmail.com if encountered this issue.");
    }
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private boolean donationInstalled() {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.checkSignatures(context.getPackageName(), "exa.lnx.d") == PackageManager.SIGNATURE_MATCH;
    }
    private boolean isVideoAdsWatched(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        cal.setTime(date);
        int a =  cal.get(Calendar.DAY_OF_MONTH);
        int b = sharedPreferences.getInt("VideoAds", 0);
        return a == b;
    }
    protected void showOreoDialog(){

        final ViewGroup nullParent = null;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.oreo_warning, nullParent);
        CheckBox checkBox = view.findViewById(R.id.checkBox);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which){
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("IsOreoNotified", true);
                editor.apply();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }
        });
    }
}
