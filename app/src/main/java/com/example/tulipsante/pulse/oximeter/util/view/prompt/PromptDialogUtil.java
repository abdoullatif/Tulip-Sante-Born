package com.example.tulipsante.pulse.oximeter.util.view.prompt;

import android.app.Activity;
import android.content.DialogInterface;

public class PromptDialogUtil {

    public static void showSingleDialog(Activity context, String content, String prompt, DialogInterface.OnClickListener listener) {
        PromptDialog dialog = new PromptDialog.Builder(context)
                .setContent(content)
                .setSingleBtn(prompt, listener)
                .create();

        dialog.show();
    }

    public static void showDoubleDialog(Activity context, String content, String cancel, String confirm, DialogInterface.OnClickListener listener) {
        PromptDialog dialog = new PromptDialog.Builder(context)
                .setContent(content)
                .setDoubleBtn(confirm, cancel, listener)
                .create();

        dialog.show();
    }

    public static void showThreeDialog(Activity context, String content, String cancel, String prompt, String confirm, DialogInterface.OnClickListener listener) {
        PromptDialog dialog = new PromptDialog.Builder(context)
                .setContent(content)
                .setThreeBtn(confirm, prompt, cancel, listener)
                .create();

        dialog.show();
    }

}
