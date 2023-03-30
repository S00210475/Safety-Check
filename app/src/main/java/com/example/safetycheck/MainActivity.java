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
    private Button submitButton, buttonOne, buttonTwo, buttonThree, buttonFour;
    private TextView remainingTimeTextView;
    private int remainingTime, startingTime = 60000;
    private CountDownTimer timer;
    String password ="", correctPassword = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactivity);
        Log.i("Test", "Buttons being found");

        buttonOne = findViewById(R.id.btn1);
        buttonTwo = findViewById(R.id.btn2);
        buttonThree = findViewById(R.id.btn3);
        buttonFour = findViewById(R.id.btn4);
        remainingTimeTextView = findViewById(R.id.remainingTimeTextView);
        submitButton = findViewById(R.id.submit);
        Log.i("Test", "Buttons found");

        timer = new CountDownTimer(startingTime, 1000) {
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

                if (password.equals(correctPassword)) {
                    timer.cancel();
                    timer.start();
                    Toast.makeText(MainActivity.this, "Password Correct", Toast.LENGTH_SHORT).show();
                    password = "";
                } else {
                    Log.i("Test", password);
                    Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                    password = "";
                }
            }
        });
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = password + "1";
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = password + "2";
            }
        });
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = password + "3";
            }
        });
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = password + "4";
            }
        });
    }

    private void sendEmail() {
        Log.i("Test", "Send Email Start");
        String host="mail.javatpoint.com";
        final String user="";//change accordingly
        final String password="";//change accordingly

        String to="";//change accordingly
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