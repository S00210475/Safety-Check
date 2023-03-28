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
        Log.i("Test", "Send Email Start");
        String host="mail.javatpoint.com";
        final String user="safetycheckapplication@gmail.com";//change accordingly
        final String password="GDSC2023";//change accordingly

        String to="chriscurran01@gmail.com";//change accordingly
        Log.i("Test", "Properties Start");
        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.auth", "true");
        Log.i("Test", "Session start");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });
        Log.i("Test", "Message Start");
        //Compose the message
        //This code is not working as intended, will be resorting to cloud functions in future
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("javatpoint");
            message.setText("This is simple program of sending email using JavaMail API");
            Log.i("Test", "Sending Email");
            //send the message
            Transport.send(message);

            Log.i("Test", "Sent Email");

        } catch (MessagingException e) {e.printStackTrace();}
    }
}