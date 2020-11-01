package com.example.learnenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    private boolean ttsInitialized = false;
    private TextToSpeech tts;
    private List<String> words;
    private final State state = new State();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            InitializeDictionary();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        tts = new TextToSpeech(MainActivity.this, status -> ttsInitialized = true,null);

        final EditText tipText = (EditText) findViewById(R.id.tipInput);
        final TextView wordCountTextView = (TextView) findViewById(R.id.wordCount);
        final TextView tipCount = (TextView) findViewById(R.id.tipCount);
        final TextView answerTextView = (TextView)findViewById(R.id.answer);
        final TextView scoreCountTextView = (TextView)findViewById(R.id.scoreCount);

        final Button repeatWordButton = (Button) findViewById(R.id.repeatWordButton);
        repeatWordButton.setOnClickListener(v ->
        {
            if (!ttsInitialized)
            {
                // Display info about initializing sound engine
                CharSequence text = "Sound engine is being initialized...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }
            else
            {
                ReadWord();
            }
        });

        final Button okButton = (Button) findViewById(R.id.tipButton);
        okButton.setOnClickListener(v ->
        {
            if (tipText.getText().toString().equals(state.getActualWord()))
            {
                tipText.setEnabled(false);

                CharSequence text = getString(R.string.figureOut);
                int duration = Toast.LENGTH_SHORT;

                okButton.setEnabled(false);

                state.setScoreCount(state.getScoreCount() + 1);
                scoreCountTextView.setText(String.valueOf(state.getScoreCount()));

                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }
            else
            {
                state.setTipCount(state.getTipCount() + 1);
                tipCount.setText(String.valueOf(state.getTipCount()));

                CharSequence text = getString(R.string.notFigureOut);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(MainActivity.this, text, duration);
                toast.show();
            }
        });

        final Button answerButton = (Button) findViewById(R.id.answerButton);
        answerButton.setOnClickListener(v ->
        {
            answerTextView.setText(state.getActualWord());
            answerTextView.setEnabled(false);
            okButton.setEnabled(false);
        });

        final Button nextWordButton = (Button) findViewById(R.id.nextWordButton);
        nextWordButton.setOnClickListener(v ->
        {
            Random random = new Random();

            state.setActualWord(words.get(random.nextInt(words.size())));
            state.setTipCount(0);
            state.setWordCount(state.getWordCount() + 1);

            tipCount.setText(String.valueOf(state.getTipCount()));
            wordCountTextView.setText(String.valueOf( state.getWordCount()));

            repeatWordButton.setEnabled(true);

            answerTextView.setEnabled(true);

            tipText.setText("");
            answerTextView.setText("");

            answerButton.setEnabled(true);
            okButton.setEnabled(true);
            tipText.setEnabled(true);

            nextWordButton.setText(R.string.NextWord);
            wordCountTextView.setText(String.valueOf( state.getWordCount()));

            ReadWord();
        });
    }

    private void ReadWord()
    {
        tts.speak(state.getActualWord(), TextToSpeech.QUEUE_FLUSH,null, "Read out loudr");
    }

    private void InitializeDictionary() throws IOException
    {
        words = new ArrayList<>();

        final Resources resources = this.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.dictionary);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
               words.add(line);
            }
        }
        finally
        {
            reader.close();
        }

        Log.d("TAG", "DONE loading words.");
    }
}