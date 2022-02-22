package com.example.tulipsante.views.dialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tulipsante.R;
import com.example.tulipsante.utils.Mail;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class MailPdfDialogFragment extends BottomSheetDialogFragment {

    private Context context;
    private CardView cancel, cardViewSave;
    private EditText editTextTo, editTextSub, editTextFrom, editTextBody;
    private PDFView pdfView;

    private File filePath;

    static final String _user = "tuliptest2021@gmail.com";
    static final String _pass = "Tuliptest21";

    public void setFilePath(File filePath) {
        this.filePath = filePath;
    }

    public MailPdfDialogFragment(File filePath) {
        this.filePath = filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mail_pdf, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.bottom_sheet);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        final View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight(),true);
        });
    }

    private void initViews(View view) {
        cancel = view.findViewById(R.id.cancelDialog);
        cardViewSave = view.findViewById(R.id.cardViewSave);
        pdfView = view.findViewById(R.id.pdfView);
        editTextBody = view.findViewById(R.id.editTextBody);
        editTextSub = view.findViewById(R.id.editTextSub);
        editTextTo = view.findViewById(R.id.editTextTo);
        editTextFrom = view.findViewById(R.id.editTextFrom);
    }

    private void initialisation() {
        pdfView.fromFile(filePath).load();
        editTextFrom.setText(_user);
        editTextSub.setText("Patient card");
        editTextBody.setText("Here is your patient card.");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initialisation();

        onDialogCancel();
        onButtonSendPressed();
    }

    private void onButtonSendPressed() {
        cardViewSave.setOnClickListener(view -> {
            if(editTextFrom.getText().toString().isEmpty() && editTextTo.getText().toString().isEmpty()) {
                Toast.makeText(context, "Enter sender and the receiver!", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    sendMessage();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void onDialogCancel() {
        cancel.setOnClickListener(view -> dismiss());
    }

    private void sendMessage() throws Exception {
        String[] recipients = { editTextTo.getText().toString() };
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail(_user, _pass);
        email.m.set_from(_user);
        email.m.setBody(editTextBody.getText().toString());
        email.m.set_to(recipients);
        email.m.set_subject(editTextSub.getText().toString());
        email.m.addAttachment(filePath.toString());
        email.execute();
        dismiss();
    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;

        public SendEmailAsyncTask() {}

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                    System.out.println("Email sent.");
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Email sent.", Toast.LENGTH_SHORT).show());
                    dismiss();
                } else {
                    System.out.println("Email failed to send.");
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Email failed to send.", Toast.LENGTH_SHORT).show());
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
//                Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
                e.printStackTrace();
//                Toast.makeText(getContext(), "Email failed to send.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getContext(), "Unexpected error occured.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}