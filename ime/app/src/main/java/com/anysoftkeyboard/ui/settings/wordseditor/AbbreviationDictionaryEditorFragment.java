package com.anysoftkeyboard.ui.settings.wordseditor;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.anysoftkeyboard.dictionaries.EditableDictionary;
import com.anysoftkeyboard.dictionaries.sqlite.AbbreviationsDictionary;
import com.menny.android.anysoftkeyboard.R;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import net.evendanan.pixel.UiUtils;

public class AbbreviationDictionaryEditorFragment extends UserDictionaryEditorFragment {

  @NonNull private final CompositeDisposable mDisposable = new CompositeDisposable();

  @Override
  public void onStart() {
    super.onStart();
    UiUtils.setActivityTitle(this, getString(R.string.abbreviation_dict_settings_titlebar));
  }

  @Override
  public void onDestroy() {
    mDisposable.dispose();
    super.onDestroy();
  }

  @Override
  protected EditableDictionary createEditableDictionary(String locale) {
    return new MyAbbreviationsDictionary(requireContext().getApplicationContext(), locale);
  }

  @Override
  protected EditorWordsAdapter createAdapterForWords(List<LoadedWord> wordsList) {
    Activity activity = getActivity();
    if (activity == null) return null;
    return new AbbreviationEditorWordsAdapter(wordsList, activity, this);
  }

  private static class AbbreviationEditorWordsAdapter extends EditorWordsAdapter {

    private final Context mContext;

    public AbbreviationEditorWordsAdapter(
        List<LoadedWord> editorWords, Context context, DictionaryCallbacks dictionaryCallbacks) {
      super(editorWords, LayoutInflater.from(context), dictionaryCallbacks);
      mContext = context;
    }

    private static String getAbbreviation(@Nullable LoadedWord word) {
      if (word == null) return "";
      return AbbreviationsDictionary.getAbbreviation(word.word, word.freq);
    }

    private static String getExplodedSentence(@Nullable LoadedWord word) {
      if (word == null) return "";
      return AbbreviationsDictionary.getExplodedSentence(word.word, word.freq);
    }

    @Override
    protected Editing createEmptyNewEditing() {
      return new Editing("", 0);
    }

    @Override
    protected void bindNormalWordViewText(TextView wordView, LoadedWord editorWord) {
      wordView.setText(
          mContext.getString(
              R.string.abbreviation_dict_word_template,
              getAbbreviation(editorWord),
              getExplodedSentence(editorWord)));
    }

    @Override
    protected View inflateEditingRowView(LayoutInflater layoutInflater, ViewGroup parent) {
      return layoutInflater.inflate(R.layout.abbreviation_dictionary_word_row_edit, parent, false);
    }

    @Override
    protected void bindEditingWordViewText(EditText wordView, LoadedWord editorWord) {
      wordView.setText(getAbbreviation(editorWord));
      EditText explodedSentence = ((View) wordView.getParent()).findViewById(R.id.word_target_view);
      explodedSentence.setText(getExplodedSentence(editorWord));
    }

    @Override
    protected LoadedWord createNewEditorWord(EditText wordView, LoadedWord oldEditorWord) {
      EditText explodedSentenceView =
          ((View) wordView.getParent()).findViewById(R.id.word_target_view);
      final String newAbbreviation = wordView.getText().toString();
      final String newExplodedSentence = explodedSentenceView.getText().toString();
      if (TextUtils.isEmpty(newAbbreviation) || TextUtils.isEmpty(newExplodedSentence)) {
        return new LoadedWord(oldEditorWord.word, oldEditorWord.freq);
      } else {
        return new LoadedWord(newAbbreviation + newExplodedSentence, newAbbreviation.length());
      }
    }
  }

  private static class MyAbbreviationsDictionary extends AbbreviationsDictionary
      implements MyEditableDictionary {

    @NonNull private final List<LoadedWord> mLoadedWords = new ArrayList<>();

    public MyAbbreviationsDictionary(Context context, String locale) {
      super(context, locale);
    }

    @Override
    protected void readWordsFromActualStorage(final WordReadListener listener) {
      mLoadedWords.clear();
      WordReadListener myListener =
          (word, frequency) -> {
            mLoadedWords.add(new LoadedWord(word, frequency));
            return listener.onWordRead(word, frequency);
          };
      super.readWordsFromActualStorage(myListener);
    }

    @NonNull
    @Override
    public List<LoadedWord> getLoadedWords() {
      return mLoadedWords;
    }
  }
}
