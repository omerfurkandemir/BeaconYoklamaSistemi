package com.furkandemir.onlineyoklama.Ogrenci;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.furkandemir.onlineyoklama.R;
import com.furkandemir.onlineyoklama.Yoklama.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText UserName, UserPassword;
    private CardView LoginCv;

    OnLoginFormActivityListener loginFormActivityListener;
    public interface OnLoginFormActivityListener{
        public void performLogin(String name);
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        UserName = view.findViewById(R.id.user_name);
        UserPassword = view.findViewById(R.id.user_pass);
        LoginCv = view.findViewById(R.id.login_cv);

        LoginCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    private void performLogin(){
        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();

        Call<User> call = Login.apiInterface.perforUserLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("ok")){
                    Login.prefConfig.writeLoginStatus(true);
                    loginFormActivityListener.performLogin(response.body().getName());
                }
                else if(response.body().getResponse().equals("failed")){
                    Login.prefConfig.displayToast("Lütfen Tekrar Deneyiniz...");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(),"Bağlantınızı kontrol edin...",Toast.LENGTH_SHORT).show();
            }
        });
        UserName.setText("");
        UserPassword.setText("");
    }

}
