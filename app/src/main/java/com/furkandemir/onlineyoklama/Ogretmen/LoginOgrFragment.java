package com.furkandemir.onlineyoklama.Ogretmen;

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

import com.furkandemir.onlineyoklama.Ogrenci.Login;
import com.furkandemir.onlineyoklama.Ogrenci.LoginFragment;
import com.furkandemir.onlineyoklama.R;
import com.furkandemir.onlineyoklama.Yoklama.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginOgrFragment extends Fragment {

    private EditText UserName, UserPassword;
    private CardView LoginCv;

    LoginOgrFragment.OnLoginFormActivityListener loginFormActivityListener;
    public interface OnLoginFormActivityListener{
        public void performLogin(String name);
    }

    public LoginOgrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_ogr, container, false);
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
        loginFormActivityListener = (LoginOgrFragment.OnLoginFormActivityListener) activity;
    }

    private void performLogin(){
        String username = UserName.getText().toString();
        String password = UserPassword.getText().toString();

        Call<User> call = LoginOgr.apiInterface.perforUserLogin_Ogr(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("ok")){
                    LoginOgr.prefConfig.writeLoginStatus(true);
                    loginFormActivityListener.performLogin(response.body().getName());
                }
                else if(response.body().getResponse().equals("failed")){
                    LoginOgr.prefConfig.displayToast("Lütfen Tekrar Deneyiniz...");
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
