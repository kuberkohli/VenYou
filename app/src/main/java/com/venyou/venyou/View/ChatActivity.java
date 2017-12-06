package com.venyou.venyou.View;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.venyou.venyou.R;
import c.R;

import java.lang.ref.WeakReference;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.ui.AIDialog;

public class ChatActivity extends AppCompatActivity implements AIListener, AIDialog.AIDialogListener{

    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;
    private AIDataService aiDataService;
    private EditText Etext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listenButton = (Button) findViewById(R.id.listenButton);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        Etext = (EditText) findViewById(R.id.editText);

        final ai.api.android.AIConfiguration config = new ai.api.android.AIConfiguration("0fecd05192a340d4b561acc49eae8d0f",
                ai.api.android.AIConfiguration.SupportedLanguages.Spanish,
                ai.api.android.AIConfiguration.RecognitionEngine.System);

        // Use with text search
        aiDataService = new AIDataService(this, config);
        // Use with Voice input
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        final AIRequest aiRequest = new AIRequest();

        if(aiRequest==null) {
            throw new IllegalArgumentException("aiRequest must be not null");
        }

        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = Etext.getText().toString();
                if(!str.equals("")){
                    aiRequest.setQuery(String.valueOf(Etext.getText()));
                    MyTask task = new MyTask(ChatActivity.this);
                    task.execute(aiRequest);
                }else{
                    Snackbar.make(getCurrentFocus(),"Kindly enter a comment in the text field.",Snackbar.LENGTH_SHORT).show();
                }
                Etext.setText("");
            }
        });
    }


    private class MyTask extends AsyncTask<AIRequest, Integer, AIResponse> {

        private WeakReference<ChatActivity> activityReference;
        private AIError aiError;

        // only retain a weak reference to the activity
        MyTask(ChatActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected AIResponse doInBackground(final AIRequest... params) {
            final AIRequest request = params[0];
            try {
                final AIResponse response =    aiDataService.request(request);
                // Return response
                return response;
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }


    public void listenButtonOnClick(final View view) {
        aiService.startListening();

    }


    @Override
    public void onResult(final AIResponse response) {
        Result result = response.getResult();
        String speech = result.getFulfillment().getSpeech();
        resultTextView.setText(speech);
    }


    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
