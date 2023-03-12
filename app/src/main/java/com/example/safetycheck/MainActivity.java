package com.example.safetycheck;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {
    private EditText passwordEditText;
    private Button submitButton;
    private TextView remainingTimeTextView;
    private int remainingTime;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEditText = findViewById(R.id.PasswordEditText);
        submitButton = findViewById(R.id.Submit);
        remainingTimeTextView = findViewById(R.id.remainingTimeTextView);

        timer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000;
                remainingTimeTextView.setText("Remaining Time: " + remainingTime);
            }

            public void onFinish() {
                new Thread(new Runnable() {
                    public void run() {
                        sendEmail();
                    }
                }).start();
            }
        }.start();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();

                if (password.equals("correctpassword")) {
                    timer.cancel();
                    Toast.makeText(MainActivity.this, "Password Correct", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmail() {
        final String username = "safetycheckapplication@gmail.com";
        final String password = "GDSC2023";
        String to = "safetycheckapplication@gmail.com";
        String subject = "User did not make their safety check";
        String message = "This is test code for safety check";

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(message);

            Transport.send(msg);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MessagingException e) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, "Error sending email", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}