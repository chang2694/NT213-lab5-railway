package com.example.session_5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("LoginActivity", "onCreate called");

        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);

        findViewById(R.id.loginButton).setOnClickListener(v -> login());
        findViewById(R.id.registerButton).setOnClickListener(v -> openRegisterActivity());
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Kiểm tra tài khoản từ cơ sở dữ liệu
        new LoginTask().execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                // Kết nối tới cơ sở dữ liệu MySQL
                String url = "jdbc:mysql://junction.proxy.rlwy.net:52217/railway?useSSL=false";
                String user = "root";
                String pass = "akYHfYyiMuapafjxQRLGPMnwNmNpEVqk";

                // Kết nối tới cơ sở dữ liệu
                connection = DriverManager.getConnection(url, user, pass);
                Log.d("Connection", "connected to db");

                // Kiểm tra tài khoản trong cơ sở dữ liệu
                String query = "SELECT * FROM users WHERE user_name = ? AND user_password = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);

                resultSet = statement.executeQuery();

                // Nếu tìm thấy tài khoản, trả về true
                return resultSet.next();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (resultSet != null) resultSet.close();
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                // Nếu đăng nhập thành công, chuyển sang activity Display
                String username = usernameEditText.getText().toString();
                Intent intent = new Intent(Login.this, Display.class);
                intent.putExtra("username", username); // Gửi tên người dùng qua Intent
                startActivity(intent);
                finish();
            } else {
                // Nếu đăng nhập thất bại, thông báo lỗi
                Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}
