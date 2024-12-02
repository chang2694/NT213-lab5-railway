package com.example.session_5;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private EditText emailEditText, usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailTextView);
        usernameEditText = findViewById(R.id.usernameTextView);
        passwordEditText = findViewById(R.id.passwordTextView);

        findViewById(R.id.registerButton).setOnClickListener(v -> register());
    }

    private void register() {
        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            // Thực hiện đăng ký trong một AsyncTask để tránh block UI thread
            new RegisterTask().execute(username, password, email);
        }
    }
    private class RegisterTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                // Kết nối tới cơ sở dữ liệu MySQL
                String url = "jdbc:mysql://junction.proxy.rlwy.net:52217/railway?useSSL=false";
                String user = "root";
                String pass = "akYHfYyiMuapafjxQRLGPMnwNmNpEVqk";

                Log.d("Connection", "connecting to db");

                // Mở kết nối
                connection = DriverManager.getConnection(url, user, pass);

                // Thực hiện câu lệnh SQL để thêm người dùng vào cơ sở dữ liệu
                String query = "INSERT INTO users (user_name, user_email, user_password) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, email);
                statement.setString(3, password);

                int result = statement.executeUpdate();
                return result > 0;  // Trả về true nếu thêm thành công, ngược lại false

            } catch (SQLException e) {
                Log.e("DatabaseError", "Error during database operation", e);
                return false;
            } finally {
                // Đảm bảo đóng kết nối và statement sau khi hoàn thành
                try {
                    if (statement != null) statement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    Log.e("DatabaseError", "Error closing resources", e);
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Register.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        }
    }

}