package com.joel.a0800restinga.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.Model.Usuario;
import com.joel.a0800restinga.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private FirebaseAuth mAuth;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton, mSignUpButton;
    private CheckBox mTermos;
    private TextView mLerTermos, mEsqueciSenha, mPossuoCadastro, mNaoPossuoCadastro;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        Toolbar tlb = (Toolbar) findViewById(R.id.toolbarLogin);

        setSupportActionBar(tlb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Registro");
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == '1' || id == EditorInfo.IME_NULL) {
                    attemptLoginOrRegister(false);
                    return true;
                }
                return false;
            }
        });



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mTermos = (CheckBox) findViewById(R.id.termos);
        mLerTermos = (TextView) findViewById(R.id.lertermos);
        mEsqueciSenha = (TextView) findViewById(R.id.esqueciMinhaSenha);
        mPossuoCadastro = (TextView) findViewById(R.id.possuoCadastro);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignUpButton = (Button)  findViewById(R.id.email_sign_up_button);
        mNaoPossuoCadastro = (TextView)  findViewById(R.id.naoPossuoCadastro);
        mPossuoCadastro.setOnClickListener(this);
        mEmailSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mLerTermos.setOnClickListener(this);
        mEsqueciSenha.setOnClickListener(this);
        mNaoPossuoCadastro.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("EMAIL_LOGIN", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("EMAIL_LOGIN", "");
        if  (! result.equals("")){
            mEmailView.setText(result);
        }


        if (user != null || result.equals("")) {
            updateUI("CADASTRAR");
        }else{
            updateUI("FAZER_LOGIN");
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void updateUI(String acao){
        if (acao.equals("FAZER_LOGIN")){

            mTermos.setVisibility(View.GONE);
            mSignUpButton.setVisibility(View.GONE);
            mEmailSignInButton.setVisibility(View.VISIBLE);
            mLerTermos.setVisibility(View.GONE);
            mEsqueciSenha.setVisibility(View.VISIBLE);
            mPossuoCadastro.setVisibility(View.GONE);
            mNaoPossuoCadastro.setVisibility(View.VISIBLE);
            mPasswordView.setVisibility(View.VISIBLE);
        }else if(acao.equals("CADASTRAR")){
            mTermos.setVisibility(View.VISIBLE);
            mLerTermos.setVisibility(View.VISIBLE);
            mEmailSignInButton.setVisibility(View.GONE);
            mSignUpButton.setVisibility(View.VISIBLE);
            mEsqueciSenha.setVisibility(View.GONE);
            mPossuoCadastro.setVisibility(View.VISIBLE);
            mNaoPossuoCadastro.setVisibility(View.GONE);
            mPasswordView.setVisibility(View.GONE);

        }else if(acao.equals("REDEFINIR")){

            updateUI("CADASTRAR");

        }else if(acao.equals("EXCLUIR_EMAIL")){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                startActivity(new Intent(LoginActivity.this, Inicial.class));
                finish(); // Finaliza a Activity atual

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed(){

        startActivity(new Intent(LoginActivity.this, Inicial.class));
        finish(); // Finaliza a Activity atual

        return;
    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLoginOrRegister(boolean isNewUser) {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (isNewUser)
            password = email + "" + "1acd2f1325gtr12";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if ((!TextUtils.isEmpty(password) && !isPasswordValid(password)) && (! isNewUser)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email não informado");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Email inválido");
            focusView = mEmailView;
            cancel = true;
        }else if((password.length() < 8) && (! isNewUser)){
            mPasswordView.setError("Senha deve possuir ao menos 8 digitos");
            focusView = mPasswordView;
            cancel = true;
        }else if(!mTermos.isChecked() && isNewUser) {
            mTermos.setError("Precisa aceitar os termos antes de concluir o registro");
            focusView = mTermos;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if(isNewUser) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                mPasswordView.setError("Senha inválida");
                                mPasswordView.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                mEmailView.setError("Email inválido");
                                mEmailView.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                mEmailView.setError("Email já existe");
                                mEmailView.requestFocus();
                            } catch(Exception e) {
                                Log.e("LOGIN", e.getMessage());
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso. Agora você pode se autenticar com suas credenciais!", Toast.LENGTH_LONG).show();
                            updateUI("FAZER_LOGIN");


                            sharedPreferences = getSharedPreferences("EMAIL_LOGIN", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EMAIL_LOGIN", email);
                            editor.apply();

                            FirebaseUser currentUser;
                            currentUser =mAuth.getCurrentUser();
                            String User = currentUser.getEmail();
                            String Token = currentUser.getUid();

                            insereUsuario(User, Token);
                        }
                    }
                });
            }
            else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);

                        if(task.isSuccessful()) {
                            if (task.getResult().getUser() != null) {

                                sharedPreferences = getSharedPreferences("EMAIL_LOGIN", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("EMAIL_LOGIN", email);
                                editor.apply();

                                startActivity(new Intent(LoginActivity.this, Inicial.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email e/ou senha incorretos.", Toast.LENGTH_LONG).show();
                            }
                        } else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            if ("ERROR_INVALID_CUSTOM_TOKEN".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_CUSTOM_TOKEN_MISMATCH".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_INVALID_CREDENTIAL".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_INVALID_EMAIL".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "O endereço de e-mail está formatado incorretamente.", Toast.LENGTH_LONG).show();
                                mEmailView.setError("The email address is badly formatted.");
                                mEmailView.requestFocus();
                            } else if ("ERROR_WRONG_PASSWORD".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "A senha é inválida ou o usuário não possui uma senha.", Toast.LENGTH_LONG).show();
                                mPasswordView.setError("Senha inválida");
                                mPasswordView.requestFocus();
                                mPasswordView.setText("");
                            } else if ("ERROR_USER_MISMATCH".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "As credenciais fornecidas não correspondem ao usuário conectado anteriormente.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_REQUIRES_RECENT_LOGIN".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "Já existe uma conta com o mesmo endereço de e-mail, mas com credenciais de login diferentes. Faça login usando um provedor associado a este endereço de e-mail\n.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_EMAIL_ALREADY_IN_USE".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "O endereço de e-mail já está sendo usado por outra conta.", Toast.LENGTH_LONG).show();
                                mEmailView.setError("O endereço de e-mail já está sendo usado por outra conta.");
                                mEmailView.requestFocus();
                            } else if ("ERROR_CREDENTIAL_ALREADY_IN_USE".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_USER_DISABLED".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "A conta do usuário foi desativada por um administrador.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_USER_TOKEN_EXPIRED".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_USER_NOT_FOUND".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_INVALID_USER_TOKEN".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_OPERATION_NOT_ALLOWED".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                            } else if ("ERROR_WEAK_PASSWORD".equals(errorCode)) {
                                Toast.makeText(getApplicationContext(), "A senha fornecida é inválida.", Toast.LENGTH_LONG).show();
                                mPasswordView.setError("A senha fornecida é inválida. Informe ao menos 8 characteres");
                                mPasswordView.requestFocus();
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            callDialog(LoginActivity.this, getApplicationContext(), "Você está desconectado", "Atenção");
        }else {
            if (v == mEmailSignInButton) {
                attemptLoginOrRegister(false);
            } else if (v == mSignUpButton) {
                attemptLoginOrRegister(true);
            } else if (v == mLerTermos) {
                Intent i = new Intent(LoginActivity.this,
                        PoliticaPrivacidadeActivity.class);
                startActivity(i);
            } else if (v == mEsqueciSenha) {
                ressetSenha();
            } else if (v == mPossuoCadastro) {
                updateUI("FAZER_LOGIN");
            } else if (v == mNaoPossuoCadastro) {
                updateUI("CADASTRAR");
            }
        }
    }

        private MaterialDialog mMaterialDialog;
        private void callDialog(final Activity activity, Context ctx,
                String message, String titulo){

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage(message)
                    .setTitle(titulo);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }



    private void ressetSenha(){
        mAuth.sendPasswordResetEmail( mEmailView.getText().toString() )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if( task.isSuccessful() ){
                            //mEmailView.setText("");
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Recuperação de acesso iniciada. Email enviado.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Falhou! Tente novamente",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void insereUsuario(String emailUser, String token){
        try {
            DatabaseReference databaseReference;
            databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

            Date dataHoraAtual = new Date();
            String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
            String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
            String dataatual = data + " " + hora;

            Usuario usuario = new Usuario(emailUser, token, dataatual, "P");


            String Id = token;

            databaseReference.child(Id).setValue(usuario);
            ressetSenha();

            callDialog(LoginActivity.this, getApplicationContext(), "Foi enviado um e-mail para que você redefina sua senha para ter acesso. Verifique!", "Cadastro");
        }catch (Exception ex){

        }
    }
}
